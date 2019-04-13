package com.haoran.investor;

import com.haoran.investor.custom.ColumnFormatter;
import com.haoran.investor.stock.StockMessage;
import com.haoran.investor.stock.StockReader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author Raymond
 * @date 2019/4/9
 */
public class MainController {

    @FXML
    private TableView<StockMessage> tbStockList;
    @FXML
    private TableColumn<StockMessage, BigDecimal> colAmount;
    @FXML
    private TableColumn<StockMessage, BigDecimal> colChange;


    @FXML
    public void initialize(){
        tbStockList.setId("tbStockList");
        String[] codes = new String[] {"000001", "000166", "600518", "000538", "600436", "000423", "600085"};
        StockReader reader = new StockReader(Arrays.asList(codes));
        reader.addListener((List<StockMessage> messageList) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(tbStockList.getItems() != null) {
                        tbStockList.getItems().clear();
                    }
                    tbStockList.getItems().addAll(messageList);
                }
            });
        });
        MainApp.STOCK_FETCH_POOL.execute(reader);
        setColumns();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("hello,world");
    }

    private void setColumns() {
        colAmount.setCellFactory(new ColumnFormatter<StockMessage, BigDecimal>(new DecimalFormat("0.00")));
        colChange.setCellFactory(new ColumnFormatter<StockMessage, BigDecimal>(new DecimalFormat("0.00%")));
    }
}

