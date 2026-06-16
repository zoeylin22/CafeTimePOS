package db;

import data_type.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // 查詢全部商品
    public List<Product> getProducts() {

        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Product";

        try (
                Connection conn = DBConfig.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                list.add(
                        new Product(
                                rs.getString("product_id"),
                                rs.getString("category"),
                                rs.getString("name"),
                                rs.getInt("price"),
                                rs.getString("img_url"),
                                rs.getString("description")
                        )
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "查詢商品失敗: "
                    + e.getMessage()
            );
        }

        return list;
    }

    // 查詢所有商品分類
    public String[] getCategories() {

        List<Product> products = getProducts();

        java.util.TreeSet<String> categories = new java.util.TreeSet<>();

        for (Product p : products) {
            categories.add(p.getCategory());
        }

        return categories.toArray(new String[categories.size()]);
    }

    // 查詢單一商品
    public Product getProduct(String productId) {

        String sql
                = "SELECT * FROM Product "
                + "WHERE product_id = ?";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement pstmt
                = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                return new Product(
                        rs.getString("product_id"),
                        rs.getString("category"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("img_url"),
                        rs.getString("description")
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "查詢商品失敗: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // 新增商品
    public void insert(Product product) {

        String sql
                = "INSERT INTO Product "
                + "(product_id, category, name, "
                + "price, img_url, description) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement pstmt
                = conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    product.getProductId()
            );

            pstmt.setString(
                    2,
                    product.getCategory()
            );

            pstmt.setString(
                    3,
                    product.getName()
            );

            pstmt.setInt(
                    4,
                    product.getPrice()
            );

            pstmt.setString(
                    5,
                    product.getImgUrl()
            );

            pstmt.setString(
                    6,
                    product.getDescription()
            );

            pstmt.executeUpdate();

            System.out.println(
                    "新增商品成功: "
                    + product.getName()
            );

        } catch (SQLException e) {

            System.err.println(
                    "新增商品失敗: "
                    + e.getMessage()
            );
        }
    }

    // 更新商品
    public void update(Product product) {

        String sql
                = "UPDATE Product "
                + "SET category = ?, "
                + "name = ?, "
                + "price = ?, "
                + "img_url = ?, "
                + "description = ? "
                + "WHERE product_id = ?";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement pstmt
                = conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    product.getCategory()
            );

            pstmt.setString(
                    2,
                    product.getName()
            );

            pstmt.setInt(
                    3,
                    product.getPrice()
            );

            pstmt.setString(
                    4,
                    product.getImgUrl()
            );

            pstmt.setString(
                    5,
                    product.getDescription()
            );

            pstmt.setString(
                    6,
                    product.getProductId()
            );

            pstmt.executeUpdate();

            System.out.println(
                    "更新商品成功: "
                    + product.getProductId()
            );

        } catch (SQLException e) {

            System.err.println(
                    "更新商品失敗: "
                    + e.getMessage()
            );
        }
    }

    // 刪除商品
    public void delete(String productId) {

        String sql
                = "DELETE FROM Product "
                + "WHERE product_id = ?";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement pstmt
                = conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    productId
            );

            pstmt.executeUpdate();

            System.out.println(
                    "刪除商品成功: "
                    + productId
            );

        } catch (SQLException e) {

            System.err.println(
                    "刪除商品失敗: "
                    + e.getMessage()
            );
        }
    }
}
