package com.project.comgle.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.comgle.auth.dto.FindEmailRequestDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.repository.MemberRepository;
import com.project.comgle.auth.dto.SmsMessageDto;
import com.project.comgle.auth.dto.SmsRequestDto;
import com.project.comgle.auth.dto.SmsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final MemberRepository memberRepository;
    private static final Map<String,Integer> smsCodeMap = new ConcurrentHashMap<>();

    // 환경변수 설정
    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    @Transactional(readOnly = true)
    public ResponseEntity<SmsResponseDto> sendSmsCode(FindEmailRequestDto emailCheckRequestDto) {

        Member findMember = memberRepository.findByMemberNameStartingWithAndPhoneNum(emailCheckRequestDto.getMemberName(),
                emailCheckRequestDto.getPhoneNum()).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_MEMBER)
        );

        int authenticationCode = new Random().nextInt(900000)+100000;

        SmsResponseDto smsResponseDto ;
        try {
            smsResponseDto = sendSms(SmsMessageDto.of( findMember.getPhoneNum().replaceAll("-", ""), "[knock 본인인증] " + authenticationCode ));
            smsResponseDto.setPhoneNum(findMember.getPhoneNum());
        } catch (IOException | URISyntaxException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new CustomException(ExceptionEnum.SMS_SEND_ERR);
        }
        smsCodeMap.put(findMember.getPhoneNum(), authenticationCode);

        return ResponseEntity.ok()
                .body(smsResponseDto);

    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> findEmail(FindEmailRequestDto emailCheckRequestDto) {

        Member findMember = memberRepository.findByPhoneNum(emailCheckRequestDto.getPhoneNum()).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_MEMBER)
        );

        int findAuthenticationCode = Optional.ofNullable(smsCodeMap.get(findMember.getPhoneNum())).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_AUTHENTICATION_CODE)
        );

        if(findAuthenticationCode != emailCheckRequestDto.getAuthenticationCode()){
            throw new CustomException(ExceptionEnum.INVALID_AUTHENTICATION_CODE);
        }

        Map<String, String> findEmail = new HashMap<>();
        findEmail.put("email", findMember.getEmail());
        smsCodeMap.remove(findMember.getPhoneNum());

        return ResponseEntity.ok().body(findEmail);
    }

    // Signature 필드 값 생성을 위한 메서드
    private String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        // one space
        String space = " ";
        // new line
        String newLine = "\n";
        // method
        String method = "POST";
        // url (include query string)
        String url = "/sms/v2/services/"+ this.serviceId + "/messages";
        // current timestamp (epoch)
        String timestamp = time.toString();
        // access key id (from portal or Sub Account)
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        // secretKey HMAC 암호화 알고리즘 암호화
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);


        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // 메세지 발송 메서드
    private SmsResponseDto sendSms(SmsMessageDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {

        // NAVER SMS Service를 활용하기 위한 요청 Header
        Long time = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        // 인증코드 발송을 위한 인증코드 생성
        List<SmsMessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        SmsRequestDto request = SmsRequestDto.of("SMS","COMM","82", phone, messageDto.getContent(), messages);

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);

        if(Objects.isNull(response)){
            throw new CustomException(ExceptionEnum.SMS_SEND_ERR);
        }

        return response;
    }

    public void clearSmsCode(){
        log.info("before smsCodeMap size = {}", smsCodeMap.size());
        smsCodeMap.clear();
        log.info("Current Time : {} - Success smsCodeMap Clear ", LocalTime.now());
    }

}
