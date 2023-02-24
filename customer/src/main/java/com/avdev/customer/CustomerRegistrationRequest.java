package com.avdev.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String phoneNumber) {
}
