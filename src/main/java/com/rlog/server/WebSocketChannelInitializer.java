package com.rlog.server;

import com.rlog.constant.BaseConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * WebSocketChannelInitializer
 * ChannelPipeline Init
 *
 * @author rock
 * @date 2019/03/12
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new OutBoundExceptionHandler());
        pipeline.addLast(new HttpRequestHandler(BaseConstant.WSURI));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(BaseConstant.WSURI));
        pipeline.addLast(textWebSocketFrameHandler);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }


}
