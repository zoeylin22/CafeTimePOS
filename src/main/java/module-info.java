module mypos {

    requires javafx.controls;
    requires java.sql;

    exports db;

    exports mypos;
    exports data_type;
    exports app_product_management;

    exports app_order_entry_system;
    exports app_main;
}