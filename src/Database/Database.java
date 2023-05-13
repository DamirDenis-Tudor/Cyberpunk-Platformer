package Database;

import Enums.ComponentType;
import Utils.RandomIdGenerator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is a rigid database implementation and is focused on the needs for this project.
 * Normally, this class should implement an interface of CRUD instructions.
 */
public class Database {
    private static Database instance = null;
    private Connection connection = null;
    private String oldestSave = "";
    private int currentNumberOfDisplayedSaves = 0;
    private String saveToBeLoaded = "";

    private Database() {
        try {

            Class.forName("org.sqlite.JDBC");
            String relativePath = Objects.requireNonNull(Database.class.getClassLoader().getResource("Database/CYBERPUNK_DATABASE.db")).getPath();
            connection = DriverManager.getConnection("jdbc:sqlite:" + relativePath);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static Database get() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createSavesTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SAVES " +
                    " (TIME TIMESTAMP," +
                    "SAVE_TABLE_NAME TEXT)";
            statement.execute(sql);
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void createSaveTable() {
        try {
            oldestSave = "SAVE_" + RandomIdGenerator.generate();
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + oldestSave +
                    " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "COMPONENT_TYPE INT NOT NULL," +
                    "SERIALIZED_FIELD BLOB NOT NULL)";
            statement.execute(sql);
            statement.close();

            Instant now = Instant.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            String formattedTimestamp = formatter.format(now.atOffset(ZoneOffset.UTC));
            sql = "INSERT INTO SAVES(TIME,SAVE_TABLE_NAME) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, formattedTimestamp);
            preparedStatement.setString(2, oldestSave);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void insertDataIntoSave(ComponentType componentType, byte[] data) {
        try {
            int maxNumberOfDisplayedSaves = 7;
            if (currentNumberOfDisplayedSaves == maxNumberOfDisplayedSaves) {
                deleteLastSave();
                currentNumberOfDisplayedSaves--;
            }
            String sql = "INSERT INTO " + oldestSave + " (COMPONENT_TYPE,SERIALIZED_FIELD) VALUES (?,?) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, componentType.ordinal());
            statement.setBytes(2, data);
            statement.execute();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public List<String> getAllSavesInfo() {
        List<String> saves = new ArrayList<>();
        try {
            String sql = "SELECT * FROM SAVES";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            currentNumberOfDisplayedSaves = 0;
            while (resultSet.next()) {
                currentNumberOfDisplayedSaves++;
                saves.add(resultSet.getString("SAVE_TABLE_NAME") + " - > " + resultSet.getString("TIME"));
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return saves;
    }

    public List<SerializedObject> getSerializedObjects() {
        List<SerializedObject> objects = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + saveToBeLoaded;
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                byte[] bytes = resultSet.getBytes("SERIALIZED_FIELD");
                objects.add(new SerializedObject(ComponentType.values()[resultSet.getInt("COMPONENT_TYPE")] , bytes));
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return objects;
    }

    public void deleteLastSave() {
        try {
            String sql = "SELECT * FROM SAVES";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            String save = resultSet.getString("SAVE_TABLE_NAME");
            sql = "DROP TABLE " + save;
            statement.execute(sql);
            sql = "DELETE FROM SAVES WHERE SAVE_TABLE_NAME = '" + save + "'";
            statement.executeUpdate(sql);

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void setSaveToBeLoaded(String save) {
        saveToBeLoaded = save;
    }

    public String getSaveToBeLoaded() {
        return saveToBeLoaded;
    }
}
