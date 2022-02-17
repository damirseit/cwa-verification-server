package app.coronawarn.verification.service;

import app.coronawarn.verification.config.VerificationApplicationConfig;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link  CustomMetricService} handling of custom metric exposure to prometheus
 */
@Slf4j
@Component
public class CustomMetricService {

  /**
   * Constructor for the CustomMetricService.
   */
  public CustomMetricService(VerificationApplicationConfig applicationConfig) {
    //this.fakeDelayTest = applicationConfig.getInitialFakeDelayMilliseconds();
  }

  public void updateUserAgentMetric(String userAgent){
    log.info("parsing userAgent {}", userAgent);
  }
}
