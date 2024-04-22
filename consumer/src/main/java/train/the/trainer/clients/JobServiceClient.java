package train.the.trainer.clients;

import train.the.trainer.samirs_taxi.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JobServiceClient {
  @Autowired
  private RestTemplate restTemplate;

  @Value("${serviceClients.products.baseUrl}")
  private String baseUrl;


  public Job getJob() {
    return restTemplate.getForObject(baseUrl + "/job", Job.class);
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
