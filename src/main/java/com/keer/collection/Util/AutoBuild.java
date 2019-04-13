package com.keer.collection.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoBuild {
    protected static final Logger logger = LoggerFactory.getLogger(AutoBuild.class);

    @Value("${GetEnvUrl}")
    private String url;
    @Autowired
    FileUtil fileUtil;

    @Scheduled(cron="0 0 1 * * ? ")
    public void scheduled() {
        String json = HttpUtil.httpGet(url);
        logger.info(json);


    }
}
