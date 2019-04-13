package com.haoran.investor.custom;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import lombok.var;

import java.math.BigDecimal;
import java.text.Format;

/**
 * @author Raymond
 * @date 2019/4/11
 */
public class ColumnFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    private Format format;

    public ColumnFormatter(Format format) {
        super();
        this.format = format;
    }
    @Override
    public TableCell<S, T> call(TableColumn<S, T> arg0) {
        var cell =  new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    Label lbl = new Label(format.format(item));
                    if(item != null && item instanceof BigDecimal) {
                        var val = (BigDecimal)item;
                        if(val.compareTo(BigDecimal.ZERO) > 0) {
                            lbl.setTextFill(Color.RED);
                            //setStyle("-fx-background-color: yellow");
                        } else if(val.compareTo(BigDecimal.ZERO) < 0) {
                            lbl.setTextFill(Color.GREEN);
                        }
                    }
                    setGraphic(lbl);

                }

            }
        };

        return cell;
    }
}

