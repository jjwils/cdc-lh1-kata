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

import static org.hamcrest.MatcherAssert.assertThat;
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
            .path("TODO")
            .willRespondWith()
            .status(-1)//CHANGE THIS TO THE INT FOR HTTP.OK
            .body(
                    new PactDslJsonBody()
                            .object("TODO", new PactDslJsonBody()
                                    .stringType("TODO", "TODO")
                                    .stringType("TODO", "TODO")
                                    .stringType("TODO", "TODO")
                            )
                            .stringType("TODO", "TODO")
                            .stringType("TODO", "TODO")
                            .stringType("TODO", "TODO")
                            .stringType("TODO", "TODO")
                            .booleanType("TODO", false)
            )
            .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "getJob", pactVersion = PactSpecVersion.V3)
  void should_get_job(MockServer mockServer) {
    //TODO Step 2
  }

}
