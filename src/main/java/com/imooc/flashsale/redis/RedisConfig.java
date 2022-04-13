package com.imooc.flashsale.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@EnableAutoConfiguration(exclude = {JmxAutoConfiguration.class})
@Getter
@Setter
public class RedisConfig {
    private String host;
    private int port;
    private int timeout; // 秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait; // 秒
}
