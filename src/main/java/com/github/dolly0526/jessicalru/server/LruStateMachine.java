package com.github.dolly0526.jessicalru.server;

import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.statemachine.impl.SimpleStateMachineStorage;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yusenyang
 * @create 2021/4/18 21:56
 */
@Component
public class LruStateMachine extends BaseStateMachine {

    // 持有一个LRU缓存
    private LruCache lruCache = new LruCache(100);

    // 读写锁控制并发问题
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    // 状态机的持久化存储
    private SimpleStateMachineStorage storage = new SimpleStateMachineStorage();


    @Override
    public CompletableFuture<Message> query(Message request) {
        return super.query(request);
    }

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        RaftProtos.LogEntryProto logEntry = trx.getLogEntry();


        return super.applyTransaction(trx);
    }
}
