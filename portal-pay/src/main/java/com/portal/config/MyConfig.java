package com.portal.config;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;

import java.io.InputStream;

public class MyConfig extends WXPayConfig {

    public String getAppID() {
        return "wx3963c4b88e10b849";
    }

    public String getMchID() {
        return "1535235291";
    }

    public String getKey() {
        return "f63be239c43f16bd52a3cf8a2fada0ea";
    }

    public InputStream getCertStream() {
        return null;
    }

    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            public void report(String s, long l, Exception e) {

            }

            public DomainInfo getDomain(WXPayConfig wxPayConfig) {
                return new DomainInfo("api.mch.weixin.qq.com",true);
            }
        };
    }
}