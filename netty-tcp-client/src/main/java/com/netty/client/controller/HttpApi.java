package com.netty.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.netty.client.server.TcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟发送api
 *
 * @author qiding
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class HttpApi {

    private final TcpClient tcpClient;

    /**
     * 消息发布
     */
    @GetMapping("/send")
    public String send(String message) {
        tcpClient.getSocketChannel().writeAndFlush(message);
        return "发送成功";
    }

    @GetMapping("/sendHex")
    public String sendHex(String message) {
        try {
            ByteBuf buff = Unpooled.buffer();
            buff.writeBytes(hexStrToBinaryStr(message));
            tcpClient.getSocketChannel().writeAndFlush(buff);
            return "发送成功";
        }catch (Exception e) {
            e.printStackTrace();
            return "发送失败错误信息："+e.getMessage();
        }
    }

    /**
     * 消息发布
     */
    @PostMapping("/send/json")
    public String send(@RequestBody JSONObject body) {
        tcpClient.getSocketChannel().writeAndFlush(body.toJSONString());
        return "发送成功";
    }

    /**
     * 连接
     */
    @GetMapping("connect")
    public String connect(String ip, Integer port) throws Exception {
        tcpClient.connect(ip, port);
        return "重启指令发送成功";
    }

    /**
     * 重连
     */
    @GetMapping("reconnect")
    public String reconnect() throws Exception {
        tcpClient.reconnect();
        return "重启指令发送成功";
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
