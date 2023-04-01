package com.project.comgle.auth.scheduler;

import com.project.comgle.auth.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SmsService smsService;

    // 초(0-59) / 분(0-59) / 시(0-23) / 일(1-31) / 월(1-12) / 요일(0-6 or SUN-SAT)
    // 요일 - 0, 7 : 일요일
    @Scheduled(cron = "0 0 0 * * 1-5")
    public void clearSmsCode() throws InterruptedException {
        log.info("Current Time : {} SMS 인증 관리 Map 초기화 실행 " , LocalDateTime.now());
        smsService.clearSmsCode();
    }

}

