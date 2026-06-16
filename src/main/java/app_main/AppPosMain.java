package app_main;

import app_order_entry_system.AppOrderEntry;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tutorial_app_product_maintenance.AppProductMaintenance;

public class AppPosMain extends Application {

    private StackPane contentArea;
    private Button btnOrder;
    private Button btnProduct;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("CafeTime 咖啡廳點餐系統");

        db.DBInitializer initializer = new db.DBInitializer();
        initializer.setupTables();

        BorderPane mainLayout = new BorderPane();

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(22, 35, 22, 35));
        topBar.setSpacing(25);
        topBar.setStyle(
                "-fx-background-color: #FFF8F0;"
                + "-fx-border-color: #E8D8C5;"
                + "-fx-border-width: 0 0 1 0;"
        );

        VBox titleBox = new VBox(4);

        Label title = new Label("CafeTime");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 34));
        title.setTextFill(Color.web("#5C3D2E"));

        Label subtitle = new Label("自助咖啡點餐系統");
        subtitle.setFont(Font.font("Segoe UI", 15));
        subtitle.setTextFill(Color.web("#9B7B60"));

        titleBox.getChildren().addAll(title, subtitle);

        StackPane spacer = new StackPane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnOrder = createTopButton("點餐系統");
        btnProduct = createTopButton("產品管理");

        btnOrder.setOnAction(e -> {
            setActiveButton(btnOrder);
            try {
                AppOrderEntry appOrderEntry = new AppOrderEntry();
                switchView(appOrderEntry.getRootPane());
            } catch (Exception ex) {
                ex.printStackTrace();
                switchView(createPlaceholder("點餐系統載入失敗"));
            }
        });

        btnProduct.setOnAction(e -> {
            setActiveButton(btnProduct);
            try {
                AppProductMaintenance appProductMaintenance = new AppProductMaintenance();
                switchView(wrapProductManagement(appProductMaintenance.getRootPane()));
            } catch (Exception ex) {
                ex.printStackTrace();
                switchView(createPlaceholder("產品管理載入失敗"));
            }
        });

        HBox navBox = new HBox(12);
        navBox.setAlignment(Pos.CENTER_RIGHT);
        navBox.getChildren().addAll(btnOrder, btnProduct);

        topBar.getChildren().addAll(titleBox, spacer, navBox);

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(25));
        contentArea.setStyle("-fx-background-color: #F6F0E8;");

        mainLayout.setTop(topBar);
        mainLayout.setCenter(contentArea);

        btnOrder.fire();

        Scene scene = new Scene(mainLayout, 1280, 800);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private Button createTopButton(String text) {

        Button btn = new Button(text);

        btn.setMinWidth(120);
        btn.setMinHeight(42);
        btn.setStyle(getNormalButtonStyle());

        btn.setOnMouseEntered(e -> {
            if (!btn.getStyleClass().contains("active")) {
                btn.setStyle(getHoverButtonStyle());
            }
        });

        btn.setOnMouseExited(e -> {
            if (!btn.getStyleClass().contains("active")) {
                btn.setStyle(getNormalButtonStyle());
            }
        });

        return btn;
    }

    private void setActiveButton(Button activeBtn) {

        btnOrder.getStyleClass().remove("active");
        btnProduct.getStyleClass().remove("active");

        btnOrder.setStyle(getNormalButtonStyle());
        btnProduct.setStyle(getNormalButtonStyle());

        activeBtn.getStyleClass().add("active");
        activeBtn.setStyle(getActiveButtonStyle());
    }

    private String getNormalButtonStyle() {
        return "-fx-background-color: white;"
                + "-fx-text-fill: #5C3D2E;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 22;"
                + "-fx-border-color: #E1C7AC;"
                + "-fx-border-radius: 22;"
                + "-fx-cursor: hand;";
    }

    private String getHoverButtonStyle() {
        return "-fx-background-color: #C8A27A;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 22;"
                + "-fx-border-color: #C8A27A;"
                + "-fx-border-radius: 22;"
                + "-fx-cursor: hand;";
    }

    private String getActiveButtonStyle() {
        return "-fx-background-color: #5C3D2E;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 22;"
                + "-fx-border-color: #5C3D2E;"
                + "-fx-border-radius: 22;"
                + "-fx-cursor: hand;";
    }

    private void switchView(Node node) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(node);
    }

    private Node wrapProductManagement(Node node) {

        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(20));
        wrapper.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 28;"
                + "-fx-border-color: #EFE1D1;"
                + "-fx-border-radius: 28;"
        );

        Label title = new Label("產品管理");
        title.setFont(Font.font("Microsoft JhengHei", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#5C3D2E"));

        Label subtitle = new Label();
        subtitle.setFont(Font.font("Microsoft JhengHei", 14));
        subtitle.setTextFill(Color.web("#9B7B60"));

        wrapper.getChildren().addAll(title, subtitle, node);

        return wrapper;
    }

    private Node createPlaceholder(String text) {

        Label label = new Label(text);
        label.setFont(Font.font("Microsoft JhengHei", FontWeight.BOLD, 26));
        label.setTextFill(Color.web("#5C3D2E"));

        return new StackPane(label);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
