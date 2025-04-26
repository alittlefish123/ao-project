package com.qingge.springboot;

import cn.hutool.crypto.asymmetric.Sign;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;

@SpringBootTest
class SpringbootApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void aliPayTest(){
         String APPID = "9021000141676561";

         String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCbRvduowyIOAGYK61yDG8u+cz3wxp+ZIgPGLQEjpnEruzFD/GVf/ewU0y/HrQEwqIkpWdcfaL2lRwiJnqprKJss3fcw7CfAsmKcPvdIKLt2FXQkt3zTFrs56NOGlYvj5/0p1N2otldqBuTYs3uWwZooVWpdwgk20j9ug/ZxlAws7DG4KERnf87mE9DHTp2Lf7blstG18ZloZE+2BxjPFaKUNy0hJPwScP6Z/wHp0HtkXwA+Gz3o1JqVft6GB2pzNNiLc5P+IwbED3q7F0HiKRoetgTmxmW4BDUKmp7qtmKJXv3bezZ2QhqMLH2pmZiYEwSmU4w8ep5UKbf7WRuyqI1AgMBAAECggEBAJA5cklJGLLNkm2M5CUyd3Ligwb+KPWM2vNqtoolCZ4t+61uCa4ali0osfgyxHJHdeMj3sWBzIPMK71skDJZLuRYLdTXzo1jQV9dYhMZ5TuV3N9GCvGY0juJS52XyMWUWbYcVOG0kMD1C+fxteTWlzVH5RDqhp31L+67i01zbbhNO7TIaVwW5uW3gw8NaKL5nJGx9ahIVkUHvliA8y+qPh/3sRh1Fg9FsFuf7vYBljVMPGipMVxMvHAOhz7vR6oifggeJydA0o0Mfn1TL8+2wESJitvv0LacOxOso13cH4REY1wciQ/ZZMhgIzjRmbhzXLelNyJZC4LsAmBgvCXRJgECgYEA05zo/TujAkTKHE5B79XFMjNDU9AV7bHnsPVTKYQgnyKmVylNH013rwXacGrg3J1P9BIPWJpvuNvvoEHaCc7TBTXH29ci96s2ttA6+HcCTXML1k1b9yx1ytscuLLG6CRaKq6Juc1h4LBZ41puOspLVwMnRBz1KysVKapQkJwRNbECgYEAu9j30OZ/q0GR57M26AwHO5Gwh5qQMYjFRHIHtXfMFul5qmI+1HhyVN/CbGYq+YWJ01fkF3hLFJtw/apWNNrDzjIze9G5CrSwqynTa1k4LxOJp+MiyUxiMQvlQgqQ/Kq3EK95OOJmUEKMko0PJan+MSLzeXFZg4lY+RHNSjlIocUCgYEAozBiTMds0hcSdfmzx6znQetehIf4ITINJt5Jn76RGaey94yWOQmYvpSY/BCwD/IQ8nvq8b1HYxvZE5E1AFNLUL1EvziNcsnKi/jRQUMlt2x9OGq3/UmAHUwUPitAUU2Xa0rmUy8byBa3eykF0LiomRWJdnGOcBftXhBFyP9SFkECgYBoRnzqfcmJK5Du6dEk5uhs7Obtn6NDnATFhfc5iRLF9T0QlBEEqQfujXfD7MMWWEa2Bygazy/+dHs4lQHdvULMh5mEtlrd0ofvu/wcrx49I79abv+0dq5bQ5s0GeF6INoWbRIBvcvF6etWvu8VnVP5usklNQLXvbqiKHupJDtpcQKBgQCY7+ziTeRknAh5XcA69umzzuuC5ffj3oeZmzfEz/N/DP1hktWYRlSPFFnfpHkzCWhkW0eIx6L6ftj62cASJIJRgvqvs8P0r5BStSDwvxUptUaB8bwmY1k/zDX0jvIh+GQFovSzUndFWZdqTGWRxRgXv1fdmUk8YPmxUwQG86/kRw==";
        //径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问

         String notify_url = " http://esfds4.natappfree.cc";
        //路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
         String return_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";

         String URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

         String CHARSET = "UTF-8";

         String FORMAT = "json";

         String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijHkVkFjKOBxMISFnKsi09Uktfs+vT93s/RzfSxeuGs4tdPOrLC00g+KCht76i87gD8RNQiOSjaxwb2W7kRhrK/kGjzq4Ry8Jg3ydr2jJ+bnlP2fGHRTx9SeHGoO6DinpvVuwnMJr/TPoXcwAZOU6OhJ16r5F3T2pkrdrQVhdmi6ZArcJQLokVXkNW5eOjiQvVtmmq56qikVf6yytIkeZOANxIDdiJqwDN/ZF1vDcQBmalyVpc1kc7VHgCbGsbFFr3TRgaRfGJtP+36XlLtxpsKVIz8ZmRZLUbw5za0+xtWS5es39UKfFat+QShHPm65jI1tiqTzYha3epKJk9fGXQIDAQAB";

         String log_path = "/log";

         String SIGNTYPE = "RSA2";



        AlipayClient alipayClient=new DefaultAlipayClient(return_url,APPID, RSA_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,
                SIGNTYPE);

        // 创建request并设置request参数
        AlipayTradePagePayRequest request=new AlipayTradePagePayRequest();
        request.setNotifyUrl(notify_url);

        // 获取二维码连接

    }

}
