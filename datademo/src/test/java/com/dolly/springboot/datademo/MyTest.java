package com.dolly.springboot.datademo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author yusenyang
 * @create 2020/11/10 15:14
 */
public class MyTest {

    @Test
    public void testJedis() throws JSONException {
        Jedis jedis = new Jedis("10.9.34.98", 6379);
        jedis.flushDB();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "dolly");
        String str = jsonObject.toString();

        // 开启事务
        Transaction multi = jedis.multi();

        try {
            multi.set("user1", str);
            multi.set("user2", str);
//            int i = 1 / 0;

            // 执行事务
            multi.exec();

        } catch (Exception e) {

            // 放弃事务
            multi.discard();
            e.printStackTrace();

        } finally {
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));

            jedis.close();
        }
    }
}
