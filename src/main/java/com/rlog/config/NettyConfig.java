package com.rlog.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * NettyConfig
 *
 * @author rock
 * @date 2019/03/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

    private int port;

    private int bossThread;

    private int workerThread;

    private boolean keepAlive;

    private int backlog;

}
