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
            String json = HttpUtil.httpGet(getEnvUrl);
            Map map = (Map) JSON.parse(json);
            if(fileUtil.writeFile("./env.json", map.get("data").toString())){
                break;
            }

        }
    }
}
