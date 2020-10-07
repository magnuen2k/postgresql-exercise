package no.kristiania;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DbAccess {
    private final DataSource dataSource;

    public DbAccess(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String productName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO products (product_name) values (?)")) {
                statement.setString(1, productName);
                statement.executeUpdate();
            }
        }
    }

    public List<String> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products")) {
                try (ResultSet res = statement.executeQuery()) {
                    List<String> products = new ArrayList<>();
                    while(res.next()){
                        products.add(res.getString("product_name"));
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
        dataSource.setPassword("pizza123");

        DbAccess db = new DbAccess(dataSource);

        System.out.println("Add new product");
        Scanner scanner = new Scanner(System.in);

        String productName = scanner.nextLine();

        db.insert(productName);
        System.out.println(db.list());
    }
}
