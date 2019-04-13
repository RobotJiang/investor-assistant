package com.haoran.investor.stock;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

/**
 * @author Raymond
 * @date 2019/4/9
 */
public class StockReader extends Thread {
    private final static Long DEFAULT_SLEEP_TIME = 2000L;
    private final static String QUOTATAION_URL = "http://hq.sinajs.cn/list=%s";

    private List<MessageListener> listeners;
    private List<String> codes;

    public StockReader(List<String> codes) {
        this.codes = codes;
    }
    public void addListener(MessageListener messageListener) {
        if(listeners == null) {
            listeners = new ArrayList<MessageListener>();
        }
        listeners.add(messageListener);
    }

    @Override
    public void run() {
        while (TRUE) {
            fetchMessage();
            try {
                Thread.sleep(DEFAULT_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 从网络上获取数据并且向监听器发送消息
     */
    private void fetchMessage() {
        int timeout = 10;
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout * 1000)
            .setConnectionRequestTimeout(timeout * 1000)
            .setSocketTimeout(timeout * 1000).build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        String queryCodes = this.codes.stream().map(item -> buildAStockWithPrefix(item)).collect(Collectors.joining(","));

        try {
            HttpGet httpGet = new HttpGet(String.format(QUOTATAION_URL, queryCodes));
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            response.close();
            String[] quotations = content.split("\n");
            List<StockMessage> messages = new ArrayList<>();
            for(String item : quotations) {
                StockMessage msg = StockMessage.fromString(item);
                if(msg != null) {
                    messages.add(msg);
                }
            }
            if(messages.size() > 0 && this.listeners != null && this.listeners.size() > 0) {
                this.listeners.forEach(listener -> {
                    listener.onMessage(messages);
                });
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
    /**
     * 给A股代码添加合适前缀
     * @param code
     * @return
     */
    public  String buildAStockWithPrefix(String code) {
        switch (code.charAt(0)) {
            case '6':
            case '9':
                return String.format("%s%s", "sh", code);
            case '0':
            case '2':
            case '3':
                return String.format("%s%s", "sz", code);

            default:
                return code;
        }
    }
}
