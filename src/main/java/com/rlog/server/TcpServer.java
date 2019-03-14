package com.rlog.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * TCPServer
 *
 * @author rock
 * @date 2018/01/10
 */
@Data
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TcpServer {

    private final ServerBootstrap serverBootstrap;

    private final InetSocketAddress tcpSocketAddress;

    private Channel serverChannel;

    public void start() throws Exception {
        serverChannel = serverBootstrap.bind(tcpSocketAddress).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannel.close();
        serverChannel.parent().close();
    }
}
