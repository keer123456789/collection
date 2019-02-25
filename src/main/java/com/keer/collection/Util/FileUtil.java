package com.keer.collection.Util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.keer.collection.domain.Info;
import com.keer.collection.domain.Infos;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


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

    public static void main(String[] args) {
        Infos infos=new Infos();
        Info info=new Info();
        info.setIp("123123");
        info.setTime("123");
        info.setId("1");
        infos.setId(info.getId());
        infos.addInfo(info);
        String content=JSON.toJSONString(infos);


        FileLock lock = null;
        FileChannel channel=null;
        try {
            ByteBuffer byteBuffer=ByteBuffer.wrap(content.getBytes("utf-8"));
            channel = new FileOutputStream("./test.json", false).getChannel();
            lock = channel.lock();
            channel.write(byteBuffer);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(lock!=null){
                try {
                    lock.release();
                    lock=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(channel!=null){
                try {
                    channel.close();
                    channel=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        try (FileChannel channel = new FileOutputStream("./test.json",true).getChannel()){
//            lock = channel.lock();//无参lock()为独占锁
//            channel.write(byteBuffer);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (lock != null) {
//                try {
//                    lock.release();
//                    lock = null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
