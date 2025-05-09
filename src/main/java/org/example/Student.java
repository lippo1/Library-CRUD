package org.example;

import java.sql.*;
import java.util.List;

public class Student implements SQL_Methods{

    static Connection connection;


    public void connect() {
        String url = "jdbc:postgresql://localhost:5432/library_management_db";
        String login = "postgres";
        String password = System.getenv("DB_PASSWORD");

        try {
            connection = DriverManager.getConnection(url, login, password);
            if (connection != null) {
                System.out.println("connection to the student table went successfully!");
            } else {
                System.out.println("Something went wrong!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void READ(){

        String sql = "SELECT * FROM student";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet res = preparedStatement.executeQuery();

            System.out.println("=-=-=-=-=-=-=-=-=-=-=" +
                    "\nStudents" +
                    "\n=-=-=-=-=-=-=-=-=-=-=");

            while (res.next()) {
                int student_id = res.getInt("student_id");
                String student_name = res.getString("student_name");
                int age = res.getInt("age");

                System.out.println( "ID: " +student_id+
                        "\nName: "+student_name+
                        "\nAge: "+age+
                        "\n=-=-=-=-=-=-=-=-=-=-=");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void READ(List<String> columns) {


        StringBuilder sql = new StringBuilder("SELECT ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM student");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void GETbyId(int id, List<String> columns) {
        StringBuilder sql = new StringBuilder("SELECT ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM student WHERE student_id = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("ID: ").append(id);

                for (int i = 0; i < columns.size(); i++) {
                    sb.append("\n")
                            .append(columns.get(i))
                            .append(": ")
                            .append(res.getString(i + 1));
                }

                System.out.println(sb.toString());
            } else {
                System.out.println("It has found no register for ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error to query: ", e);
        }
    }
    public  void UPDATE(int id, List<String> columns, List<String> values) {

        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("Columns and values length must be equal!");
        }

        StringBuilder sql = new StringBuilder("UPDATE student SET ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i)).append(" = (?)");
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE student_id = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));
            }
            preparedStatement.setInt(values.size() + 1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error to update: " + e.getMessage());
        }
    }

    public void DELETE(int id) {

        String sql = "DELETE FROM student " +
                "WHERE id = (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void INSERT(String name, int age) {
        String sql = "INSERT INTO student VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
