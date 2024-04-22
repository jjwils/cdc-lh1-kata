package io.pact.workshop.product_catalogue.models;

import lombok.Data;

@Data
public class Customer {
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
}
