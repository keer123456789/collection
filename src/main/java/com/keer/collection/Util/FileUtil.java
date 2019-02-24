package com.keer.collection.Util;


import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.keer.collection.domain.Info;
import com.keer.collection.domain.Infos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;


public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建json文件
     * 会覆盖之前的数据。
     * @param infos
     * @return
     */
    public static boolean writeFile(Infos infos) {
        JSONWriter writer = null;
        String path = "./JsonData/" + infos.getId() + ".json";
        try {
            logger.info("开始写入文件…………");
            writer = new JSONWriter(new FileWriter(path));
            writer.startArray();

            writer.writeValue(infos);
            writer.endArray();
            writer.close();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            logger.info("写入文件错误");
            return false;
        }

    }

    /**
     * 读取json文件
     * @param path
     * @return
     */
    public static Infos readFile(String path) {
        JSONReader reader = null;
        try {
            reader = new JSONReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            logger.info("系统中找不到指定文件，path："+path);
            return null;
        }
        reader.startArray();
        Infos infos = new Infos();
        while (reader.hasNext()) {
            infos = reader.readObject(Infos.class);

        }
        reader.endArray();
        reader.close();
        return infos;

    }

    public static void main(String[] args) throws IOException {
        Infos infos=new Infos();
//        infos.setId("1");
//        for(int i=10;i<20;i++){
//            Info info=new Info();
//            info.setId("1");
//            info.setTime("12"+i);
//            info.setIp("192.168.85."+i);
//            infos.addInfo(info);
//        }
//
//        JSONWriter writer = new JSONWriter(new FileWriter("test.json"));
//        writer.startArray();
//
//        writer.writeValue(infos);
//        writer.endArray();
//        writer.close();
        Info info=new Info();
        info.setIp("123123");
        info.setTime("123");
        info.setId("1");
        String path="./JsonData/"+info.getId()+".json";
        infos= FileUtil.readFile(path);
        logger.info(infos.toString());
    }

}
