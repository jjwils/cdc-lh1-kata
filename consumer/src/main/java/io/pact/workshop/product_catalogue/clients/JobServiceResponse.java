package io.pact.workshop.product_catalogue.clients;

import io.pact.workshop.product_catalogue.models.Job;
import lombok.Data;

import java.util.List;

@Data
public class JobServiceResponse {
  private Job job;
}
