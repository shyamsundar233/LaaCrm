package com.laacrm.main.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "limit")
@Data
public class FeatureLimits {

    private Integer moduleLimit;

    private Integer fieldLimit;

}
