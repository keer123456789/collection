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


@Service
public class CollectionService {
    protected static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private ApplicationContext publisher;

    @Autowired
    FileUtil fileUtil;





    /**
     * 获得env.json的文件中的数据
     * @return
     * @throws InterruptedException
     */
    public String getEnv() throws InterruptedException {
        String json=fileUtil.readFile("./env.json");
        Thread.sleep(1000);
        Map map= (Map) JSON.parse(json);
        Thread.sleep(1000);
        return map.get("temMax")+","+map.get("temMin")+","+map.get("HumMax")+","+map.get("HumMin")+","+map.get("CO2Max")+","+map.get("CO2Min");
    }


    public boolean sendData(List<Map> data,String assetId){
        double tem =0.0;
        double hum=0.0;
        double co2=0.0;
        for(Map map:data){
            tem=tem+Double.valueOf(map.get("temperture").toString());
            hum=hum+Double.valueOf(map.get("humidity").toString());
            co2=co2+Double.valueOf(map.get("CO2").toString());
        }
        tem=tem/data.size();
        hum=hum/data.size();
        co2=co2/data.size();
        Map map=new HashMap();
        map.put("temperture",tem+"");
        map.put("humidity",hum+"");
        map.put("CO2",co2+"");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(new Date());
        map.put("time",time);
        BigchainDBData bigchainDBData=new BigchainDBData("Environment",map);
        String TXID=BigchainDBUtil.transferToSelf(bigchainDBData,assetId);
        if(BigchainDBUtil.checkTransactionExit(TXID)){
            return true;
        }else {
            return false;
        }

    }
}
