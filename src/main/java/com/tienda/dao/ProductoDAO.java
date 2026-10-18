/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.conexion.Conexion;
import com.tienda.model.Categoria;
import com.tienda.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Insertar un producto
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO producto "
                + "(nombre, precio, cantidad, id_categoria) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getPrecio());
                ps.setInt(3, p.getCantidad());
                ps.setInt(4, p.getCategoria().getIdCategoria());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: "
                    + e.getMessage());
            return false;
        }
    }

    // Listar todos los productos con su categorÃa
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.id_producto, p.nombre, p.precio, "
                + "p.cantidad, c.id_categoria, c.nombre AS categoria "
                + "FROM producto p "
                + "INNER JOIN categoria c "
                + "ON p.id_categoria = c.id_categoria";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return productos;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Categoria categoria = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("categoria")
                    );

                    Producto producto = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("cantidad"),
                            categoria
                    );

                    productos.add(producto);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: "
                    + e.getMessage());
        }

        return productos;
    }

    // Buscar un producto por su ID
    public Producto buscarPorId(int id) {
        String sql = "SELECT p.id_producto, p.nombre, p.precio, "
                + "p.cantidad, c.id_categoria, c.nombre AS categoria "
                + "FROM producto p "
                + "INNER JOIN categoria c "
                + "ON p.id_categoria = c.id_categoria "
                + "WHERE p.id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Categoria categoria = new Categoria(
                                rs.getInt("id_categoria"),
                                rs.getString("categoria")
                        );

                        return new Producto(
                                rs.getInt("id_producto"),
                                rs.getString("nombre"),
                                rs.getDouble("precio"),
                                rs.getInt("cantidad"),
                                categoria
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto: "
                    + e.getMessage());
        }

        return null;
    }

    // Actualizar un producto existente
    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, "
                + "cantidad = ?, id_categoria = ? "
                + "WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getPrecio());
                ps.setInt(3, p.getCantidad());
                ps.setInt(4, p.getCategoria().getIdCategoria());
                ps.setInt(5, p.getIdProducto());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: "
                    + e.getMessage());
            return false;
        }
    }

    // Eliminar un producto por su ID
    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: "
                    + e.getMessage());
            return false;
        }
    }
}