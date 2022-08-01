package com.netty.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.netty.server.handler.GatewayService;
import com.netty.server.store.ChannelStore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
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
        return getString(clientId, message);
    }

    @PostMapping("/send/json")
    public String send(@RequestBody JSONObject body) {
        String clientId = body.getString("clientId");
        String message = body.getString("message");
        return getString(clientId, message);
    }

    private String getString(String clientId, String message) {
        try {
            Channel channel = ChannelStore.getChannel(clientId);
            ByteBuf buff = Unpooled.buffer();
            if (channel != null) {
                buff.writeBytes(hexStrToBinaryStr(message));
                channel.writeAndFlush(buff);
                return "发送成功";
            }
            return "请检查客户端是否在线";
        }catch (Exception e) {
            e.printStackTrace();
            return "发送客户端失败错误信息："+e.getMessage();
        }
    }
    @GetMapping("/getAllChannelInfo")
    public JSONObject getAllChannelInfo() {
        return new JSONObject(GatewayService.getAllChannels());
    }

    /**
     * 将十六进制的字符串转换成字节数组
     * @param hexString
     * @return
     */
    public static byte[] hexStrToBinaryStr(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];
        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index/2] = (byte)Integer.parseInt(sub,16);
            index += 2;
        }
        return bytes;
    }
}
