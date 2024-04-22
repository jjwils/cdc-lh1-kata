package io.pact.workshop.product_catalogue.models;

import lombok.Data;

@Data
public class job {
    private Customer customer;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private boolean returnFlag;
}
