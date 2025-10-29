package com.hu.wink.controller;

import com.hu.wink.common.BaseResponse;
import com.hu.wink.common.ResultUtils;
import com.hu.wink.model.dto.user.UserLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 测试验证功能
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @PostMapping("/validate")
    public BaseResponse<String> testValidation(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        log.info("验证通过，接收到请求：{}", userLoginRequest);
        return ResultUtils.success("验证成功");
    }
}