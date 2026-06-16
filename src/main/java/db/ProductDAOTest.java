package db;

import data_type.Product;
import java.util.List;

public class ProductDAOTest {

    public static void main(String[] args) {

        DBInitializer initializer = new DBInitializer();
        initializer.setupTables();

        ProductDAO dao = new ProductDAO();

        List<Product> products = dao.getProducts();

        System.out.println("商品資料如下：");

        for (Product p : products) {
            System.out.println(
                    p.getProductId() + ", "
                    + p.getCategory() + ", "
                    + p.getName() + ", "
                    + p.getPrice()
            );
        }
    }
}