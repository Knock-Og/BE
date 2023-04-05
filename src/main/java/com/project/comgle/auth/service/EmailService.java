package com.project.comgle.auth.service;

import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.auth.dto.EmailAuthResponseDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.repository.MemberRepository;
import com.project.comgle.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public SuccessResponse sendEmailCode(String toEmail) throws MessagingException {

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
    public EmailAuthResponseDto findPassWord(String toEmail, String code){

        String codeFoundByEmail = redisUtil.getData(toEmail);
        if (codeFoundByEmail == null) {
            throw new CustomException(ExceptionEnum.INVALID_VALUE);
        }

        Optional<Member> findMember = memberRepository.findByEmail(toEmail);
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        if(codeFoundByEmail.equals(code)){
            String newPwd = getRandomPwd();
            String newPwdEncode = passwordEncoder.encode(newPwd);

            findMember.get().updatePwd(newPwdEncode);

            return new EmailAuthResponseDto(newPwd);
        }

        throw new CustomException(ExceptionEnum.INVALID_VALUE);
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        String authNum = createCode();

        String title = "[Knock] 본인인증 코드 발송"; //이메일 제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setText(setContext(authNum), "utf-8", "html");
        redisUtil.setDataExpire(email, authNum, 60 * 3L);   // 3분 뒤 Redis에 저장된 인증코드 삭제

        return message;
    }

    private String createCode() {

        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for(int i = 0; i < 12; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }

        return key.toString();
    }

    private String setContext(String code) {

        Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process("mail", context);
    }

    private String getRandomPwd(){

        // 강력한 난수를 발생시키기 위해 SecureRandom을 사용
        SecureRandom sr = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };

        int len = charSet.length;

        sr.setSeed(new Date().getTime());

        for (int i = 0 ; i < 16 ; i++) {
            sb.append(charSet[sr.nextInt(len)]);
        }

        return sb.toString();
    }

}
