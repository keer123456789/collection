package com.keer.collection.Init;

import com.keer.collection.Util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    protected static Logger logger= LoggerFactory.getLogger(Init.class);

    @Value("${initURL}")
    private String url;
    @Override
    public void run(String... args) throws Exception {
        for (; true; ) {
            if (HttpUtil.httpGet(url).equals("true")) {
                logger.info("注册成功");
                break;
            }
        }
    }
}
