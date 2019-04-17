package com.keer.collection.Util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 每天凌晨1点请求环境数据范围
 */
//@Component
public class AutoBuild {
    protected static final Logger logger = LoggerFactory.getLogger(AutoBuild.class);

    @Value("${GetEnvUrl}")
    private String url;
    @Autowired
    FileUtil fileUtil;

    @Scheduled(cron="0 0 1 * * ? ")
    public void scheduled() {
        String json = HttpUtil.httpGet(url);
        Map map= (Map) JSON.parse(json);
        logger.info(map.toString());
        for(;true;) {
            if(fileUtil.writeFile("./env.json", map.get("data").toString())){
                break;
            }
        }

    }
}
