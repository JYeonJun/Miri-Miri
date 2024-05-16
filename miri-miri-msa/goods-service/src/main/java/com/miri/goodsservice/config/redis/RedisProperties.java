package com.miri.goodsservice.config.redis;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private Master master;
    private List<Slave> slaves;

    @Data
    public static class Master {
        private String host;
        private Integer port;
    }

    @Data
    public static class Slave {
        private String host;
        private Integer port;
    }
}
