/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.github.juan.tienda_barrio;

import com.tienda.vista.VentanaInventario;
import javax.swing.SwingUtilities;


public class Tienda_Barrio {

    public static void main(String[] args) {
        System.out.println("Iniciando ventana...");

        SwingUtilities.invokeLater(() -> {
            VentanaInventario ventana = new VentanaInventario();
            ventana.setVisible(true);
            System.out.println("Ventana creada y mostrada.");
        });
    }
}