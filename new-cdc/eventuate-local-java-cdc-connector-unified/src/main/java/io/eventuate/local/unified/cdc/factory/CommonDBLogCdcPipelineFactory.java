package io.eventuate.local.unified.cdc.factory;

import io.eventuate.local.common.BinLogEvent;
import io.eventuate.local.common.CdcDataPublisher;
import io.eventuate.local.common.PublishingStrategy;
import io.eventuate.local.db.log.common.*;
import io.eventuate.local.java.common.broker.DataProducerFactory;
import io.eventuate.local.java.kafka.EventuateKafkaConfigurationProperties;
import io.eventuate.local.java.kafka.consumer.EventuateKafkaConsumerConfigurationProperties;
import io.eventuate.local.java.kafka.producer.EventuateKafkaProducer;
import io.eventuate.local.unified.cdc.properties.CommonDbLogCdcPipelineProperties;
import org.apache.curator.framework.CuratorFramework;

public abstract class CommonDBLogCdcPipelineFactory<PROPERTIES extends CommonDbLogCdcPipelineProperties, EVENT extends BinLogEvent>
        extends CommonCdcPipelineFactory<PROPERTIES, EVENT> {

  protected EventuateKafkaConfigurationProperties eventuateKafkaConfigurationProperties;
  protected EventuateKafkaConsumerConfigurationProperties eventuateKafkaConsumerConfigurationProperties;
  protected EventuateKafkaProducer eventuateKafkaProducer;
  protected PublishingFilter publishingFilter;

  public CommonDBLogCdcPipelineFactory(CuratorFramework curatorFramework,
                                       PublishingStrategy<EVENT> publishingStrategy,
                                       DataProducerFactory dataProducerFactory,
                                       EventuateKafkaConfigurationProperties eventuateKafkaConfigurationProperties,
                                       EventuateKafkaConsumerConfigurationProperties eventuateKafkaConsumerConfigurationProperties,
                                       EventuateKafkaProducer eventuateKafkaProducer,
                                       PublishingFilter publishingFilter) {

    super(curatorFramework, publishingStrategy, dataProducerFactory);
    this.eventuateKafkaConfigurationProperties = eventuateKafkaConfigurationProperties;
    this.eventuateKafkaConsumerConfigurationProperties = eventuateKafkaConsumerConfigurationProperties;
    this.eventuateKafkaProducer = eventuateKafkaProducer;
    this.publishingFilter = publishingFilter;
  }

  protected abstract OffsetStore createOffsetStore(PROPERTIES properties);

  protected CdcDataPublisher<EVENT> createCdcDataPublisher(OffsetStore offsetStore) {

    return new DbLogBasedCdcDataPublisher<>(dataProducerFactory,
            offsetStore,
            publishingFilter,
            publishingStrategy);
  }
}
