package org.linlinjava.litemall.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP地址相关工具类
 */
public class IpUtil {

    private static final Log logger = LogFactory.getLog(IpUtil.class);

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;//先尝试获取 x-forwarded-for   Proxy-Client-IP  WL-Proxy-Client-IP   3个请求头的key，看能否获取ip
                            //上面的方法都不行，就ipAddress = request.getRemoteAddr()先获取本地ip
                            // 再通过 在获取 InetAddress inet = InetAddress.getLocalHost();
                            //再ipAddress=inet.getHostAddress();   就可以获取真实ip
                            //还有多个代理的情况，请看源码49行开始


        try {
                                        //先尝试获取 获取请求头信息的key>>>x-forwarded-for:       可以去前端的newwork捕获
            ipAddress = request.getHeader("x-forwarded-for");     // 如果为空就get request.getHeader("Proxy-Client-IP");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");      // 如果为空就get request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");     // 如果为空就get request.getRemoteAddr();
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  //
                ipAddress = request.getRemoteAddr();//如果还是为空就用 request.getRemoteAddr();获取
                if (ipAddress.equals("127.0.0.1")) {      //如果 request.getRemoteAddr()==127.0.0.1   代表是本机
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        logger.error(e.getMessage(), e);
                    }
                    ipAddress = inet.getHostAddress();    //把IP设置为   InetAddress.getLocalHost(); 获取用户本机ip地址
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));    //*********,获取第一ip  为客户端的真实ip
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;   //最终返回ip
    }
}
