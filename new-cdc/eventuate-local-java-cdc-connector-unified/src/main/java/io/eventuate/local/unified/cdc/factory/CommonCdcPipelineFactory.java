package io.eventuate.local.unified.cdc.factory;

import com.zaxxer.hikari.HikariDataSource;
import io.eventuate.javaclient.spring.jdbc.EventuateSchema;
import io.eventuate.local.common.*;
import io.eventuate.local.java.common.broker.DataProducerFactory;
import io.eventuate.local.unified.cdc.properties.CdcPipelineProperties;
import org.apache.curator.framework.CuratorFramework;

import javax.sql.DataSource;

abstract public class CommonCdcPipelineFactory<PROPERTIES extends CdcPipelineProperties, EVENT extends BinLogEvent>
        implements CdcPipelineFactory<PROPERTIES, EVENT> {

  protected CuratorFramework curatorFramework;
  protected PublishingStrategy<EVENT> publishingStrategy;
  protected DataProducerFactory dataProducerFactory;

  public CommonCdcPipelineFactory(CuratorFramework curatorFramework, PublishingStrategy<EVENT> publishingStrategy, DataProducerFactory dataProducerFactory) {
    this.curatorFramework = curatorFramework;
    this.publishingStrategy = publishingStrategy;
    this.dataProducerFactory = dataProducerFactory;
  }

  protected EventTableChangesToAggregateTopicTranslator<EVENT> createEventTableChangesToAggregateTopicTranslator(PROPERTIES properties,
                                                                                                                 CdcDataPublisher<EVENT> cdcDataPublisher,
                                                                                                                 CdcProcessor<EVENT> cdcProcessor) {


    return new EventTableChangesToAggregateTopicTranslator<>(cdcDataPublisher,
            cdcProcessor,
            curatorFramework,
            properties.getLeadershipLockPath());
  }

  protected EventuateSchema createEventuateSchema(PROPERTIES properties) {
    return new EventuateSchema(properties.getEventuateDatabaseSchema());
  }

  protected DataSource createDataSource(PROPERTIES properties) {


    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setUsername(properties.getDataSourceUserName());
    hikariDataSource.setPassword(properties.getDataSourcePassword());
    hikariDataSource.setJdbcUrl(properties.getDataSourceUrl());
    hikariDataSource.setDriverClassName(properties.getDataSourceDriverClassName());


    hikariDataSource.setConnectionTestQuery("select 1");

    return hikariDataSource;
  }
}
