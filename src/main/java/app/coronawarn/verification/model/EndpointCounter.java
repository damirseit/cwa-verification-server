package app.coronawarn.verification.model;

import io.micrometer.core.instrument.Counter;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointCounter {

  public String name = null;
  public final Map<String, Counter> deviceCounters = new HashMap<>();
  public final Map<String, Counter> agentCounters = new HashMap<>();
}