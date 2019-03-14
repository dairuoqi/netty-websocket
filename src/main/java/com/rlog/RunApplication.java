package com.rlog;

import com.rlog.server.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * RunApplication
 * 程序入口
 *
 * @author rock
 * @date 2019/03/12
 */
@SpringBootApplication
public class RunApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(RunApplication.class, args);
        TcpServer tcpServer = context.getBean(TcpServer.class);
        tcpServer.start();
    }

}
