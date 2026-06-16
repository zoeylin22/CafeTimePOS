package data_type;

/**
 * 訂單明細資料
 */
public class OrderDetail {

    private int id;
    private String orderId;
    private String productId;
    private int quantity;

    public OrderDetail() {
    }

    public OrderDetail(
            int id,
            String orderId,
            String productId,
            int quantity
    ) 
    {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderDetail(
            String orderId,
            String productId,
            int quantity
    ) 
    {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderDetail{"
                + "id=" + id
                + ", orderId='" + orderId + '\''
                + ", productId='" + productId + '\''
                + ", quantity=" + quantity
                + '}';
    }
}