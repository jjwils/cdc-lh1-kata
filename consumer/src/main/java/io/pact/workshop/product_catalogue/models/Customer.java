package io.pact.workshop.product_catalogue.models;

import lombok.Data;

@Data
public class Customer {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
