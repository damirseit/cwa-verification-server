package app.coronawarn.verification.service;

import app.coronawarn.verification.config.VerificationApplicationConfig;
import app.coronawarn.verification.model.EndpointCounter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link  CustomMetricService} handling of custom metric exposure to prometheus.
 */
@Slf4j
@Component
public class CustomMetricService {

  private final UserAgentAnalyzer uaa;

  private final MeterRegistry meterRegistry;

  private final Map<String, EndpointCounter> apiEndpointCounters;

  /**
   * Constructor for the CustomMetricService.
   */
  public CustomMetricService(VerificationApplicationConfig applicationConfig, MeterRegistry meterRegistry) {
    uaa = UserAgentAnalyzer
      .newBuilder()
      .hideMatcherLoadStats()
      .withCache(10000)
      .build();
    this.meterRegistry = meterRegistry;
    apiEndpointCounters = new HashMap<>();
  }

  private void createOrUpdateDeviceCounter(EndpointCounter endpointCounter, String deviceClass, String deviceName, String deviceVersion){
    if (endpointCounter.deviceCounters.containsKey(deviceName)){
      endpointCounter.deviceCounters.getOrDefault(deviceName,null).increment();
    } else {
      List<Tag> tags = new ArrayList<>();
      tags.add(Tag.of("endpoint", endpointCounter.name));
      tags.add(Tag.of("deviceClass", deviceClass));
      tags.add(Tag.of("deviceName", deviceName));
      tags.add(Tag.of("deviceVersion", deviceVersion));
      Counter counter = this.meterRegistry.counter("api.device.class", tags);
      counter.increment();
      endpointCounter.deviceCounters.put(deviceName,counter);
    }
  }

  private void createOrUpdateAgentCounter(EndpointCounter endpointCounter, String agentClass, String agentName, String agentVersion){
    if (endpointCounter.agentCounters.containsKey(agentName)){
      endpointCounter.agentCounters.getOrDefault(agentName,null).increment();
    } else {
      List<Tag> tags = new ArrayList<>();
      tags.add(Tag.of("endpoint", endpointCounter.name));
      tags.add(Tag.of("agentClass", agentClass));
      tags.add(Tag.of("agentName", agentName));
      tags.add(Tag.of("agentVersion", agentVersion));
      Counter counter = this.meterRegistry.counter("api.agent.class", tags);
      counter.increment();
      endpointCounter.agentCounters.put(agentName,counter);
    }
  }

  public void updateUserAgentMetric(String endpoint, String userAgent) {
    UserAgent ua = uaa.parse(userAgent);
    String deviceClass = ua.get(UserAgent.DEVICE_CLASS).getValue();
    String deviceName = ua.get(UserAgent.DEVICE_NAME).getValue();
    String deviceVersion = ua.get(UserAgent.DEVICE_VERSION).getValue();
    String agentClass = ua.get(UserAgent.AGENT_CLASS).getValue();
    String agentName = ua.get(UserAgent.AGENT_NAME).getValue();
    String agentVersion = ua.get(UserAgent.AGENT_VERSION).getValue();

    EndpointCounter endpointCounter = apiEndpointCounters.getOrDefault(endpoint,null);
    if(endpointCounter == null) {
      endpointCounter = new EndpointCounter(endpoint);
      apiEndpointCounters.put(endpoint, endpointCounter);
    }
    createOrUpdateDeviceCounter(endpointCounter, deviceClass, deviceName, deviceVersion);
    createOrUpdateAgentCounter(endpointCounter, agentClass, agentName, agentVersion);
  }
}
