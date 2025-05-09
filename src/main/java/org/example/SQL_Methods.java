package org.example;

import java.util.List;

public interface SQL_Methods {

    void connect();

    void READ();

    void GETbyId(int id, List<String> columns);

    void READ(List<String> columns);

    void UPDATE(int id, List<String> columns, List<String> values);

    void DELETE(int id);

}
