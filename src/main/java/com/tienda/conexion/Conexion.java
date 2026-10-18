
package com.tienda.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/tienda";

    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(
                    URL, USUARIO, CLAVE
            );
        } catch (SQLException e) {
            System.err.println(
                    "Error al conectar con MySQL: "
                    + e.getMessage()
            );
            return null;
        }
    }
}