package com.avdev.notification;

import com.avdev.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void sendNotification(NotificationRequest notificationRequest) {
        notificationRepository.save(
                Notification.builder()
                        .toCustomerId(notificationRequest.toCustomerId())
                        .toCustomerPhoneNumber(notificationRequest.toCustomerPhoneNumber())
                        .message(notificationRequest.message())
                        .sender("avdev")
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}
