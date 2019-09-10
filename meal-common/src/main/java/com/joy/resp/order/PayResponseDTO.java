package com.joy.resp.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by SongLiang on 2019-09-02
 */
@Data
public class PayResponseDTO implements Serializable {

    private String prePayParams;

    private URI payUri;

    private String appId;

    private String timeStamp;

    private String nonceStr;

    @JsonProperty("package")
    private String packAge;

    private String signType;

    private String paySign;

    private Double orderAmount;

    private String orderId;

    private String outTradeNo;

}