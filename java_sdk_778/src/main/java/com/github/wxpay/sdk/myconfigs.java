package com.github.wxpay.sdk;

import java.io.InputStream;

public class myconfigs extends WXPayConfig{
    @Override
    String getAppID() {
        return "wx3611d92ce184a570";
    }
    @Override
    String getMchID() {
        return "1627228242";
    }
    @Override
    String getKey() {
        return "44088119960318xiejingpei31981234";
    }
    @Override
    InputStream getCertStream() {
        return null;
    }
    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}
