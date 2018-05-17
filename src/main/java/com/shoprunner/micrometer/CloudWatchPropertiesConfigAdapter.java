//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
package com.shoprunner.micrometer;

import io.micrometer.cloudwatch.CloudWatchConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;
import org.springframework.cloud.aws.autoconfigure.metrics.CloudWatchProperties;

class CloudWatchPropertiesConfigAdapter extends StepRegistryPropertiesConfigAdapter<CloudWatchProperties> implements CloudWatchConfig {
    CloudWatchPropertiesConfigAdapter(CloudWatchProperties properties) {
        super(properties);
    }

    public String namespace() {
        return (String)this.get(CloudWatchProperties::getNamespace, CloudWatchConfig.super::namespace);
    }

    public int batchSize() {
        return (Integer)this.get(StepRegistryProperties::getBatchSize, () -> {
            return super.batchSize();
        });
    }
}

