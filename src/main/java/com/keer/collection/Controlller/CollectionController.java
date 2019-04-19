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
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class CollectionController {


    protected static final Logger logger = LoggerFactory.getLogger(CollectionController.class);
    @Autowired
    CollectionService collectionService;

    private boolean isCreate=false;//资产创建与否

    private String assetId="";//资产ID


    private int index=0;//数据批次

    private List<Map> data=new ArrayList<>();
    @Autowired
     private ApplicationContext publisher;




    /**
     * nodemcu请求环境数据范围
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/getEnv")
    public String getEnv() throws InterruptedException {
        logger.info("nodemcu请求数据范围………………");
        return collectionService.getEnv();
    }

    /**
     * nodemcu 发送数据
     * @param tem
     * @param hum
     * @param co
     * @return
     */
    @PostMapping("/get")
    public String getData(String tem,String hum,String co,String id){
        logger.info("准备发送数据…………");
        if(Integer.parseInt(id)==index&&data.size()<3){
            Map map=new HashMap();
            map.put("temperture",tem);
            map.put("humidity",hum);
            map.put("CO2",co);
            map.put("id",id);
            data.add(map);
        }
        if(data.size()>=3){
            if(collectionService.sendData(data,assetId)){
                data.clear();
                index++;
            }
        }
        return "success";
    }



    /**
     * 服务器发来请求，此树莓派已经被创建
     * @return
     */
    @GetMapping("/isCreate/{assetID}")
    public String isCreate(@PathVariable String assetID){
        logger.info("服务器发来请求，此树莓派已经被创建 资产ID："+assetID);
        isCreate=true;
        assetId=assetID;
        return "success";
    }

    /**
     * nodemcu 请求是否可以发送数据
     * @return 树莓派资产创建完成，返回success，没有创建，返回fail
     */
    @GetMapping("/isSend")
    public String isSend(){
        logger.info("nodemcu请求是否可以发送数据"+isCreate);
        if(isCreate){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * nodemcu 发来请求，获取数据的index
     * @return 返回index
     */
    @GetMapping("/dataIndex")
    public String dataIndex(){
        logger.info("nodemcu获取数据index，"+index);
        return index+"";
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
