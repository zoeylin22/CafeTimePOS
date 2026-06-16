package app_product_management;

import data_type.Product;
import db.ProductDAO;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class AppProductMaintenance {

    private final ProductDAO productDAO = new ProductDAO();
    private final TableView<Product> table = new TableView<>();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    public VBox getRootPane() {

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));

        TextField txtId = new TextField();
        txtId.setPromptText("商品編號");

        TextField txtCategory = new TextField();
        txtCategory.setPromptText("分類");

        TextField txtName = new TextField();
        txtName.setPromptText("商品名稱");

        TextField txtPrice = new TextField();
        txtPrice.setPromptText("價格");

        TextField txtImgUrl = new TextField();
        txtImgUrl.setPromptText("圖片路徑");

        TextField txtDescription = new TextField();
        txtDescription.setPromptText("商品描述");

        setupTable();
        loadProducts();

        Button btnAdd = new Button("新增商品");
        Button btnUpdate = new Button("修改商品");
        Button btnDelete = new Button("刪除商品");

        btnAdd.setOnAction(e -> {
            Product p = new Product(
                    txtId.getText(),
                    txtCategory.getText(),
                    txtName.getText(),
                    Integer.parseInt(txtPrice.getText()),
                    txtImgUrl.getText(),
                    txtDescription.getText()
            );
            productDAO.insert(p);
            loadProducts();
            clearFields(txtId, txtCategory, txtName, txtPrice, txtImgUrl, txtDescription);
        });

        btnUpdate.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                Product p = new Product(
                        txtId.getText(),
                        txtCategory.getText(),
                        txtName.getText(),
                        Integer.parseInt(txtPrice.getText()),
                        txtImgUrl.getText(),
                        txtDescription.getText()
                );
                productDAO.update(p);
                loadProducts();
                clearFields(txtId, txtCategory, txtName, txtPrice, txtImgUrl, txtDescription);
            }
        });

        btnDelete.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                productDAO.delete(selected.getProductId());
                loadProducts();
                clearFields(txtId, txtCategory, txtName, txtPrice, txtImgUrl, txtDescription);
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                txtId.setText(selected.getProductId());
                txtCategory.setText(selected.getCategory());
                txtName.setText(selected.getName());
                txtPrice.setText(String.valueOf(selected.getPrice()));
                txtImgUrl.setText(selected.getImgUrl());
                txtDescription.setText(selected.getDescription());
            }
        });

        VBox formBox = new VBox(8);
        formBox.getChildren().addAll(
                txtId,
                txtCategory,
                txtName,
                txtPrice,
                txtImgUrl,
                txtDescription,
                btnAdd,
                btnUpdate,
                btnDelete
        );

        root.getChildren().addAll(table, formBox);

        return root;
    }

    private void setupTable() {

        TableColumn<Product, String> colId = new TableColumn<>("商品編號");
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<Product, String> colCategory = new TableColumn<>("分類");
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, String> colName = new TableColumn<>("名稱");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> colPrice = new TableColumn<>("價格");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, String> colImg = new TableColumn<>("圖片路徑");
        colImg.setCellValueFactory(new PropertyValueFactory<>("imgUrl"));

        TableColumn<Product, String> colDescription = new TableColumn<>("描述");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().addAll(
                colId,
                colCategory,
                colName,
                colPrice,
                colImg,
                colDescription
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(350);
        table.setItems(productList);
    }

    private void loadProducts() {

        List<Product> products = productDAO.getProducts();

        productList.clear();
        productList.addAll(products);
    }

    private void clearFields(TextField... fields) {

        for (TextField field : fields) {
            field.clear();
        }
    }
}