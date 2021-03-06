package no.kristiania;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductDao {
    private final DataSource dataSource;

    public ProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Passing in sql statement - INSERT INTO - to insert data
    public void insertProduct(Product product) throws SQLException {
        // Make connection to database
        try (Connection connection = dataSource.getConnection()) {
            // Create statement and execute it
            try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO products (product_name, price) values (?, ?)")) {
                insertStatement.setString(1, product.getProductName());
                insertStatement.setInt(2, product.getProductPrice());
                insertStatement.executeUpdate();
            }
        }
    }

    // List all products in Database
    // Passing in sql statement - SELECT * - loop through result of select and return a List with all products
    public List<String> list() throws SQLException {
        // Make connection to database
        try (Connection connection = dataSource.getConnection()) {
            // Create statement
            try (PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM products")) {
                // Execute statement and store result in variable
                try (ResultSet res = selectStatement.executeQuery()) {
                    List<String> products = new ArrayList<>();
                    // Loop through result of sql query and put each element into a list
                    while(res.next()){
                        // TODO Make different method to format a better output response
                        // Example output
                        String product = "Product: " + res.getString("product_name") + " - Price: " + res.getString("price");
                        products.add(product);
                    }
                    return products;
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/pizza");
        dataSource.setUser("dbtestmagnus");
        dataSource.setPassword("pizza123"); // Password should be in a separate file !!

        ProductDao db = new ProductDao(dataSource);
        Product product = new Product();

        System.out.println("Add new product");

        // Create scanner to take input from user
        Scanner scanner = new Scanner(System.in);
        String productName = scanner.nextLine();

        System.out.println("Add price for the product");
        int productPrice = scanner.nextInt();

        product.setProductName(productName);
        product.setProductPrice(productPrice);

        // Add input from user to database
        db.insertProduct(product);

        // Display products from database
        System.out.println(db.list());
    }
}
