package com.netty.server.decoder;

import com.netty.server.utils.Hex;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {  //自定义解码器
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        if (in.readableBytes() >= 4) {  //判断读取数据是否可读
            // 待处理的消息包
            byte[] bytesReady = new byte[in.readableBytes()];
            in.readBytes(bytesReady);
            // 将字节数据转为字符串并去除首尾换行和空格
            // 此处的转换后的字符串类型是下一个Handler接收的消息类型
            out.add(Hex.byte2HexStr(bytesReady));
        }
    }
}