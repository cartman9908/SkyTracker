package com.skytracker.pricealert.service;

import com.skytracker.common.dto.alerts.FlightAlertEventMessageDto;
import com.skytracker.common.exception.EmailInvalidException;
import com.skytracker.common.exception.EmailSendFailedException;
import com.skytracker.pricealert.dto.SendMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailService {

    private final JavaMailSender mailSender;

    public void sendMail(FlightAlertEventMessageDto eventMessageDto) {
        SendMessageDto message = SendMessageDto.from(eventMessageDto);
        try {
            SimpleMailMessage smm = new SimpleMailMessage();

            if (message.getEmail() == null || !message.getEmail().contains("@")) {
                throw new EmailInvalidException(message.getEmail());
            }

            smm.setTo(message.getEmail());
            smm.setSubject(message.getSubject());
            smm.setText(message.getContent());

            mailSender.send(smm);
            log.info("이메일 {}로 알림 발송 완료", message.getEmail());
        } catch (Exception e) {
            throw new EmailSendFailedException(message.getEmail(), e);
        }
    }
}