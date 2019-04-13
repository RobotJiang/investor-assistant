package com.haoran.investor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.*;

/**
 * @author Raymond
 * @date 2019/4/9
 */
public class MainApp extends Application {
    private final static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
        .setNameFormat("stock-async-job-pool-%d").build();

    public final static ExecutorService STOCK_FETCH_POOL = new ThreadPoolExecutor(5, 10,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingDeque<>(20), namedThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        primaryStage.setTitle("Investor Assistant");
        Scene scene = new Scene(root, 450, 600);
        String loc = MainApp.class.getResource("/css/main.css").toExternalForm();
        scene.getStylesheets().add(loc);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                STOCK_FETCH_POOL.shutdown();
                Platform.exit();
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
