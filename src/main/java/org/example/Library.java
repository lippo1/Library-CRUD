package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.List;

public class Library implements SQL_Methods{

    static Connection connection;

    public void connect() {
        String url = "jdbc:postgresql://localhost:5432/library_management_db";
        String login = "postgres";
        String password = System.getenv("DB_PASSWORD");

        try {
            connection = DriverManager.getConnection(url, login, password);
            if (connection != null) {
                System.out.println("connection to the library table went successfully!");
            } else {
                System.out.println("Something went wrong!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void READ() {

        String sql = "SELECT * FROM books";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet res = preparedStatement.executeQuery();

            System.out.println("=-=-=-=-=-=-=-=-=-=-=" +
                             "\nLibrary Books" +
                             "\n=-=-=-=-=-=-=-=-=-=-=");

            while (res.next()) {
                int id = res.getInt("book_id");
                String bookName = res.getString("book_name");
                String author = res.getString("author");
                String department = res.getString("department");
                String editor = res.getString("editor");
                int year = res.getInt("book_year");
                int with_student_id = res.getInt("with_student_id");

                System.out.println( "ID: " +id+
                                    "\nBook: "+bookName+
                                    "\nAuthor: "+author+
                                    "\nDepartment: "+department+
                                    "\nEditor: "+editor+
                                    "\nYear: "+year+
                                    "\nWith studend ID: "+with_student_id+
                                    "\n=-=-=-=-=-=-=-=-=-=-=");
            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void GETbyId(int id, List<String> columns) {
        StringBuilder sql = new StringBuilder("SELECT ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM books WHERE book_id = ?");

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

    public  void READ(List<String> columns) {

        StringBuilder sql = new StringBuilder("SELECT ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM books");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void UPDATE(int id, int year) {

        String sql = "UPDATE books " +
                "SET year = (?) " +
                "WHERE id = (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
     }

    @Override
    public  void UPDATE(int id, List<String> columns, List<String> values) {

        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("Columns and values length must be equal!");
        }

        StringBuilder sql = new StringBuilder("UPDATE books SET ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i)).append(" = (?)");
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE book_id = ?");

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
    @Override
    public void DELETE(int id) {

        String sql = "DELETE FROM books " +
                "WHERE id = (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void INSERT(String name, String author, String department, String editor) {

        String sql = "INSERT INTO books" +
                "(book_name, author, department, editor)" +
                " VALUES (?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, department);
            preparedStatement.setString(4,editor);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void INSERT(String name, String author, String department, String editor, int year) {

        String sql = "INSERT INTO books" +
                "(book_name, author, department, editor, book_year)" +
                " VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, department);
            preparedStatement.setString(4,editor);
            preparedStatement.setInt(5, year);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
