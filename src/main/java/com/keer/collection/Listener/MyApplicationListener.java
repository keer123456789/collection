package com.keer.collection.Listener;

import com.alibaba.fastjson.JSON;
import com.keer.collection.Service.CollectionService;
import com.keer.collection.Util.FileUtil;
import com.keer.collection.domain.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {
    protected static final Logger logger = LoggerFactory.getLogger(MyApplicationListener.class);

    public void onApplicationEvent(MyApplicationEvent event) {
        int sum = FileUtil.getDirSize("./JsonData");
        if (sum != 0) {
            logger.info("开始进行计算");
            computeData();
        } else {
            logger.info("数据文件个数：" + sum + "，不进行计算！！");
        }

    }

    private static void computeData() {
        String dir = "./JsonData";
        File typeFile = new File(dir);
        List<Info> infos = new ArrayList<>();
        File[] typeFiles = typeFile.listFiles();
        for (int i = 0; i < typeFiles.length; i++) {
            String type = typeFiles[i].getName();
            String typeDir = dir + "/" + type;
            File ipFile = new File(typeDir);
            File[] ipFiles = ipFile.listFiles();
            if (ipFiles.length == 3) {
                for (int j = 0; j < ipFiles.length; j++) {
                    String ip = ipFiles[j].getName();
                    String ipDir = typeDir + "/" + ip;
                    File dataFile = new File(ipDir);
                    File[] dataFiles = dataFile.listFiles();
                    String data = FileUtil.readFile(dataFiles[0]);
                    Info info = JSON.parseObject(data, Info.class);
                    infos.add(info);
                }
                logger.info("你好：" + infos.size());
            } else {
                logger.info("文件个数不足，不进行计算");
            }
        }





    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static void main(String[] args) {
        File file=new File("./JsonData/");
        logger.info(String.valueOf(deleteDir(file)));
    }

}
