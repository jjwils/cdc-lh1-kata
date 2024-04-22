package io.pact.workshop.product_catalogue.clients;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.PactSpecVersion; // required for v4.6.x to set pactVersion
import io.pact.workshop.product_catalogue.models.Customer;
import io.pact.workshop.product_catalogue.models.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "JobService")
class JobServiceClientPactTest {
  @Autowired
  private JobServiceClient jobServiceClient;


  @Pact(consumer = "SamirsApp")
  public RequestResponsePact getJob(PactDslWithProvider builder) {
    return builder
      .given("a job exists")
      .uponReceiving("get job")
        .path("/job")
      .willRespondWith()
        .status(200)
        .body(
          new PactDslJsonBody()
                        .object("customer", new PactDslJsonBody()
                                .stringType("firstName", "Prince")
                                .stringType("lastName", "Ali")
                                .stringType("phoneNumber", "07456978900")
                        )
                        .stringType("startLatitude", "53.35612531404332")
                        .stringType("startLongitude", "-2.277333661375856")
                        .stringType("endLatitude", "53.48064143725981")
                        .stringType("endLongitude", "-2.2423585050324775")
                        .booleanType("return", false)
        )
      .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "getJob", pactVersion = PactSpecVersion.V3)
  void testSingleProduct(MockServer mockServer) {
    jobServiceClient.setBaseUrl(mockServer.getUrl());
    Job job = jobServiceClient.getJob();
    assertThat(job, is(equalTo(new Job(
            new Customer("Prince", "Ali", "07456978900"),
            "53.35612531404332",
            "-2.277333661375856",
            "53.48064143725981",
            "-2.2423585050324775",
            false
            ))));
  }
}
