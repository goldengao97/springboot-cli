package com.netty.server.controller;

import com.netty.server.handler.MessageHandler;
import com.netty.server.store.ChannelStore;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class HttpApi {

    @GetMapping("/send")
    public String send(String clientId, String message) {
        Channel channel = ChannelStore.getChannel(clientId);
        channel.writeAndFlush(message);
        return "发送成功";
    }
}
