package com.github.dolly0526.jessicalru.server;

import cn.hutool.core.util.ObjectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.examples.counter.CounterCommon;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.util.NetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;

/**
 * @author yusenyang
 * @create 2021/4/18 21:28
 */
@Slf4j
@Component
public class LruServerRunner implements SmartLifecycle {

    @Value("${ratis.server.index}")
    private Integer serverIndex;

    private RaftServer raftServer;


    @SneakyThrows
    @Override
    public void start() {

        // 确定raft集群的节点信息
        RaftPeer raftPeer = CounterCommon.PEERS.get(serverIndex - 1);

        // 当前节点的配置
        RaftProperties properties = new RaftProperties();

        // 设置raft日志的存储位置
        File raftStorageDir = new File("storage/" + raftPeer.getId().toString());
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(raftStorageDir));

        // 设置通信的端口，貌似只有grpc的包是能用的
        int port = NetUtils.createSocketAddr(raftPeer.getAddress()).getPort();
        GrpcConfigKeys.Server.setPort(properties, port);

        // 启动当前节点
        raftServer = RaftServer.newBuilder()
                .setGroup(CounterCommon.RAFT_GROUP)
                .setProperties(properties)
                .setServerId(raftPeer.getId())
                .setStateMachine(new LruStateMachine())
                .build();

        raftServer.start();
        log.info("RaftServer启动 {}", raftPeer.toString());
    }

    @SneakyThrows
    @Override
    public void stop() {
        raftServer.close();
    }

    @Override
    public boolean isRunning() {
        return ObjectUtil.isNotNull(raftServer);
    }
}
