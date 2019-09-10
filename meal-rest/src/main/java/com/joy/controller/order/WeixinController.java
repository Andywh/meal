package com.joy.controller.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by SongLiang on 2019-08-29
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("/auth")
    @ResponseBody
    public String auth(@RequestParam("code") String code) {
        log.info("进入 auth 方法");
        log.info("code={}", code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx3e8c175a3509bc3f&secret=1d44927d9c2f2fe0239a6144f2e31bfe&code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);
        return "auth";
    }

}
