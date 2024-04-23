package train.the.trainer.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import train.the.trainer.samirs_taxi.models.Customer;
import train.the.trainer.samirs_taxi.models.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockResolver.Wiremock;
import ru.lanwen.wiremock.ext.WiremockUriResolver;
import ru.lanwen.wiremock.ext.WiremockUriResolver.WiremockUri;
import train.the.trainer.samirs_taxi.clients.JobServiceClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ExtendWith({ WiremockResolver.class, WiremockUriResolver.class })
class JobServiceClientTest {
  @Autowired
  private JobServiceClient jobServiceClient;


  @Test
  void getJob(@Wiremock WireMockServer server, @WiremockUri String uri) {
    jobServiceClient.setBaseUrl(uri);
    server.stubFor(
            get(urlPathEqualTo("/job"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody("{\n" +
                                    "  \"customer\": {\n" +
                                    "    \"firstName\": \"Prince\",\n" +
                                    "    \"lastName\": \"Ali\",\n" +
                                    "    \"phoneNumber\": \"07456978900\"\n" +
                                    "  },\n" +
                                    "  \"startLatitude\": \"53.35612531404332\",\n" +
                                    "  \"startLongitude\": \"-2.277333661375856\",\n" +
                                    "  \"endLatitude\": \"53.48064143725981\",\n" +
                                    "  \"endLongitude\": \"-2.2423585050324775\",\n" +
                                    "  \"return\": false\n" +
                                    "}")
                            .withHeader("Content-Type", "application/json")
                    )
    );

    Job job = jobServiceClient.getJob();

    assertThat(job, is(equalTo(new Job(
            new Customer("Prince", "Ali", "07456978900"),
            "53.35612531404332",
            "-2.277333661375856",
            "53.48064143725981",
            "-2.2423585050324775",
            false))));

  }
}
