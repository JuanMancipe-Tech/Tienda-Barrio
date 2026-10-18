/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.vista;

import com.tienda.dao.CategoriaDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.model.Categoria;
import com.tienda.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaInventario extends JFrame {

    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;

    private JComboBox<Categoria> cmbCategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    private int idProductoSeleccionado = -1;

    public VentanaInventario() {
        setTitle("Inventario - Tienda de Don Ernesto");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
        cargarCategorias();
        cargarProductos();
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "SISTEMA DE INVENTARIO",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        // Formulario
        JPanel formulario = new JPanel(
                new GridLayout(4, 2, 8, 8)
        );
        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del producto")
        );

        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtCantidad = new JTextField();
        cmbCategoria = new JComboBox<>();

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Precio:"));
        formulario.add(txtPrecio);

        formulario.add(new JLabel("Cantidad:"));
        formulario.add(txtCantidad);

        formulario.add(new JLabel("Categoría:"));
        formulario.add(cmbCategoria);

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 10));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelIzquierdo.setPreferredSize(new Dimension(320, 0));
        panelIzquierdo.add(formulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        // Tabla de productos
        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID", "Nombre", "Precio", "Cantidad", "Categoría"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tablaProductos.getSelectedRow() != -1) {
                cargarProductoSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(
                BorderFactory.createTitledBorder("Productos registrados")
        );
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();

        List<Categoria> categorias = categoriaDAO.listar();

        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);

        List<Producto> productos = productoDAO.listar();

        for (Producto producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getCategoria().getNombre()
            });
        }
    }

    private Producto leerFormulario() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException(
                    "Escribe el nombre del producto."
            );
        }

        double precio;
        int cantidad;

        try {
            precio = Double.parseDouble(
                    txtPrecio.getText().trim().replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El precio debe ser un número válido."
            );
        }

        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser un número entero."
            );
        }

        if (precio <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero."
            );
        }

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad no puede ser negativa."
            );
        }

        Categoria categoria =
                (Categoria) cmbCategoria.getSelectedItem();

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "Selecciona una categoría."
            );
        }

        return new Producto(
                0, nombre, precio, cantidad, categoria
        );
    }

    private void guardarProducto() {
        try {
            Producto producto = leerFormulario();

            if (productoDAO.insertar(producto)) {
                JOptionPane.showMessageDialog(
                        this, "Producto guardado correctamente."
                );
                cargarProductos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(
                        this, "No se pudo guardar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(),
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void cargarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idProductoSeleccionado =
                Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

        txtNombre.setText(
                modeloTabla.getValueAt(fila, 1).toString()
        );
        txtPrecio.setText(
                modeloTabla.getValueAt(fila, 2).toString()
        );
        txtCantidad.setText(
                modeloTabla.getValueAt(fila, 3).toString()
        );

        String nombreCategoria =
                modeloTabla.getValueAt(fila, 4).toString();

        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Categoria categoria = cmbCategoria.getItemAt(i);

            if (categoria.getNombre().equals(nombreCategoria)) {
                cmbCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private void editarProducto() {
        if (idProductoSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona un producto de la tabla."
            );
            return;
        }

        try {
            Producto producto = leerFormulario();
            producto.setIdProducto(idProductoSeleccionado);

            if (productoDAO.actualizar(producto)) {
                JOptionPane.showMessageDialog(
                        this, "Producto actualizado correctamente."
                );
                cargarProductos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(
                        this, "No se pudo actualizar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(),
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void eliminarProducto() {
        if (idProductoSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona un producto de la tabla."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(idProductoSeleccionado)) {
                JOptionPane.showMessageDialog(
                        this, "Producto eliminado correctamente."
                );
                cargarProductos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(
                        this, "No se pudo eliminar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void limpiarFormulario() {
        idProductoSeleccionado = -1;

        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");

        tablaProductos.clearSelection();

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
    }
}