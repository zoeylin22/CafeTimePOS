package app_order_entry_system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * CafeTime 點餐系統
 */
public class AppOrderEntry extends Application {

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(getRootPane(), 1200, 750);

        primaryStage.setTitle("CafeTime 點餐系統");

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public BorderPane getRootPane() {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(15));

        root.setStyle(
                "-fx-background-color: #F6F0E8;"
        );

        // Bootstrap CSS
        root.getStylesheets().add(
                getClass()
                        .getResource("/css/bootstrap3.css")
                        .toExternalForm()
        );

        // 購物車
        OrderPane orderPane = new OrderPane();

        // 商品區
        ProductMenuPane productMenuPane
                = new ProductMenuPane();

        productMenuPane.setOrderPane(orderPane);

        // Layout
        root.setCenter(productMenuPane);

        root.setRight(orderPane);

        BorderPane.setMargin(
                orderPane,
                new Insets(0, 0, 0, 20)
        );

        return root;
    }

    public static void main(String[] args) {

        launch(args);
    }
}