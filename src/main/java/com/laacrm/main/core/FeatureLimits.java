package com.laacrm.main.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "limit")
public class FeatureLimits {

    public Integer MODULE_LIMIT;

    public Integer FIELD_LIMIT;

}
