package com.keer.collection.Service;

import com.alibaba.fastjson.JSON;
import com.keer.collection.BigChainDB.BigchainDBUtil;
import com.keer.collection.Util.FileUtil;
import com.keer.collection.domain.BigchainDBData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class CollectionService {
    protected static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private ApplicationContext publisher;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    BigchainDBUtil bigchainDBUtil;

    private boolean isUse=false;


    /**
     * 获得env.json的文件中的数据
     *
     * @return
     * @throws InterruptedException
     */
    public String getEnv() throws InterruptedException {
        String json = fileUtil.readFile("./env.json");
        Thread.sleep(1000);
        Map map = (Map) JSON.parse(json);
        Thread.sleep(1000);
        return map.get("temMax") + "," + map.get("temMin") + "," + map.get("HumMax") + "," + map.get("HumMin") + "," + map.get("CO2Max") + "," + map.get("CO2Min");
    }


    /**
     * 发送数据
     *
     * @param data
     * @param assetId
     * @return
     */
    public boolean sendData(List<Map> data, String assetId) throws InterruptedException {
        if(!isUse) {
            isUse=true;


            double tem = 0.0;
            int temp = 0;
            double hum = 0.0;
            int humi = 0;
            double co2 = 0.0;
            int co2sum = 0;
            for (Map map : data) {
                if (isDouble(map.get("temperture").toString())) {
                    tem = tem + Double.valueOf(map.get("temperture").toString());
                    temp++;
                }
                if (isDouble(map.get("humidity").toString())) {
                    hum = hum + Double.valueOf(map.get("humidity").toString());
                    humi++;
                }
                if (isDouble(map.get("CO2").toString())) {
                    co2 = co2 + Double.valueOf(map.get("CO2").toString());
                    co2sum++;
                }
            }
            Map map = new HashMap();

            //判断正常温度的个数，做平均数
            if (temp == 0) {
                map.put("temperture", "fail");
            } else {
                tem = tem / temp;
                map.put("temperture", tem + "");
            }

            //判断正湿度度的个数，做平均数
            if (humi == 0) {
                map.put("humidity", "fail");
            } else {
                hum = hum / humi;
                map.put("humidity", hum + "");
            }

            //判断正常二氧化碳的个数，做平均数
            if (co2sum == 0) {
                map.put("CO2", "fail");
            } else {
                co2 = co2 / co2sum;
                map.put("CO2", co2 + "");
            }


            map.put("id", data.get(0).get("id").toString());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            map.put("time", time);
            logger.info("此次最终环境数据为：" + map.toString());

            BigchainDBData bigchainDBData = new BigchainDBData("Environment", map);
            String TXID = bigchainDBUtil.transferToSelf(bigchainDBData, assetId);
            Thread.sleep(2000);
            if (bigchainDBUtil.checkTransactionExit(TXID)) {
                logger.info("交易ID：" + TXID);
                isUse=false;
                return true;
            } else {
                isUse=false;
                return false;
            }
        }else{
            return false;
        }

    }


    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }


}
