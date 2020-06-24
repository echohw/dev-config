package com.example.devconfig.properties;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by AMe on 2020-06-22 13:08.
 */
@Data
@Component
@ConfigurationProperties(prefix = "access")
public class AccessLimitProperties {

    private Map<String, Properties> limits = new LinkedHashMap<>();

    @Data
    public static class Properties {

        private String path;
        private Integer timeUnit;
        private Integer limitInUnit;
        private String hint = "您的请求过于频繁, 请${ttl}s后再试";
    }

}
