package com.keer.collection.Service;

import com.alibaba.fastjson.JSON;
import com.keer.collection.Listener.MyApplicationEvent;
import com.keer.collection.Util.FileUtil;
import com.keer.collection.domain.Info;
import com.keer.collection.domain.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class CollectionService {
    protected static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private ApplicationContext publisher;

    @Autowired
    FileUtil fileUtil;

    /**
     * 数据写入
     *
     * @param info
     * @return
     */
    public JsonResult setInfo(Info info) throws InterruptedException {
//        JsonResult jsonResult = new JsonResult();
//        String dir = "./JsonData/" + info.getType();
//        int sum = fileUtil.getDirSize(dir);
//        logger.info(String.valueOf(sum));
//        if (sum >= 0 && sum < 3) {
//            boolean read = fileUtil.writeFile("helo.json",info);
//            if (read) {
//                if (sum == 2) {
//                    logger.info("触发算法事件，即将开始计算");
//                    MyApplicationEvent event = new MyApplicationEvent(this);
//                    publisher.publishEvent(event);
//                }
//
//            } else {
//                return error(dir + "/" + info.getIp() + "/" + info.getId() + ".json");
//            }
//
//        } else {
//            return error(dir + "/" + info.getIp() + "/" + info.getId() + ".json");
//        }
//
//
//        return success(dir + "/" + info.getIp() + "/" + info.getId() + ".json");
        return null;
    }

    /**
     * 写入数据错误
     * setInfo使用
     *
     * @param path
     * @return
     */
    private JsonResult error(String path) {
        JsonResult jsonResult = new JsonResult();
        logger.error("数据写入" + path + "失败！！");
        jsonResult.setMessage("数据写入失败");
        jsonResult.setState(JsonResult.ERROR);
        return jsonResult;

    }

    /**
     * 写入数据成功
     * setInfo使用
     *
     * @param path
     * @return
     */
    private JsonResult success(String path) {
        JsonResult jsonResult = new JsonResult();
        logger.info("数据成功写入" + path);
        jsonResult.setMessage("数据写入成功");
        jsonResult.setState(JsonResult.SUCCESS);
        return jsonResult;
    }


    public String getEnv(){
        String json=fileUtil.readFile("./env.json");
        Map map= (Map) JSON.parse(json);
        return map.get("temMax")+","+map.get("temMin")+","+map.get("HumMax")+","+map.get("HumMin")+","+map.get("CO2Max")+","+map.get("CO2Min");
    }
}
