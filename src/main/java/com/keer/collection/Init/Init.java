package com.keer.collection.Init;

import com.keer.collection.Util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    protected static Logger logger= LoggerFactory.getLogger(Init.class);

    @Override
    public void run(String... args) throws Exception {
        for (; true; ) {
            if (HttpUtil.httpGet("192.168.137.1:8080/setRaspberryIP").equals("true")) {
                logger.info("注册成功");
                break;
            }
        }
    }
}
