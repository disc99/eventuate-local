package io.eventuate.local.java.kafka.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("eventuate.local.kafka.consumer")
public class EventuateKafkaConsumerConfigurationProperties {
  Map<String, String> properties = new HashMap<>();

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public static EventuateKafkaConsumerConfigurationProperties empty() {
    return new EventuateKafkaConsumerConfigurationProperties();
  }
}
