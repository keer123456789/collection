package com.keer.collection.Init;

import com.alibaba.fastjson.JSON;
import com.keer.collection.Util.FileUtil;
import com.keer.collection.Util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

@Component
public class Init implements CommandLineRunner {

    protected static Logger logger = LoggerFactory.getLogger(Init.class);

    @Value("${initURL}")
    private String initUrl;
    @Value("${GetEnvUrl}")
    private String getEnvUrl;
    @Autowired
    FileUtil fileUtil;

    @Override
    public void run(String... args) throws Exception {
        for (; true; ) {
            if (HttpUtil.httpGet(initUrl).equals("true")) {
                logger.info("注册成功");
                break;
            }
        }

        for (; true; ) {
            logger.info("请求服务器获取数据范围");
            String json = HttpUtil.httpGet(getEnvUrl);
            Map map = (Map) JSON.parse(json);
            if(fileUtil.writeFile("./env.json", map.get("data").toString())){
                break;
            }

        }
    }

    /**
     * 获取本地mac
     * @return
     * @throws UnknownHostException
     * @throws SocketException
     */
    private String getlocalMac() throws UnknownHostException, SocketException {
        InetAddress ia = InetAddress.getLocalHost();
        System.out.println(ia);
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);

            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        System.out.println("本机MAC地址:"+sb.toString().toUpperCase());
        return sb.toString().toUpperCase();
    }
}
