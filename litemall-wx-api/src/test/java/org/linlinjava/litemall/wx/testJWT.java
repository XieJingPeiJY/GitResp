package org.linlinjava.litemall.wx;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class testJWT {   //token不需要存进数据库；；设置 有效期  token自带算法有  时间戳，日历 ，把用户名；用户id 加密成字符串等
                            //最后都会解密成相应的时间点验证

//<dependency>    导入JWT依赖
//<groupId>com.auth0</groupId>
//<artifactId>java-jwt</artifactId>
//</dependency>


        // 秘钥
        static final String SECRET = "X-Litemall-Token";
        // 签名是有谁生成
        static final String ISSUSER = "LITEMALL";
        // 签名的主题
        static final String SUBJECT = "this is litemall token";
        // 签名的观众
        static final String AUDIENCE = "MINIAPP";


        public String createToken(Integer userId,String username){              //生成token
            try {

                Algorithm algorithm = Algorithm.HMAC256(SECRET);  //Algorithm.HMAC256 加密  secret是用来加密数字签名的密钥

                Date nowDate = new Date();     //获取现在时间戳
                // 过期时间：2小时
                Date expireDate = getAfterDate(nowDate,0,0,0,2,0,0);     //设置过期的时间点   ，2个小时
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("alg", "HS256");// 为了设置头部信息
                map.put("typ", "JWT");// 为了设置头部信息
                String token = JWT.create()     //JWTCreator.init()。。。。   生成token
                        // 设置头部信息 Header
                        .withHeader(map)             // 设置头部信息
                        // 设置 载荷 Payload
                        .withClaim("userId", userId)        //传进用户id
                        .withClaim("opo",username)     //此处可以传入多个值；验证成功后；可以get到制定的值
                                                       //验证失败后try  catch  的catch会抛异常
                        .withIssuer(ISSUSER)         // 签名是由谁生成   ISSUSER等字符串；自己定义
                        .withSubject(SUBJECT)        // 签名的主题
                        .withAudience(AUDIENCE)
                        // 生成签名的时间
                        .withIssuedAt(nowDate)
                        // 签名过期的时间
                        .withExpiresAt(expireDate)
                        // 签名 Signature
                        .sign(algorithm);          // Algorithm algorithm = Algorithm.HMAC256(SECRET);  加密过的签名放入          //以上生成token的配置
                return token;       //token为一段字符串
            } catch (JWTCreationException exception){
                exception.printStackTrace();
            }
            return null;
        }

        public String check(String token) {     //验证token
            try {
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(ISSUSER)
                        .build();
                DecodedJWT jwt = verifier.verify(token);    //先验证签名  ，原理里面步骤1， DecodedJWT jwt = JWT.decode(token);

                Map<String, Claim> claims = jwt.getClaims();   //claims里面有userid，生成和客户端返回token封装了用户id
                Claim claim = claims.get("opo");         //获得用户id等
                String s = claim.asString();
                return s;

            } catch (JWTVerificationException exception){
                System.out.println("验证失败");
                return "false";
                //			exception.printStackTrace();
            }


        }

    public  Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second){
        if(date == null){
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();   //日历

        cal.setTime(date);
        if(year != 0){              // 以下是增加时间，设置有效期
            cal.add(Calendar.YEAR, year);
        }
        if(month != 0){
            cal.add(Calendar.MONTH, month);
        }
        if(day != 0){
            cal.add(Calendar.DATE, day);
        }
        if(hour != 0){
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if(minute != 0){
            cal.add(Calendar.MINUTE, minute);
        }
        if(second != 0){
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    public static void main(String[] args) {
        testJWT testJWT = new testJWT();
      //  String token = testJWT.createToken(888888,"OPO");
      //  token=token+"909mm";
     //   System.out.println(token);
        String check = testJWT.check("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGlzIGlzIGxpdGVtYWxsIHRva2VuIiwiYXVkIjoiTUlOSUFQUCIsImlzcyI6IkxJVEVNQUxMIiwiZXhwIjoxNjI1NDAxMDcxLCJ1c2VySWQiOjg4ODg4OCwiaWF0IjoxNjI1MzkzODcxLCJvcG8iOiJPUE8ifQ.5ePb-Fr-XbTwLYcu58gsfMWzU8NG81NLnFlkSzMdojk");
        System.out.println(check);

    }



}
