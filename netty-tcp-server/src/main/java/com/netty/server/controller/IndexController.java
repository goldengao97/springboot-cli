package com.netty.server.controller;


import com.netty.server.entity.User;
import com.netty.server.service.IUserService;
import com.netty.server.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final IUserService userService;

    /**
     * 注解自动实现缓存
     */
    @GetMapping("/getUser")
    public User getUser(Long id) {
        return userService.getUser(id);
    }

    /**
     * 注解自动实现缓存清理
     */
    @GetMapping("/delUser")
    public String delUser(Long id) {
        userService.delUser(id);
        return "删除成功";
    }

    /**
     * 手动设置缓存
     */
    @GetMapping("/redis/set")
    public String set() {
        RedisUtils.save("key", new User().setUserId(999L).setSex("男").setUsername("王小锤").setRemark("手动设置缓存"), 6000);
        return "设置成功 ！";
    }

    /**
     * 手动获取缓存
     */
    @GetMapping("/redis/get")
    public User get() {
        return RedisUtils.get("key", User.class);
    }

    /**
     * 删除缓存
     * @return
     */
    @GetMapping("/redis/del")
    public Boolean del() {
        return RedisUtils.delete("key");
    }
}
