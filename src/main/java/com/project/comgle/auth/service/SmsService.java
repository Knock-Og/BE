package com.project.comgle.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.comgle.auth.dto.*;
import com.project.comgle.member.entity.Member;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    @Transactional(readOnly = true)
    public ResponseEntity<FindEmailResponseDto> sendSmsCode(SendSmsRequestDto emailCheckRequestDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        Member findMember = memberRepository.findByMemberNameStartingWithAndPhoneNum(emailCheckRequestDto.getMemberName(),
                emailCheckRequestDto.getPhoneNum()).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_MEMBER)
        );

        int authenticationCode = new Random().nextInt(900000)+100000;

        FindEmailResponseDto findEmailResponseDto = sendSms(SmsMessageDto.of(findMember.getPhoneNum().replaceAll("-", ""), "[knock 본인인증] " + authenticationCode));
        findEmailResponseDto.setPhoneNum(findMember.getPhoneNum());
        findEmailResponseDto.setMessage("The sms has been sent successfully.");

        smsCodeMap.put(findMember.getPhoneNum(), authenticationCode);

        return ResponseEntity.accepted()
                .body(findEmailResponseDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<EmailResponseDto> findEmail(FindEmailRequestDto emailCheckRequestDto) {

        Member findMember = memberRepository.findByPhoneNum(emailCheckRequestDto.getPhoneNum()).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_MEMBER)
        );

        int findAuthenticationCode = Optional.ofNullable(smsCodeMap.get(findMember.getPhoneNum())).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_AUTHENTICATION_CODE)
        );

        if(findAuthenticationCode != emailCheckRequestDto.getAuthenticationCode()){
            throw new CustomException(ExceptionEnum.INVALID_AUTHENTICATION_CODE);
        }

        smsCodeMap.remove(findMember.getPhoneNum());

        return ResponseEntity.ok().body(new EmailResponseDto(findMember.getEmail()));
    }

    public void clearSmsCode(){
        log.info("before smsCodeMap size = {}", smsCodeMap.size());
        smsCodeMap.clear();
        log.info("Current Time : {} - Success smsCodeMap Clear ", LocalTime.now());
    }

    private String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId + "/messages";

        String timestamp = time.toString();

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

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);


        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    private FindEmailResponseDto sendSms(SmsMessageDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {

        Long time = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        List<SmsMessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        SmsRequestDto request = SmsRequestDto.of("SMS","COMM","82", phone, messageDto.getContent(), messages);

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        FindEmailResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, FindEmailResponseDto.class);

        if(Objects.isNull(response)){
            throw new CustomException(ExceptionEnum.SEND_SMS_CODE_ERR);
        }

        return response;
    }

}
