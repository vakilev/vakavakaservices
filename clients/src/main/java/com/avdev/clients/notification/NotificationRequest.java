package com.avdev.clients.notification;

public record NotificationRequest (Integer toCustomerId,
                                   String toCustomerPhoneNumber,
                                   String message
){
}
