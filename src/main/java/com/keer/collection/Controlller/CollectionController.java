package com.keer.collection.Controlller;


import com.keer.collection.Listener.MyApplicationEvent;
import com.keer.collection.Service.CollectionService;
import com.keer.collection.domain.Info;
import com.keer.collection.domain.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Map;

@RestController
public class CollectionController {


    protected static final Logger logger = LoggerFactory.getLogger(CollectionController.class);
    @Autowired
    CollectionService collectionService;
    @Autowired
     private ApplicationContext publisher;

    /**
     *
     * @param info
     * @param request
     * @return
     */
    @PostMapping("/info")
    public JsonResult setInfo(@RequestBody Info info,HttpServletRequest request) throws InterruptedException {
        logger.info("请求ip:"+getIpAddr(request));
        info.setIp(getIpAddr(request));
        return collectionService.setInfo(info);
    }


    /**
     * nodemcu请求环境数据范围
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/getEnv")
    public String getEnv() throws InterruptedException {
        logger.info("请求数据范围………………");
        return collectionService.getEnv();
    }

    @PostMapping("/get")
    public String getData(@RequestBody Map map){
        logger.info(map.toString());
        return "success";
    }

    /**
     * 获取请求IP
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
