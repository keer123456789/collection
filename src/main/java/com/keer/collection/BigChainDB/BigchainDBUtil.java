package com.keer.collection.BigChainDB;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.*;

import com.google.gson.JsonSyntaxException;
import com.keer.collection.domain.BigchainDBData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class BigchainDBUtil {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBUtil.class);
    @Autowired
    KeyPairHolder keyPairHolder;
    /**
     * 给资产增加metadata信息
     * <p>
     * youself is KeyPairHolder.getKeyPair() representive
     *
     * @param metaData
     * @param assetId
     * @return
     * @throws Exception
     */
    public  String transferToSelf(BigchainDBData metaData, String assetId) {

        Transaction transferTransaction = null;
        try {
            transferTransaction = BigchainDbTransactionBuilder
                    .init()
                    .operation(Operations.TRANSFER)
                    .addAssets(assetId, String.class)
                    .addMetaData(metaData)
                    .addInput(null, transferToSelfFulFill(assetId), keyPairHolder.getPublic())
                    .addOutput("1", keyPairHolder.getPublic())
                    .buildAndSign(
                            keyPairHolder.getPublic(),
                            keyPairHolder.getPrivate())
                    .sendTransaction();
        } catch (Exception e) {
            logger.error("资产ID：" + assetId + ",不存在!!!!!!!");
            return null;
        }
        return transferTransaction.getId();
    }

    /**
     * 通过资产id获取最后交易输出
     *
     * @param assetId
     * @return
     * @throws IOException
     */
    private  FulFill transferToSelfFulFill(String assetId) throws IOException {
        final FulFill spendFrom = new FulFill();
        String transactionId = getLastTransactionId(assetId);
        spendFrom.setTransactionId(transactionId);
        spendFrom.setOutputIndex(0);
        return spendFrom;
    }

    /**
     * 通过资产id获取最后交易id
     *
     * @param assetId asset Id
     * @return last transaction id
     * @throws IOException
     */
    public  String getLastTransactionId(String assetId) throws IOException {
        return getTransactionId(getLastTransaction(assetId));
    }

    /**
     * 通过资产id获得最后交易信息
     *
     * @param assetId assetId
     * @return last transaction
     * @throws IOException
     */
    public  Transaction getLastTransaction(String assetId) throws IOException {
        List<Transaction> transfers = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).getTransactions();

        if (transfers != null && transfers.size() > 0) {
            return transfers.get(transfers.size() - 1);
        } else {
            return getCreateTransaction(assetId);
        }
    }

    /**
     * 通过资产id得到transaction（create）
     *
     * @param assetId
     * @return
     * @throws IOException
     */
    public  Transaction getCreateTransaction(String assetId) throws IOException {
        try {
            Transactions apiTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.CREATE);

            List<Transaction> transactions = apiTransactions.getTransactions();
            if (transactions != null && transactions.size() == 1) {
                return transactions.get(0);
            } else {
                return null;
            }

        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Transaction获取交易id
     *
     * @param transaction
     * @return
     */
    private  String getTransactionId(Transaction transaction) {
        String withQuotationId = transaction.getId();
        return withQuotationId.substring(1, withQuotationId.length() - 1);
    }

    /**
     * 检查交易是否存在
     *
     * @param txID
     * @return
     */
    public  boolean checkTransactionExit(String txID) {
        try {
            Thread.sleep(2000);
            Transaction transaction = TransactionsApi.getTransactionById(txID);
            Thread.sleep(2000);
            if (transaction.getId() != null) {
                logger.info("交易存在！！ID：" + txID);
                return true;
            } else {
                logger.info("交易不存在！！ID：" + txID);
                return true;
            }
        } catch (Exception e) {
            logger.info("未知错误！！！");
            e.printStackTrace();
            return false;

        }

    }


    public  Transaction getTransactionByTXID(String ID) {
        logger.info("开始查询交易信息：TXID：" + ID);
        try {
            logger.info("查询成功！！！！！！");
            return TransactionsApi.getTransactionById(ID);
        } catch (IOException e) {
            logger.error("交易不存在，TXID：" + ID);
            return null;
        }
    }
}
