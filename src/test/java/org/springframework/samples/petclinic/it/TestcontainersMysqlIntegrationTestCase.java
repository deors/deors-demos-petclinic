package org.springframework.samples.petclinic.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Locale;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.samples.petclinic.jdbc.SimpleJdbcClinic;
import org.testcontainers.containers.MySQLContainer;

public class TestcontainersMysqlIntegrationTestCase {

    @ClassRule
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("petclinic")
            .withUsername("pc")
            .withPassword("pc");

    @Test
    public void loadsClinicDataFromMysqlContainer() throws Exception {
        initializeDatabase();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(mysql.getJdbcUrl());
        dataSource.setUsername(mysql.getUsername());
        dataSource.setPassword(mysql.getPassword());

        SimpleJdbcClinic clinic = new SimpleJdbcClinic();
        clinic.init(dataSource);

        assertEquals("Expected the MySQL container to expose the seeded vet data", 6, clinic.getVets().size());
        assertEquals("Expected the MySQL container to expose the seeded pet types", 6, clinic.getPetTypes().size());
        assertEquals("Expected the MySQL container to expose the seeded owner data", "Carlos",
                clinic.loadOwner(10).getFirstName());
    }

    private static void initializeDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
                Statement statement = connection.createStatement()) {
            connection.setCatalog("petclinic");
            executeSqlFile(statement, "db/mysql/initDB.txt");
            executeSqlFile(statement, "db/mysql/populateDB.txt");
        }
    }

    private static void executeSqlFile(Statement statement, String resourcePath) throws Exception {
        String sql = readSqlResource(resourcePath);
        for (String statementText : sql.split(";")) {
            String sanitized = statementText.trim();
            if (sanitized.isEmpty() || sanitized.startsWith("--") || sanitized.startsWith("/*")) {
                continue;
            }
            String upperCaseStatement = sanitized.toUpperCase(Locale.ENGLISH);
            if (upperCaseStatement.startsWith("CREATE DATABASE")
                    || upperCaseStatement.startsWith("CREATE USER")
                    || upperCaseStatement.startsWith("GRANT ")
                    || upperCaseStatement.startsWith("USE ")
                    || upperCaseStatement.startsWith("FLUSH ")) {
                continue;
            }
            statement.execute(sanitized);
        }
    }

    private static String readSqlResource(String resourcePath) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        assertNotNull("Missing test resource: " + resourcePath, inputStream);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            inputStream.close();
        }
    }
}
