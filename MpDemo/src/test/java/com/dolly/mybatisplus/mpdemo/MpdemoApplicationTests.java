package com.dolly.mybatisplus.mpdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dolly.mybatisplus.mpdemo.entity.User;
import com.dolly.mybatisplus.mpdemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MpdemoApplicationTests {

    @Autowired
    private UserMapper userMapper;


    @Test
    public void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void addUser() {
        User user = new User(111L, "yusenyang", 22, "dolly@mail.ustc.edu.cn");
        System.out.println(userMapper.insert(user));
    }

    @Test
    public void updateUser() {
        User user = new User(1L, "dolly", 22, "dolly@mail.ustc.edu.cn");
        System.out.println(userMapper.updateById(user));
    }

    @Test
    public void testOptimisticLock() {

        // 先查询，再修改
        User user = userMapper.selectById(1);
        user.setAge(200);
        userMapper.updateById(user);
    }

    @Test
    public void testSelect() {
        System.out.println(userMapper.selectById(1));
//        System.out.println(userMapper.selectBatchIds(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testSelectByMap() {
        Map<String, Object> map = new HashMap<String, Object>(){{
            put("name", "dolly");
            put("age", 22);
        }};

        System.out.println(userMapper.selectByMap(map));
    }

    @Test
    public void testPage() {
        Page<User> page = new Page(1, 3);
        System.out.println(userMapper.selectPage(page, null));
        System.out.println(page.getCurrent());
        System.out.println(page.getSize());
        System.out.println(page.getRecords());
        System.out.println(page.getTotal());
        System.out.println(page.getPages());
        System.out.println(page.hasPrevious());
        System.out.println(page.hasNext());
    }

    @Test
    public void testDeleteById() {
        userMapper.deleteById(1L);
    }

    @Test
    public void testBatchDeletes() {
        userMapper.deleteBatchIds(Arrays.asList(2L, 3L));
    }

    @Test
    public void testSelectQuery() {

        QueryWrapper<User> wrapper = new QueryWrapper<>();

//        wrapper.ge("age", 30);
        wrapper.between("age", 20, 30);

        System.out.println(userMapper.selectList(wrapper));
    }
}
