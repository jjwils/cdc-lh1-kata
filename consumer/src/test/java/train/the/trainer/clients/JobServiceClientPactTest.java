package train.the.trainer.clients;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.PactSpecVersion; // required for v4.6.x to set pactVersion
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import train.the.trainer.samirs_taxi.models.Customer;
import train.the.trainer.samirs_taxi.models.Job;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
            .status(200)//CHANGE THIS TO THE INT FOR HTTP.OK
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
  void should_get_job(MockServer mockServer) {
    jobServiceClient.setBaseUrl(mockServer.getUrl());
    Job job = jobServiceClient.getJob();
    assertThat(job, is(equalTo(new Job(
            new Customer("TODO", "TODO", "TODO"),
            "TODO",
            "TODO",
            "TODO",
            "TODO",
            false
    ))));
  }

}
