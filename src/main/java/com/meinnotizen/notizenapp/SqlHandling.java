package com.meinnotizen.notizenapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class SqlHandling {
    private static SqlHandling sqlHandling = new SqlHandling();
    private  Connection con;

    private SqlHandling() {
    }

    public static SqlHandling getInstance() {
        return sqlHandling;
    }


    public Connection connect(String host, String user, String pass) {
        try {
            con = DriverManager.getConnection(host, user, pass);
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet query(String sqlStatement) {
        Statement prepStmt;
        ResultSet ergebnis;
        try {
            prepStmt = con.createStatement();
            ergebnis = prepStmt.executeQuery(sqlStatement);
            return ergebnis;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
