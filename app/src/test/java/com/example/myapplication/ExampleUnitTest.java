package com.example.myapplication;

import static com.example.myapplication.allConstant.Allconstant.URL_SERVER;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.Assert.*;

import com.example.myapplication.apiClass.RetrofitClient;
import com.example.myapplication.apiService.ApiService;
import com.example.myapplication.model.UtilisateurModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public static void main(String[] args) {
        String poiFilePath = "path/to/your/file.poi";
        String sqliteDbPath = "path/to/your/database.db";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteDbPath)) {
            createTable(connection);
            insertPois(connection, poiFilePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS poi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitude DOUBLE," +
                "longitude DOUBLE," +
                "name TEXT," +
                "description TEXT)";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        }
    }

    private static void insertPois(Connection connection, String poiFilePath) {
        try (PoiReader poiReader = new PoiReader(poiFilePath)) {
            Poi poi;
            String insertSQL = "INSERT INTO poi (latitude, longitude, name, description) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                while ((poi = poiReader.readPoi()) != null) {
                    stmt.setDouble(1, poi.getLatitude());
                    stmt.setDouble(2, poi.getLongitude());
                    stmt.setString(3, poi.getName());
                    stmt.setString(4, poi.getDescription());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}