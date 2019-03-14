package com.rlog.work;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * LogThread
 * 读取推送日志线程
 *
 * @author rock
 * @date 2019/03/13
 */
@Slf4j
public class LogThread extends Thread {

    private BufferedReader reader;
    private ChannelHandlerContext ctx;

    public LogThread(InputStream in, ChannelHandlerContext ctx) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.ctx = ctx;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                log.info(line);
                ctx.writeAndFlush(new TextWebSocketFrame(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
