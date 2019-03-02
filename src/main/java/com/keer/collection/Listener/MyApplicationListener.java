package com.keer.collection.Listener;

import com.keer.collection.Util.FileUtil;
import org.springframework.context.ApplicationListener;

public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {

    public void onApplicationEvent(MyApplicationEvent event) {
        int sum= FileUtil.getDirSize("./JsonData");
    }

}
