package com.dolly.com.redis_stream_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class StreamConsumerRunner implements ApplicationRunner, DisposableBean {

    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer;

    @Autowired
    private StreamMessageListener streamMessageListener;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 创建配置对象
        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainerOptions
                        .builder()
                        // 一次性最多拉取多少条消息
                        .batchSize(1)
                        // 超时时间，设置为0，表示不超时（超时后会抛出异常）
                        .pollTimeout(Duration.ZERO)
                        // 序列化器
                        .serializer(new StringRedisSerializer())
                        .build();

        // 根据配置对象创建监听容器对象
        streamMessageListenerContainer = StreamMessageListenerContainer.create(stringRedisTemplate.getConnectionFactory(), options);

        // 使用监听容器对象开始监听消费（使用的是手动确认方式）
        streamMessageListenerContainer.receive(
                Consumer.from("dispatch_group", "dispatch_group@dolly"),
                StreamOffset.create("dispatch_stream", ReadOffset.lastConsumed()),
                streamMessageListener);

        // 启动监听
        streamMessageListenerContainer.start();

    }

    @Override
    public void destroy() throws Exception {
        streamMessageListenerContainer.stop();
    }
}