package com.rlog.server;


import com.rlog.work.LogThread;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;


/**
 * TextWebSocketFrameHandler
 * text 消息传输处理
 *
 * @author rock
 * @date 2018/01/10
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ctx.writeAndFlush(msg);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("Event====> {} ", evt);
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            Process process = Runtime.getRuntime().exec("tail -f /Users/rock/Desktop/demo/target/demolog");
            InputStream inputStream = process.getInputStream();
            LogThread thread = new LogThread(inputStream, ctx);
            thread.start();
            ctx.pipeline().remove(HttpRequestHandler.class);
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        offline(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        offline(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("=====> {}", cause.getMessage());
        offline(ctx);
    }

    private void offline(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        group.remove(channel);
        ctx.close();
    }

}


