package db;

import data_type.OrderDetail;
import data_type.SaleOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderEntryDAO {

    // 新增訂單主檔
    public boolean insertSaleOrder(SaleOrder saleOrder) {

        String sql = "INSERT INTO SaleOrder "
                + "(order_id, order_date, total_amount, customer_id) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, saleOrder.getOrderId());
            pstmt.setString(2, saleOrder.getOrderDate().toString());
            pstmt.setDouble(3, saleOrder.getTotalAmount());
            pstmt.setString(4, saleOrder.getCustomerId());

            pstmt.executeUpdate();

            System.out.println("訂單主檔新增成功: " + saleOrder.getOrderId());

            return true;

        } catch (SQLException e) {

            System.err.println("訂單主檔新增失敗: " + e.getMessage());

            return false;
        }
    }

    // 新增訂單明細
    public boolean insertOrderDetail(OrderDetail detail) {

        String sql = "INSERT INTO OrderDetail "
                + "(order_id, product_id, quantity) "
                + "VALUES (?, ?, ?)";

        try (
                Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, detail.getOrderId());
            pstmt.setString(2, detail.getProductId());
            pstmt.setInt(3, detail.getQuantity());

            pstmt.executeUpdate();

            System.out.println("訂單明細新增成功: " + detail.getProductId());

            return true;

        } catch (SQLException e) {

            System.err.println("訂單明細新增失敗: " + e.getMessage());

            return false;
        }
    }
}