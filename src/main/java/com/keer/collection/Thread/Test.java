package com.keer.collection.Thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.keer.collection.domain.Infos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    /**
     * 创建json文件
     * 会覆盖之前的数据。
     * @param infos
     * @return
     */
    public static boolean writeFile(Infos infos) {
        String content= JSON.toJSONString(infos);
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
}
