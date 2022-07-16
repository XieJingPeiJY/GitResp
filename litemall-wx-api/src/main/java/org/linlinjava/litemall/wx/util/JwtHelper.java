package org.linlinjava.litemall.wx.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
//<dependency>    导入JWT依赖
//<groupId>com.auth0</groupId>
//<artifactId>java-jwt</artifactId>
//</dependency>

public class JwtHelper {
	// 秘钥
	static final String SECRET = "X-Litemall-Token";
	// 签名是有谁生成
	static final String ISSUSER = "LITEMALL";
	// 签名的主题
	static final String SUBJECT = "this is litemall token";
	// 签名的观众
	static final String AUDIENCE = "MINIAPP";

	public static void main(String[] args) {
		JwtHelper jwtHelper = new JwtHelper();
		System.out.println(jwtHelper.createToken(3));
	}


	
	
	public String createToken(Integer userId){              //生成token
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
	
	public Integer verifyTokenAndGetUserId(String token) {     //验证token   //验证成功直接返回用户id
		try {
		    Algorithm algorithm = Algorithm.HMAC256(SECRET);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(ISSUSER)
		        .build();
		    DecodedJWT jwt = verifier.verify(token);              //先验证签名  ，里面步骤1， DecodedJWT jwt = JWT.decode(token);
		    Map<String, Claim> claims = jwt.getClaims();   //claims里面有userid，生成和客户端返回token封装了用户id
		    Claim claim = claims.get("userId");         //获得用户id
		    return claim.asInt();
		} catch (JWTVerificationException exception){
//			exception.printStackTrace();
		}
		
		return 0;
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
	
}
