package com.haoran.investor.stock;

import lombok.Data;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Raymond
 * @date 2019/4/9
 */
@Data
public class StockMessage {
    private final static Pattern STOCK_CODE_PATTERN = Pattern.compile("[^\\d+](?<code>\\d+)");

    private String code;
    private String name;
    private BigDecimal preClose;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal changeAmount;


    public static StockMessage fromString(String quoStr) {
        String[] fieldArr = quoStr.split("=");
        StockMessage stockMessage = new StockMessage();
        Matcher matcher = STOCK_CODE_PATTERN.matcher(fieldArr[0]);
        if(matcher.find()) {
            stockMessage.setCode(matcher.group("code"));
        } else {
            return null;
        }
        String[] quotaArray = fieldArr[1].replace("\n", "").split(",");
        stockMessage.setName(quotaArray[0].replace("\"", ""));
        stockMessage.setPrice((new BigDecimal(quotaArray[3])).setScale(2, BigDecimal.ROUND_HALF_UP));
        stockMessage.setPreClose((new BigDecimal(quotaArray[2])).setScale(2, BigDecimal.ROUND_HALF_UP));
        BigDecimal change = BigDecimal.ZERO;
        if(stockMessage.getPreClose() != null && stockMessage.getPreClose().compareTo(BigDecimal.ZERO) != 0) {
            change = (stockMessage.getPrice().subtract(stockMessage.getPreClose())).divide(stockMessage.getPreClose(), 4, BigDecimal.ROUND_HALF_EVEN);
            stockMessage.setChange(change);
            stockMessage.setChangeAmount((stockMessage.getPrice().subtract(stockMessage.getPreClose())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return stockMessage;
    }
}
