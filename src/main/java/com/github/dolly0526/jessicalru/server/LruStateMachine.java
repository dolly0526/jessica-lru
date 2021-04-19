package com.github.dolly0526.jessicalru.server;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.github.dolly0526.jessicalru.common.LruRequest;
import com.github.dolly0526.jessicalru.common.LruRequest.LruOper;
import com.github.dolly0526.jessicalru.common.LruResponse;
import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.statemachine.impl.SimpleStateMachineStorage;
import org.apache.ratis.util.AutoCloseableLock;
import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yusenyang
 * @create 2021/4/18 21:56
 */
public class LruStateMachine extends BaseStateMachine {

    // 持有一个LRU缓存
    private LruCache<String, Object> cache = new LruCache<>(100);

    // 读写锁控制并发问题
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    // 状态机的持久化备份
    private SimpleStateMachineStorage storage = new SimpleStateMachineStorage();


    @Override
    public CompletableFuture<Message> query(Message request) {

        // 从message中获取请求的json，用utf8编码
        String reqJson = request.getContent().toStringUtf8();

        // 反序列化成对象
        String key = JSONUtil.toBean(reqJson, LruRequest.class).getKey();
        Object value;

        // 查询前先获取读锁，尽量减小锁住的范围
        try (AutoCloseableLock readLock = AutoCloseableLock.acquire(lock.readLock())) {
            value = cache.get(key);

        } catch (Throwable t) {
            t.printStackTrace();
            return CompletableFuture.completedFuture(Message.valueOf(
                    JSONUtil.toJsonStr(new LruResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            t.getMessage()
                    ))));
        }

        // 如果是空的，直接不返回就行
        if (ObjectUtil.isNotEmpty(value)) {
            return CompletableFuture.completedFuture(Message.valueOf(
                    JSONUtil.toJsonStr(new LruResponse(
                            HttpStatus.OK.value(),
                            HttpStatus.OK.getReasonPhrase(),
                            value.toString()
                    ))));
        } else {
            return CompletableFuture.completedFuture(Message.valueOf(
                    JSONUtil.toJsonStr(new LruResponse(
                            HttpStatus.NO_CONTENT.value(),
                            HttpStatus.NO_CONTENT.getReasonPhrase()
                    ))));
        }
    }

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {

        // 获取已提交的日志段
        RaftProtos.LogEntryProto entry = trx.getLogEntry();

        // 从日志段中获取请求的json，用utf8编码
        String reqJson = entry.getStateMachineLogEntry().getLogData().toStringUtf8();
        updateLastAppliedTermIndex(entry.getTerm(), entry.getIndex());

        // 反序列化成对象
        LruRequest lruReq = JSONUtil.toBean(reqJson, LruRequest.class);
        Object oldValue;

        // 查询前先获取写锁
        try (AutoCloseableLock writeLock = AutoCloseableLock.acquire(lock.writeLock())) {
            switch (LruOper.matchOper(lruReq.getOper())) {
                case CREATE: {
                    oldValue = cache.putIfAbsent(lruReq.getKey(), lruReq.getValue());
                    break;
                }
                case UPDATE: {
                    oldValue = cache.put(lruReq.getKey(), lruReq.getValue());
                    break;
                }
                case DELETE: {
                    oldValue = cache.remove(lruReq.getKey());
                    break;
                }
                default:
                    oldValue = null;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return CompletableFuture.completedFuture(Message.valueOf(
                    JSONUtil.toJsonStr(new LruResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            t.getMessage()
                    ))));
        }

        // 如果是空的，直接不返回就行
        if (ObjectUtil.isEmpty(oldValue)) {
            return CompletableFuture.completedFuture(Message.valueOf(
                    JSONUtil.toJsonStr(new LruResponse(
                            HttpStatus.NO_CONTENT.value(),
                            HttpStatus.NO_CONTENT.getReasonPhrase()
                    ))));
        } else {
            return CompletableFuture.completedFuture(Message.valueOf(
                    JSONUtil.toJsonStr(new LruResponse(
                            HttpStatus.OK.value(),
                            HttpStatus.OK.getReasonPhrase(),
                            oldValue.toString()
                    ))));
        }
    }
}
