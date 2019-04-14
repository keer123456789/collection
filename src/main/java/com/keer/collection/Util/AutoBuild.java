package com.keer.collection.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每天凌晨1点请求环境数据范围
 */
@Component
public class AutoBuild {
    protected static final Logger logger = LoggerFactory.getLogger(AutoBuild.class);

    @Value("${GetEnvUrl}")
    private String url;
    @Autowired
    FileUtil fileUtil;

    @Scheduled(cron="*/10 * * * * ? ")
    public void scheduled() {
        String json = HttpUtil.httpGet(url);
        logger.info(json);
        for(;true;) {
            if(fileUtil.writeFile("./env.json", json)){
                break;
            }
        }

    }
}