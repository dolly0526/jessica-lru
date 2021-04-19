package com.github.dolly0526.jessicalru;

import cn.hutool.json.JSONUtil;
import com.github.dolly0526.jessicalru.common.LruRequest;
import lombok.SneakyThrows;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JessicaLruApplicationTests {

    @Autowired
    private RaftClient raftClient;


    @SneakyThrows
    @Test
    public void testClient() {

        LruRequest req1 = new LruRequest(LruRequest.LruOper.CREATE.getOper(), "test", "dolly");
        LruRequest req2 = new LruRequest(LruRequest.LruOper.RETRIEVE.getOper(), "test");

        RaftClientReply reply1 = raftClient.send(Message.valueOf(JSONUtil.toJsonStr(req1)));
        System.out.println(reply1);

        RaftClientReply reply2 = raftClient.send(Message.valueOf(JSONUtil.toJsonStr(req2)));
        System.out.println(reply2);
    }
}
