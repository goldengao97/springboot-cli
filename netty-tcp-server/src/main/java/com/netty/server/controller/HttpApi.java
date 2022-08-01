package com.netty.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.netty.server.handler.GatewayService;
import com.netty.server.store.ChannelStore;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class HttpApi {

    @GetMapping("/send")
    public String send(String clientId, String message) {
        Channel channel = ChannelStore.getChannel(clientId);
        if(channel != null) {
            channel.writeAndFlush(message);
            return "发送成功";
        }
        return "请检查客户端是否在线";
    }

    @PostMapping("/send/json")
    public String send(@RequestBody JSONObject body) {
        String clientId = body.getString("clientId");
        String message = body.getString("message");
        Channel channel = ChannelStore.getChannel(clientId);
        if(channel != null) {
            channel.writeAndFlush(message);
            return "发送成功";
        }
        return "请检查客户端是否在线";
    }

    @GetMapping("/getAllChannelInfo")
    public JSONObject getAllChannelInfo() {
        return new JSONObject(GatewayService.getAllChannels());
    }
}
