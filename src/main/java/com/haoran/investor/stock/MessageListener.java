package com.haoran.investor.stock;

import java.util.EventListener;
import java.util.List;

/**
 * @author Raymond
 * @date 2019/4/9
 */
public interface MessageListener extends EventListener {
    /**
     * 消息到达通知
     * @param stockMessages
     */
    void onMessage(List<StockMessage> stockMessages);
}
