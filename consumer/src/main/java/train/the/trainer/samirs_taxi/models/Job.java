package train.the.trainer.samirs_taxi.models;

import lombok.Data;

@Data
public class Job {
    private final Customer customer;
    private final String startLatitude;
    private final String startLongitude;
    private final String endLatitude;
    private final String endLongitude;
    private final boolean returnFlag;
}
