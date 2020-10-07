package no.kristiania;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class DbAccessTest {

    @Test
    void shouldListSavedProducts() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:pizza;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(dataSource).load().migrate();

        DbAccess projectMemberDao = new DbAccess(dataSource);
        String projectMemberName = samplePersonName();
        Integer productPrice = samplePrice();
        projectMemberDao.insertProduct(projectMemberName, productPrice);
        assertThat(projectMemberDao.list())
                .contains("Product: " + projectMemberName + " - Price: " + productPrice);
    }

    private Integer samplePrice() {
        ArrayList<Integer> prices = new ArrayList<>();
        prices.add(32);
        prices.add(19);
        prices.add(63);
        prices.add(26);
        prices.add(45);

        Random r = new Random();

        return prices.get(r.nextInt(prices.size()));
    }

    public static String samplePersonName() {
        ArrayList<String> names = new ArrayList<>();
        names.add("Magnus");
        names.add("Kai");
        names.add("Daniel");
        names.add("Lauri");
        names.add("Stian");

        Random r = new Random();

        return names.get(r.nextInt(names.size()));
    }
}
