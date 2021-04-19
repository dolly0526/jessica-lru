package com.github.dolly0526.jessicalru.server;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU缓存
 *
 * @author yusenyang
 * @create 2021/4/18 21:46
 */
@Slf4j
public class LruCache extends LinkedHashMap<String, Object> {

    private int _capacity;

    public LruCache(int capacity) {
        super(capacity, 0.8f, true);
        _capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
        return size() > _capacity;
    }
}
