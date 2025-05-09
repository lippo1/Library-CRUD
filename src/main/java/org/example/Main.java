package org.example;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Library library = new Library();
        library.connect();

        /*Library.INSERT("Entendendo algoritmos",
                "Bhargava",
                "Informática",
                "Manning",
                2020);

         */
        List<String> headers = new ArrayList<>();
        headers.add("book_name");
        headers.add("author");
        headers.add("department");
        headers.add("editor");

        List<String> values = new ArrayList<>();
        values.add("Cálculo para Leigos");
        values.add("Mark Ryan");
        values.add("Math, Stats or Physics");
        values.add("Alta books");


        library.GETbyId(3, headers);

    }
}