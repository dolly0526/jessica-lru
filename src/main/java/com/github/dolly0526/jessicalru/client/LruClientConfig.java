package com.github.dolly0526.jessicalru.client;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.client.RaftClientRpc;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.examples.counter.CounterCommon;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.ClientId;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author yusenyang
 * @create 2021/4/18 21:28
 */
@Slf4j
@Component
public class LruClientConfig implements DisposableBean {

    private RaftClient raftClient;


    @Bean
    public RaftClient raftClient() {

        // 当前客户端的配置
        RaftProperties properties = new RaftProperties();

        // 客户端通信方式
        RaftClientRpc rpc = new GrpcFactory(new Parameters())
                .newRaftClientRpc(ClientId.randomId(), properties);

        // 构建客户端
        raftClient = RaftClient.newBuilder()
                .setRaftGroup(CounterCommon.RAFT_GROUP)
                .setProperties(properties)
                .setClientRpc(rpc)
                .build();

        return raftClient;
    }

    @Override
    public void destroy() throws Exception {
        if (ObjectUtil.isNotNull(raftClient)) raftClient.close();
    }
}
