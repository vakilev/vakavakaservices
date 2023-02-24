package com.avdev.customer;

import com.avdev.amqp.RabbitMQMessageProducer;
import com.avdev.clients.fraud.FraudCheckResponse;
import com.avdev.clients.fraud.FraudClient;
import com.avdev.clients.notification.NotificationClient;
import com.avdev.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        var notificationRequest = new NotificationRequest
                (
                        customer.getId(),
                        customer.getPhoneNumber(),
                        String.format("Hello, %s! from avdev",
                                customer.getFirstName())
                );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
