package app_order_entry_system;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data_type.Product;
import db.ProductDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * CafeTime 商品選單系統
 */
class ProductMenuPane extends VBox {

    private OrderPane orderPane;

private final ProductDAO productDAO
        = new ProductDAO();

private final String[] categories
        = productDAO.getCategories();

    private final Map<String, TilePane> menus
            = new TreeMap<>();

    private final VBox menuContainerPane
            = new VBox();

    public ProductMenuPane() {

        this.setSpacing(22);
        this.setPadding(new Insets(5, 10, 5, 10));
        this.setStyle("-fx-background-color: transparent;");

        Label pageTitle = new Label("請選擇餐點");

        pageTitle.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        30
                )
        );

        pageTitle.setTextFill(
                Color.web("#5C3D2E")
        );

        Label pageSubtitle = new Label();

        pageSubtitle.setFont(
                Font.font(
                        "Segoe UI",
                        14
                )
        );

        pageSubtitle.setTextFill(
                Color.web("#9B7B60")
        );

        VBox titleBox = new VBox(3);

        titleBox.getChildren().addAll(
                pageTitle,
                pageSubtitle
        );

        TilePane categoryContainer = new TilePane();

        categoryContainer.setHgap(15);
        categoryContainer.setVgap(15);

        for (String category : categories) {

            Button btn = new Button(category);

            btn.setPrefSize(145, 48);

            btn.setStyle(getCategoryButtonStyle());

            btn.setOnMouseEntered(e -> {
                btn.setStyle(getCategoryButtonHoverStyle());
            });

            btn.setOnMouseExited(e -> {
                btn.setStyle(getCategoryButtonStyle());
            });

            btn.setOnAction(event -> {

                menuContainerPane.getChildren().clear();

                menuContainerPane.getChildren().add(
                        menus.get(category)
                );
            });

            categoryContainer.getChildren().add(btn);

            menus.put(
                    category,
                    getProductCategoryMenu(category)
            );
        }

        if (categories.length > 0) {

            menuContainerPane.getChildren().add(
                    menus.get(categories[0])
            );
        }

        this.getChildren().addAll(
                titleBox,
                categoryContainer,
                menuContainerPane
        );
    }

    public void setOrderPane(OrderPane orderPane) {

        this.orderPane = orderPane;
    }

    private TilePane getProductCategoryMenu(
            String category
    ) {

        List<Product> products
        = productDAO.getProducts();

        TilePane categoryMenu = new TilePane();

        categoryMenu.setHgap(22);
        categoryMenu.setVgap(22);
        categoryMenu.setPrefColumns(3);

        for (Product product : products) {

            if (product.getCategory().equals(category)) {

                VBox productCard = new VBox();

                productCard.setAlignment(Pos.CENTER);
                productCard.setSpacing(12);
                productCard.setPadding(new Insets(18));

                productCard.setPrefSize(235, 330);

                productCard.setStyle(
                        "-fx-background-color: white;"
                        + "-fx-background-radius: 28;"
                        + "-fx-border-radius: 28;"
                        + "-fx-border-color: #EFE1D1;"
                        + "-fx-border-width: 1;"
                );

                try {

                    String imageUrl = product.getImgUrl();

                    Image img;

                    if (imageUrl.startsWith("http://")
                            || imageUrl.startsWith("https://")) {

                        img = new Image(imageUrl);

                    } else {

                        java.io.File file
                                = new java.io.File(imageUrl);

                        if (!file.exists()) {

                            throw new java.io.FileNotFoundException(
                                    "找不到檔案："
                                    + file.getAbsolutePath()
                            );
                        }

                        img = new Image(
                                file.toURI().toString()
                        );
                    }

                    if (img.isError()) {

                        throw new Exception(
                                "圖片載入失敗"
                        );
                    }

                    ImageView imgView
                            = new ImageView(img);

                    imgView.setFitWidth(170);
                    imgView.setFitHeight(135);
                    imgView.setPreserveRatio(true);

                    productCard.getChildren().add(imgView);

                } catch (Exception e) {

                    VBox placeholderBox = new VBox();

                    placeholderBox.setAlignment(Pos.CENTER);

                    placeholderBox.setPrefSize(170, 135);

                    placeholderBox.setStyle(
                            "-fx-background-color: #EFE7DD;"
                            + "-fx-background-radius: 22;"
                    );

                    Label noImage = new Label(
                            product.getName()
                    );

                    noImage.setWrapText(true);

                    noImage.setFont(
                            Font.font(
                                    "Segoe UI",
                                    FontWeight.BOLD,
                                    15
                            )
                    );

                    noImage.setTextFill(
                            Color.web("#6F4E37")
                    );

                    placeholderBox.getChildren().add(noImage);

                    productCard.getChildren().add(
                            placeholderBox
                    );
                }

                Label productName = new Label(
                        product.getName()
                );

                productName.setFont(
                        Font.font(
                                "Microsoft JhengHei",
                                FontWeight.BOLD,
                                19
                        )
                );

                productName.setTextFill(
                        Color.web("#3E2723")
                );

                productName.setWrapText(true);

                Label productDescription = new Label(
                        product.getDescription()
                );

                productDescription.setFont(
                        Font.font(
                                "Microsoft JhengHei",
                                13
                        )
                );

                productDescription.setTextFill(
                        Color.web("#8D6E63")
                );

                productDescription.setWrapText(true);

                productDescription.setMaxWidth(185);

                productDescription.setMinHeight(45);

                productDescription.setAlignment(Pos.CENTER);

                Label productPrice = new Label(
                        "NT$ " + product.getPrice()
                );

                productPrice.setFont(
                        Font.font(
                                "Segoe UI",
                                FontWeight.BOLD,
                                17
                        )
                );

                productPrice.setTextFill(
                        Color.web("#B9855A")
                );

                Button addBtn = new Button(
                        "加入購物車"
                );

                addBtn.setPrefWidth(160);

                addBtn.setMinHeight(42);

                addBtn.setStyle(
                        getAddButtonStyle()
                );

                addBtn.setOnMouseEntered(e -> {
                    addBtn.setStyle(
                            getAddButtonHoverStyle()
                    );
                });

                addBtn.setOnMouseExited(e -> {
                    addBtn.setStyle(
                            getAddButtonStyle()
                    );
                });

                addBtn.setOnAction(event -> {

                    // 甜點直接加入
                    if (product.getCategory().equals("甜點")) {

                        if (orderPane != null) {

                            orderPane.addToCart(product);
                        }

                    } else {

                        // 飲料跳出客製化選項
                        showOptionDialog(product);
                    }
                });

                productCard.getChildren().addAll(
                        productName,
                        productDescription,
                        productPrice,
                        addBtn
                );

                categoryMenu.getChildren().add(
                        productCard
                );
            }
        }

        return categoryMenu;
    }

    private void showOptionDialog(Product product) {

        Dialog<ButtonType> dialog
                = new Dialog<>();

        dialog.setTitle("客製化選項");

        Label title = new Label(
                product.getName()
        );

        title.setFont(
                Font.font(
                        "Microsoft JhengHei",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#5C3D2E")
        );

        Label tempLabel = new Label("冰熱選擇");

        tempLabel.setFont(
                Font.font(
                        "Microsoft JhengHei",
                        FontWeight.BOLD,
                        15
                )
        );

        ComboBox<String> tempBox
                = new ComboBox<>();

        tempBox.getItems().addAll(
                "冰",
                "熱"
        );

        tempBox.setValue("冰");

        tempBox.setPrefWidth(220);

        Label sugarLabel = new Label("糖度選擇");

        sugarLabel.setFont(
                Font.font(
                        "Microsoft JhengHei",
                        FontWeight.BOLD,
                        15
                )
        );

        ComboBox<String> sugarBox
                = new ComboBox<>();

        sugarBox.getItems().addAll(
                "正常糖",
                "半糖",
                "微糖",
                "無糖"
        );

        sugarBox.setValue("正常糖");

        sugarBox.setPrefWidth(220);

        VBox box = new VBox(12);

        box.setPadding(new Insets(15));

        box.getChildren().addAll(
                title,
                tempLabel,
                tempBox,
                sugarLabel,
                sugarBox
        );

        dialog.getDialogPane().setContent(box);

        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {

                String customizedName
                        = product.getName()
                        + " / "
                        + tempBox.getValue()
                        + " / "
                        + sugarBox.getValue();

                Product customizedProduct
                        = new Product(
                                product.getProductId()
                                + "-"
                                + tempBox.getValue()
                                + "-"
                                + sugarBox.getValue(),

                                product.getCategory(),

                                customizedName,

                                product.getPrice(),

                                product.getImgUrl(),

                                product.getDescription()
                        );

                if (orderPane != null) {

                    orderPane.addToCart(
                            customizedProduct
                    );
                }
            }
        });
    }

    private String getCategoryButtonStyle() {

        return "-fx-background-color: white;"
                + "-fx-text-fill: #5C3D2E;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 22;"
                + "-fx-border-color: #E1C7AC;"
                + "-fx-border-radius: 22;"
                + "-fx-cursor: hand;";
    }

    private String getCategoryButtonHoverStyle() {

        return "-fx-background-color: #C8A27A;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 22;"
                + "-fx-border-color: #C8A27A;"
                + "-fx-border-radius: 22;"
                + "-fx-cursor: hand;";
    }

    private String getAddButtonStyle() {

        return "-fx-background-color: #5C3D2E;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 20;"
                + "-fx-cursor: hand;";
    }

    private String getAddButtonHoverStyle() {

        return "-fx-background-color: #7A523D;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 20;"
                + "-fx-cursor: hand;";
    }
}