package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInitializer {

    public void setupTables() {

        setupProductTable();
        setupSaleOrderTable();
        setupOrderDetailTable();
        insertDefaultProducts();
    }

    private void setupProductTable() {

        String sql = "CREATE TABLE IF NOT EXISTS Product ("
                + "product_id TEXT PRIMARY KEY, "
                + "category TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "price INTEGER NOT NULL, "
                + "img_url TEXT, "
                + "description TEXT"
                + ")";

        try (Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("Product 資料表建立完成");

        } catch (SQLException e) {
            System.err.println("Product 資料表建立失敗: " + e.getMessage());
        }
    }

    private void setupSaleOrderTable() {

        String sql = "CREATE TABLE IF NOT EXISTS SaleOrder ("
                + "order_id TEXT PRIMARY KEY, "
                + "order_date TEXT, "
                + "total_amount REAL, "
                + "customer_id TEXT"
                + ")";

        try (Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("SaleOrder 資料表建立完成");

        } catch (SQLException e) {
            System.err.println("SaleOrder 資料表建立失敗: " + e.getMessage());
        }
    }

    private void setupOrderDetailTable() {

        String sql = "CREATE TABLE IF NOT EXISTS OrderDetail ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "order_id TEXT NOT NULL, "
                + "product_id TEXT NOT NULL, "
                + "quantity INTEGER NOT NULL"
                + ")";

        try (Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("OrderDetail 資料表建立完成");

        } catch (SQLException e) {
            System.err.println("OrderDetail 資料表建立失敗: " + e.getMessage());
        }
    }

    private void insertDefaultProducts() {

        String sql = "INSERT OR IGNORE INTO Product "
                + "(product_id, category, name, price, img_url, description) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String[][] products = {
            {"p-c-101", "咖啡", "美式咖啡", "80", "product_imgs/american.png", "經典黑咖啡，風味清爽，適合日常飲用"},
            {"p-c-102", "咖啡", "拿鐵咖啡", "100", "product_imgs/latte.png", "濃縮咖啡搭配香醇牛奶，口感滑順"},
            {"p-c-103", "咖啡", "卡布奇諾", "110", "product_imgs/cappuccino.png", "濃縮咖啡結合綿密奶泡，風味濃郁"},
            {"p-c-104", "咖啡", "濃縮咖啡", "90", "product_imgs/espresso.png", "小杯濃縮咖啡，呈現純粹咖啡香氣"},
            {"p-d-101", "甜點", "可頌", "60", "product_imgs/croissant.png", "外層酥脆、奶油香氣濃厚的經典點心"},
            {"p-d-102", "甜點", "起司蛋糕", "90", "product_imgs/cheesecake.png", "綿密濃郁的起司風味，適合搭配咖啡"},
            {"p-d-103", "甜點", "提拉米蘇", "100", "product_imgs/tiramisu.png", "帶有咖啡香氣的義式經典甜點"},
            {"p-t-101", "茶飲", "紅茶", "50", "product_imgs/blacktea.png", "茶香溫潤，清爽順口，適合不喝咖啡的顧客"}
        };

        try (Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String[] p : products) {
                pstmt.setString(1, p[0]);
                pstmt.setString(2, p[1]);
                pstmt.setString(3, p[2]);
                pstmt.setInt(4, Integer.parseInt(p[3]));
                pstmt.setString(5, p[4]);
                pstmt.setString(6, p[5]);
                pstmt.executeUpdate();
            }

            System.out.println("預設商品資料建立完成");

        } catch (SQLException e) {
            System.err.println("預設商品資料建立失敗: " + e.getMessage());
        }
    }
}