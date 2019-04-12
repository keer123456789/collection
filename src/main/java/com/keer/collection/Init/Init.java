package com.keer.collection.Init;

import com.keer.collection.Util.HttpUtil;
import org.springframework.boot.CommandLineRunner;

public class Init implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        for (; true; ) {
            if (HttpUtil.httpGet("192.168.137.1:8080/setRaspberryIP").equals("true")) {
                break;
            }
        }
    }
}
