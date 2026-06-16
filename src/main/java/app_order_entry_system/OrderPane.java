package app_order_entry_system;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import db.OrderEntryDAO;

import data_type.OrderDetail;
import data_type.OrderDetailEntry;
import data_type.Product;
import data_type.SaleOrder;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

/**
 * CafeTime 購物車系統
 */
class OrderPane extends VBox {

    private final ObservableList<OrderDetailEntry> orderCart
            = FXCollections.observableArrayList();

    private final TableView<OrderDetailEntry> table
            = new TableView<>();

    private final TextArea display
            = new TextArea();

    public OrderPane() {

        table.setPlaceholder(
                new Label("購物車是空的")
        );

        this.setSpacing(20);

        this.setPadding(new Insets(20));

        this.setPrefWidth(480);

        this.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 25;"
                + "-fx-border-radius: 25;"
        );

        Label title = new Label("購物車");

        title.setStyle(
                "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #6F4E37;"
        );

        initializeOrderTable();
        this.getChildren().add(title);
        this.getChildren().add(table);
        this.getChildren().add(display);
        initializeOrderOperationContainer();
    }

    private void initializeOrderOperationContainer() {

        Button btnDelete = new Button("移除");

        btnDelete.setStyle(
                "-fx-background-color: #E57373;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 15;"
                + "-fx-padding: 10 20 10 20;"
        );

        btnDelete.setOnAction((ActionEvent event) -> {

            OrderDetailEntry selectedItem
                    = table.getSelectionModel()
                            .getSelectedItem();

            if (selectedItem != null) {

                orderCart.remove(selectedItem);

                checkTotal();
            }
        });

        Button btnCheckout = new Button("結帳");

        btnCheckout.setStyle(
                "-fx-background-color: #C19A6B;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 15;"
                + "-fx-padding: 10 20 10 20;"
        );

        btnCheckout.setOnAction((ActionEvent event) -> {

            if (!orderCart.isEmpty()) {

                double total = 0;

                for (OrderDetailEntry od : orderCart) {

                    total += od.getPrice()
                            * od.getQuantity();
                }

                final double finalTotal = total;

                // =========================
                // 付款方式選擇
                // =========================
                Dialog<ButtonType> paymentDialog
                        = new Dialog<>();

                paymentDialog.setTitle("選擇付款方式");

                Label titleLabel = new Label(
                        "請選擇付款方式"
                );

                titleLabel.setFont(
                        Font.font(
                                "Microsoft JhengHei",
                                FontWeight.BOLD,
                                22
                        )
                );

                ComboBox<String> paymentBox
                        = new ComboBox<>();

                paymentBox.getItems().addAll(
                        "現金付款",
                        "信用卡",
                        "LINE Pay"
                );

                paymentBox.setValue("現金付款");

                paymentBox.setPrefWidth(220);

                VBox paymentPane = new VBox(15);

                paymentPane.setPadding(
                        new Insets(20)
                );

                paymentPane.getChildren().addAll(
                        titleLabel,
                        paymentBox
                );

                paymentDialog.getDialogPane()
                        .setContent(paymentPane);

                paymentDialog.getDialogPane()
                        .getButtonTypes()
                        .addAll(
                                ButtonType.OK,
                                ButtonType.CANCEL
                        );

                paymentDialog.showAndWait()
                        .ifPresent(result -> {

                            if (result == ButtonType.OK) {

                                String paymentMethod
                                        = paymentBox.getValue();

                                boolean saveSuccess
                                        = saveOrderTo(
                                                finalTotal,
                                                orderCart
                                        );

                                orderCart.clear();

                                if (saveSuccess) {

                                    display.setText(
                                            "結帳完成\n"
                                            + "總金額 : "
                                            + Math.round(finalTotal)
                                            + " 元"
                                    );

                                    showCheckoutSuccessDialog(
                                            finalTotal,
                                            paymentMethod
                                    );

                                } else {

                                    display.setText("儲存失敗");
                                }
                            }
                        });

            } else {

                display.setText("購物車是空的");
            }
        });

        TilePane operationBtnTile
                = new TilePane();

        operationBtnTile.setVgap(10);

        operationBtnTile.setHgap(10);

        operationBtnTile.getChildren().addAll(
                btnDelete,
                btnCheckout
        );

        this.getChildren().add(
                operationBtnTile
        );
    }

    private void initializeOrderTable() {

        table.setEditable(true);
        table.setPrefHeight(380);
        table.setStyle(
                "-fx-background-radius: 15;"
                + "-fx-border-radius: 15;"
                + "-fx-font-size: 14px;"
        );

        TableColumn<OrderDetailEntry, String> order_item_name
                = new TableColumn<>("品名");

        order_item_name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        order_item_name.setPrefWidth(260);

        TableColumn<OrderDetailEntry, Integer> order_item_price
                = new TableColumn<>("價格");

        order_item_price.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );
        order_item_name.setPrefWidth(90);

        TableColumn<OrderDetailEntry, Integer> order_item_qty
                = new TableColumn<>("數量");

        order_item_qty.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
        order_item_name.setPrefWidth(90);

        order_item_qty.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new IntegerStringConverter()
                )
        );

        order_item_qty.setOnEditCommit(event -> {

            int rowNum
                    = event.getTablePosition()
                            .getRow();

            int newValue
                    = event.getNewValue();

            OrderDetailEntry target
                    = event.getTableView()
                            .getItems()
                            .get(rowNum);

            target.setQuantity(newValue);

            checkTotal();
        });

        table.getColumns().add(order_item_name);

        table.getColumns().add(order_item_price);

        table.getColumns().add(order_item_qty);

        table.setItems(orderCart);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        display.setWrapText(true);

        display.setEditable(false);

        display.setPrefHeight(80);

        display.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #6F4E37;"
                + "-fx-background-color: #F8F5F2;"
                + "-fx-background-radius: 15;"
        );

        display.setText(" ");
    }

    private void checkTotal() {

        double total = 0;

        for (OrderDetailEntry od : orderCart) {

            total += od.getPrice()
                    * od.getQuantity();
        }

        String totalmsg = String.format(
                "小計 : %d 元",
                Math.round(total)
        );

        display.setText(totalmsg);
    }

    public void addToCart(Product product) {

        for (OrderDetailEntry item : orderCart) {

            if (item.getId().equals(
                    product.getProductId()
            )) {

                int qty
                        = item.getQuantity() + 1;

                item.setQuantity(qty);

                table.refresh();

                checkTotal();

                return;
            }
        }

        OrderDetailEntry newOrder
                = new OrderDetailEntry(
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        1
                );

        orderCart.add(newOrder);

        checkTotal();
    }

    private void showCheckoutSuccessDialog(
            double total,
            String paymentMethod
    ) {

        Dialog<Void> dialog
                = new Dialog<>();

        dialog.setTitle("結帳完成");

        VBox box = new VBox(15);

        box.setAlignment(Pos.CENTER);

        box.setPadding(new Insets(25));

        box.setPrefWidth(360);

        Label title = new Label("訂單已完成");

        title.setFont(
                Font.font(
                        "Microsoft JhengHei",
                        FontWeight.BOLD,
                        28
                )
        );

        title.setTextFill(
                Color.web("#5C3D2E")
        );

        Label amount = new Label(
                "總金額：NT$ "
                + Math.round(total)
        );

        amount.setFont(
                Font.font(
                        "Microsoft JhengHei",
                        FontWeight.BOLD,
                        20
                )
        );

        amount.setTextFill(
                Color.web("#B9855A")
        );

        Label thanks = new Label(
                "付款方式："
                + paymentMethod
                + "\n謝謝您的訂購，請稍候取餐。"
        );

        thanks.setFont(
                Font.font(
                        "Microsoft JhengHei",
                        15
                )
        );

        thanks.setTextFill(
                Color.web("#6F4E37")
        );

        box.getChildren().addAll(
                title,
                amount,
                thanks
        );

        dialog.getDialogPane()
                .setContent(box);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.OK);

        ScaleTransition scale
                = new ScaleTransition(
                        Duration.millis(350),
                        box
                );

        scale.setFromX(0.85);

        scale.setFromY(0.85);

        scale.setToX(1.0);

        scale.setToY(1.0);

        FadeTransition fade
                = new FadeTransition(
                        Duration.millis(350),
                        box
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        dialog.setOnShown(e -> {

            scale.play();

            fade.play();
        });

        dialog.showAndWait();
    }

    private boolean saveOrderTo(
            double totalAmount,
            ObservableList<OrderDetailEntry> orderDetails
    ) {

        try {

            LocalDateTime now
                    = LocalDateTime.now();

            String orderId = "ord-"
                    + now.format(
                            DateTimeFormatter.ofPattern(
                                    "yyyyMMdd-HHmmss"
                            )
                    );

            SaleOrder saleOrder
                    = new SaleOrder();

            saleOrder.setOrderId(orderId);

            saleOrder.setOrderDate(now);

            saleOrder.setTotalAmount(totalAmount);

            saleOrder.setCustomerId(
                    "customer-101"
            );

            OrderEntryDAO dao = new OrderEntryDAO();

            boolean success
                    = dao.insertSaleOrder(saleOrder);

            if (success) {

                for (OrderDetailEntry item
                        : orderDetails) {

                    OrderDetail detail
                            = new OrderDetail();

                    detail.setOrderId(orderId);

                    detail.setProductId(
                            item.getId()
                    );

                    detail.setQuantity(
                            item.getQuantity()
                    );

                    boolean detailSuccess
                            = dao.insertOrderDetail(detail);

                    if (!detailSuccess) {

                        System.err.println(
                                "Save Failed : "
                                + item.getId()
                        );
                    }
                }
            }

            return success;

        } catch (Exception e) {

            System.err.println(
                    "Save Error : "
                    + e.getMessage()
            );

            return false;
        }
    }
}
