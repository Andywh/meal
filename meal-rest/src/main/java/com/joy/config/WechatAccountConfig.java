package com.joy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by SongLiang on 2019-08-29
 */
@Data
@Component
@ConfigurationProperties(prefix =  "wechat")
public class WechatAccountConfig {

    private String mpAppId;

    private String mpAppSecret;

}
