package com.keer.collection.BigChainDB;


import com.bigchaindb.util.KeyPairUtils;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;

/**
 * BigchainDB秘钥的存放
 */
@Component
public class KeyPairHolder {
    private static Logger logger = LoggerFactory.getLogger(KeyPairHolder.class);

    @Value("${keypath}")
    private  String keyPath;

    /**
     * 通过./keypair.txt获得密钥对
     *
     * @return
     */
    public  KeyPair getKeyPairFromTXT() {
        try {
            return KeyPairUtils.decodeKeyPair(getKeyPairFormTXT());
        } catch (Exception e) {
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }

    public  KeyPair getKeyPairFromString(String key) {
        try {
            return KeyPairUtils.decodeKeyPair(key);
        } catch (Exception e) {
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }


    public  String getKeyPairFormTXT() {
        try {
            FileInputStream in = new FileInputStream(keyPath);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            String key = new String(buffer);
            logger.info("成功获得" + keyPath + "路径下的密钥");
            return key;
        } catch (Exception e) {
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }

    /**
     * 将密钥对存贮在./keypair.txt文件中
     *
     * @param keyPair
     */
    public  boolean SaveKeyPairToTXT(KeyPair keyPair) {
        return SaveKeyPairToTXT(keyPair,keyPath);
    }

    /**
     * 写秘钥到指定路径
     * @param keyPair
     * @param path
     * @return
     */
    public  boolean SaveKeyPairToTXT(KeyPair keyPair,String path) {
        try {
            logger.info("开始写密钥到" + path);
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(KeyPairUtils.encodePrivateKeyBase64(keyPair).getBytes());
            fos.close();
            logger.info("写密钥成功");
            return true;
        } catch (Exception e) {
            logger.error("写密钥失败");
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 获得密钥对
     *
     * @return
     */
    public  KeyPair getKeyPair() {
        KeyPairGenerator edDsaKpg = new KeyPairGenerator();
        logger.info("成功获取新的密钥对");
        return edDsaKpg.generateKeyPair();
    }

    //TODO 以下两个方法均是从项目下txt获得密钥，方便测试使用。！！！！之后应该吧路径写入配置文件

    /**
     * 从txt中获得发送交易使用的公钥
     *
     * @return
     */
    public  EdDSAPublicKey getPublic() {
        logger.info("获得" + keyPath + "中的公钥");
        return (EdDSAPublicKey) getKeyPairFromTXT().getPublic();
    }

    /**
     * 公钥转换成字符串
     *
     * @param key
     * @return
     */
    public  String pubKeyToString(EdDSAPublicKey key) {
        return KeyPairUtils.encodePublicKeyInBase58(key);
    }

    /**
     * 从txt获得发送交易使用的私钥
     *
     * @return
     */
    public  EdDSAPrivateKey getPrivate() {
        logger.info("获得" + keyPath + "中的私钥");
        return (EdDSAPrivateKey) getKeyPairFromTXT().getPrivate();
    }




}