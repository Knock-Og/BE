package com.project.comgle.service;

import com.project.comgle.dto.common.SuccessResponse;
import com.project.comgle.dto.request.EmailAuthRequestDto;
import com.project.comgle.dto.response.EmailAuthResponseDto;
import com.project.comgle.entity.Member;
import com.project.comgle.exception.CustomException;
import com.project.comgle.exception.ExceptionEnum;
import com.project.comgle.repository.MemberRepository;
import com.project.comgle.service.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //메일 전송
    @Transactional
    public SuccessResponse sendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        Optional<Member> findMember = memberRepository.findByEmail(toEmail);
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        MimeMessage emailForm = createEmailForm(toEmail);

        emailSender.send(emailForm);

        return SuccessResponse.of(HttpStatus.CREATED, "Email sent successfully");
    }

    @Transactional
    public MimeMessage createEmailForm(String email) throws MessagingException {

        String authNum = createCode();
        log.info("authNum : " + authNum);

        String title = "[Knock] 본인인증 코드 발송"; //이메일 제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setText(setContext(authNum), "utf-8", "html");

        redisUtil.setDataExpire(email, authNum, 60 * 3L);   // 3분 뒤 Redis에 저장된 인증코드 삭제

        return message;
    }

    @Transactional
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i = 0; i < 12; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }

    @Transactional
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process("mail", context);
    }

    // 인증 코드 확인 후 비밀번호 인증
    @Transactional
    public EmailAuthResponseDto checkMailCode(String toEmail, String code){
        String codeFoundByEmail = redisUtil.getData(toEmail);
        if (codeFoundByEmail == null) {
            throw new CustomException(ExceptionEnum.INVALID_VALUE);
        }
        log.info("codeFoundByEmail : " + codeFoundByEmail);

        Optional<Member> findMember = memberRepository.findByEmail(toEmail);
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        if(codeFoundByEmail.equals(code)){
            String newPwd = getRamdomPwd();
            log.info("newPwd : " + newPwd);

            String newPwdEncode = passwordEncoder.encode(newPwd);
            log.info("Encoding newPwd : " + newPwdEncode);

            findMember.get().updatePwd(newPwdEncode);

            return new EmailAuthResponseDto(newPwd);
        }

        throw new CustomException(ExceptionEnum.INVALID_VALUE);
    }

    // 비밀번호 랜덤
    @Transactional
    public String getRamdomPwd(){
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };

        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();   // 강력한 난수를 발생시키기 위해 SecureRandom을 사용
        sr.setSeed(new Date().getTime());

        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<16; i++) {
            idx = sr.nextInt(len);
            sb.append(charSet[idx]);
        }

        return sb.toString();
    }
}
