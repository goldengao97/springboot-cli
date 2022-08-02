package com.netty.server.handler;

import com.netty.server.store.ChannelStore;
import com.netty.server.utils.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * 消息处理,单例启动
 *
 * @author qiding
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class MessageHandler extends SimpleChannelInboundHandler<String> {
    //int readIdleTimes = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        log.debug("\n");
        log.debug("channelId:" + ctx.channel().id());
        log.debug("收到消息:{}", message);
        //TODO redis存客户端消息  根据message内容判断
        if(message.contains("")) {
            RedisUtils.save("", "");
        }
        // 回复客户端
        //ctx.writeAndFlush("ok");
    }

    /**
     * 指定客户端发送
     *
     * @param clientId 其它已成功登录的客户端
     * @param message  消息
     */

    public void sendByClientId(String clientId, String message) {
        Channel channel = ChannelStore.getChannel(clientId);
        channel.writeAndFlush(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("\n");
        log.debug("断开连接");
        GatewayService.removeGatewayChannelByClientId(ChannelStore.getClientId(ctx));
        ChannelStore.closeAndClean(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("\n");
        log.debug("成功建立连接,channelId：{}", ctx.channel().id());
        // 判断是否未登录
        if (!ChannelStore.isAuth(ctx)) {
            // 登录逻辑实现
            InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
            String clientId = String.valueOf(ipSocket.getAddress()).replace("/","") + ":" +ipSocket.getPort();
            ChannelStore.bind(ctx, clientId);
            GatewayService.addGatewayChannel(clientId,ctx.channel());
            log.debug("登录成功");
            //ctx.writeAndFlush("login successfully");
            return;
        }
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.debug("心跳事件时触发");
        /*IdleStateEvent event = (IdleStateEvent) evt;

        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes++; // 读空闲的计数加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                // 不处理
                break;
        }

        System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (readIdleTimes > 3) {
            System.out.println(" [server]读空闲超过3次，关闭连接，释放更多资源");
            ctx.channel().writeAndFlush("idle close");
            ctx.channel().close();
        }*/
    }
}
