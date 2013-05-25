/*
 * Copyright 2012 The Netty Project
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.xx_dev.apn.proxy.ssltest.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

import org.apache.log4j.Logger;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class SSLServerHandler extends ChannelInboundByteHandlerAdapter {

    private static final Logger logger = Logger.getLogger(SSLServerHandler.class.getName());

    @Override
    public void inboundBufferUpdated(final ChannelHandlerContext ctx, ByteBuf in) {
        ByteBuf out = ctx.nextOutboundByteBuffer();

        logger.info(in.toString());

        // produce a lot of bytes
        for (int i = 0; i < 1024 * 1024; i++) {
            out.writeByte(1);
        }

        logger.info("begin flush");
        ctx.flush().addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("end flush");
                ctx.close();
                logger.info("closed");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
