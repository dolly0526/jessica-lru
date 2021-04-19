package com.github.dolly0526.jessicalru.client;

import cn.hutool.json.JSONUtil;
import com.github.dolly0526.jessicalru.common.LruRequest;
import com.github.dolly0526.jessicalru.common.LruResponse;
import lombok.SneakyThrows;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yusenyang
 * @create 2021/4/18 21:27
 */
@RestController
@RequestMapping("/api")
public class LruClientController {

    @Autowired
    private RaftClient raftClient;


    @SneakyThrows
    @PostMapping("/lru")
    public LruResponse lruCreate(@RequestBody Map<String, Object> params) {

        RaftClientReply reply = raftClient.send(Message.valueOf(
                JSONUtil.toJsonStr(new LruRequest(
                        LruRequest.LruOper.CREATE.getOper(),
                        params.get("key").toString(),
                        params.get("value").toString()
                ))));

        return JSONUtil.toBean(reply.getMessage().getContent().toStringUtf8(), LruResponse.class);
    }

    @SneakyThrows
    @GetMapping("/lru")
    public LruResponse lruRetrieve(@RequestBody Map<String, Object> params) {

        RaftClientReply reply = raftClient.sendReadOnly(Message.valueOf(
                JSONUtil.toJsonStr(new LruRequest(
                        LruRequest.LruOper.RETRIEVE.getOper(),
                        params.get("key").toString()
                ))));

        return JSONUtil.toBean(reply.getMessage().getContent().toStringUtf8(), LruResponse.class);
    }

    @SneakyThrows
    @PutMapping("/lru")
    public LruResponse lruUpdate(@RequestBody Map<String, Object> params) {

        RaftClientReply reply = raftClient.send(Message.valueOf(
                JSONUtil.toJsonStr(new LruRequest(
                        LruRequest.LruOper.UPDATE.getOper(),
                        params.get("key").toString(),
                        params.get("value").toString()
                ))));

        return JSONUtil.toBean(reply.getMessage().getContent().toStringUtf8(), LruResponse.class);
    }

    @SneakyThrows
    @DeleteMapping("/lru")
    public LruResponse lruDelete(@RequestBody Map<String, Object> params) {

        RaftClientReply reply = raftClient.send(Message.valueOf(
                JSONUtil.toJsonStr(new LruRequest(
                        LruRequest.LruOper.DELETE.getOper(),
                        params.get("key").toString()
                ))));

        return JSONUtil.toBean(reply.getMessage().getContent().toStringUtf8(), LruResponse.class);
    }
}
