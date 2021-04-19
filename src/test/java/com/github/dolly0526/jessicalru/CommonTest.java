package com.github.dolly0526.jessicalru;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

/**
 * @author yusenyang
 * @create 2021/4/19 12:03
 */
public class CommonTest {

    @Test
    public void testJson() {

        String str = "{\"nickname\":\"dolly123\",\"username\":\"yusenyang\"}";
        System.out.println(JSONUtil.isJson(str));
    }
}
