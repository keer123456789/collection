package com.keer.collection.Service;

import com.keer.collection.Controlller.CollectionController;
import com.keer.collection.Util.FileUtil;
import com.keer.collection.domain.Info;
import com.keer.collection.domain.Infos;
import com.keer.collection.domain.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {
    protected static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

    /**
     * 数据写入
     * @param info
     * @return
     */
    public JsonResult setInfo(Info info){
        JsonResult jsonResult=new JsonResult();
        String path="./JsonData/"+info.getId()+".json";
        Infos infos= FileUtil.readFile(path);
        if(infos==null){
            infos=new Infos();
            infos.addInfo(info);
            infos.setId(info.getId());
            if(FileUtil.writeFile(infos)){
                return success(path);
            }
            else{
                return error(path);
            }
        }else if(infos.getInfos().size()<3 && infos.getInfos().size()>=0){
            infos=FileUtil.readFile(path);
            infos.addInfo(info);
            logger.info("正在写入数据");
            if(FileUtil.writeFile(infos)){
                return success(path);
            }else{
                return error(path);
            }
        }else{
            logger.error("******************多余数据写入，********************");
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("多与数据写入");
            return jsonResult;
        }


    }

    /**
     * 写入数据错误
     * setInfo使用
     * @param path
     * @return
     */
    private JsonResult error(String path){
        JsonResult jsonResult=new JsonResult();
        logger.error("数据写入"+path+"失败！！");
        jsonResult.setMessage("数据写入失败");
        jsonResult.setState(JsonResult.ERROR);
        return jsonResult;

    }

    /**
     * 写入数据成功
     * setInfo使用
     * @param path
     * @return
     */
    private JsonResult success(String path){
        JsonResult jsonResult=new JsonResult();
        logger.info("数据成功写入"+path);
        jsonResult.setMessage("数据写入成功");
        jsonResult.setState(JsonResult.SUCCESS);
        return jsonResult;
    }
}
