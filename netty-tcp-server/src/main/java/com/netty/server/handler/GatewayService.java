package com.netty.server.handler;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GatewayService {
    private static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
    public static void addGatewayChannel(String deviceId, Channel gateway_channel){
        map.put(deviceId, gateway_channel);
    }

    public static Map<String, Channel> getAllChannels(){
        return map;
    }

    public static Channel getGatewayChannelByClientId(String clientId){
        return map.get(clientId);
    }

    public static void removeGatewayChannelByClientId(String clientId) {
        map.remove(clientId);
    }
}
