package com.shoprunner.micrometer;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import io.micrometer.cloudwatch.CloudWatchConfig;
import io.micrometer.cloudwatch.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.autoconfigure.context.ContextCredentialsAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.metrics.CloudWatchProperties;
import org.springframework.cloud.aws.context.annotation.ConditionalOnMissingAmazonClient;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ContextCredentialsAutoConfiguration.class})
@EnableConfigurationProperties({CloudWatchProperties.class})
@ConditionalOnProperty(
    prefix = "spring.metrics.export.cloudwatch",
    name = {"namespace"}
)
@ConditionalOnClass({CloudWatchMeterRegistry.class, RegionProvider.class})
@AutoConfigureBefore(SimpleMetricsExportAutoConfiguration.class)
public class CloudWatchExportAutoConfiguration {
    @Autowired(
        required = false
    )
    private RegionProvider regionProvider;

    public CloudWatchExportAutoConfiguration() {
    }

    @Bean
    @ConditionalOnProperty(
        value = {"spring.metrics.export.cloudwatch.enabled"},
        matchIfMissing = true
    )
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config, Clock clock, AmazonCloudWatchAsync client) {
        return new CloudWatchMeterRegistry(config, clock, client);
    }

    @Bean
    @ConditionalOnMissingAmazonClient(AmazonCloudWatchAsync.class)
    public AmazonWebserviceClientFactoryBean<AmazonCloudWatchAsyncClient> amazonCloudWatchAsync(AWSCredentialsProvider credentialsProvider) {
        return new AmazonWebserviceClientFactoryBean(AmazonCloudWatchAsyncClient.class, credentialsProvider, this.regionProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public CloudWatchConfig cloudWatchConfig(CloudWatchProperties cloudWatchProperties) {
        return new CloudWatchPropertiesConfigAdapter(cloudWatchProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public Clock micrometerClock() {
        return Clock.SYSTEM;
    }
}

