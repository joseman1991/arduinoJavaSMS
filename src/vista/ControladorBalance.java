/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import Modelo.LeerXLSX;
import Modelo.Telefonos;
import Modelo.TelefonosDAO;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
 
 
 

/**
 *
 * @author JOSE
 */
public class ControladorBalance {

     
    private final Balance balance;
    private DefaultTableModel dtm;
    private FileChooser fc;
    private File archivo;
    private final LeerXLSX lcsv;
    private final List<String[]> filas;
    private final List<Telefonos> listaArticulos;

    public ControladorBalance(Balance balance) {
        this.lcsv = new LeerXLSX();
        this.balance = balance;
        filas = lcsv.getFilas();
        listaArticulos = new ArrayList<>();
        balance.setTitle("Exportar artículos al sistema");
        eventos();

    }

    private void eventos() {
        String[] coumnas = {"Numeros"};
        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        dtm.setColumnIdentifiers(coumnas);        
        balance.tabla.setModel(dtm);
        balance.tabla.setRowHeight(25);
        balance.tabla.setGridColor(Color.black);
        balance.tabla.setShowGrid(true);
        int medida[] = {500};
        AnchoColumna ac = new AnchoColumna(this.balance.tabla, medida);
        eventoBotonBuscar();
        eventoGuardar();
        eventoCerrar();
        eventoActualizar();
        eventoBorrar();
    }

    public void InicializarBalance() {
        balance.setVisible(true);
    }

    private void cursorCargando(JDialog jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JDialog jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void eventoBotonBuscar() {
        balance.boton.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);
            FileFilter filter = new FileNameExtensionFilter("Archivo de Excel| *.xlxs", "xlsx");
            this.fc = new FileChooser();
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            int status = fc.showOpenDialog(balance);
            if (status == JFileChooser.APPROVE_OPTION) {
                archivo = fc.getSelectedFile();
                if (archivo != null) {
                    balance.nombre.setText(archivo.getAbsolutePath());
                    try {
                        lcsv.leerXLSX(archivo.getAbsolutePath());
                        System.out.println("aqui "+filas.size());
                        for (int i = 0; i < filas.size(); i++) {
                            String[] get = filas.get(i);                             
                            dtm.addRow(get);
                        }
                        llenarListaArticulos();
                        fc.LookAndFeel();
                        balance.actuallizar.setEnabled(true);
                        balance.eliminar.setEnabled(true);
                    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            cursorNomal(balance);
        });
    }

    private void eventoCerrar() {
        balance.cancelar.addActionListener((ActionEvent e) -> {
            balance.dispose();
        });
    }

    private void eventoGuardar() {

        balance.accion.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);
            if (listaArticulos.size() > 0) {
                Connection con = null;
                TelefonosDAO aO = new TelefonosDAO();
                int estado = 0;
                try {
                    estado = aO.insertarNumeros(listaArticulos);
                    con = aO.getConexion();
                    JOptionPane.showMessageDialog(balance, "Numeros de teléfonos registrados", "Listo", JOptionPane.INFORMATION_MESSAGE);
                    while (dtm.getRowCount() > 0) {
                        dtm.removeRow(0);
                    }
                    con.commit();
                    cursorNomal(balance);

                } catch (SQLException ex) {
                    if (ex.getSQLState().equals("23505")) {
                        JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (con != null) {
                        try {
                            con.rollback();
                        } catch (SQLException ex1) {
                            JOptionPane.showMessageDialog(balance, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        cursorNomal(balance);
                    }
                } finally {
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException ex1) {
                            JOptionPane.showMessageDialog(balance, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    cursorNomal(balance);
                }
            } else {
                JOptionPane.showMessageDialog(balance, "Ingresa al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
                cursorNomal(balance);
            }
        });
    }

    private void llenarListaArticulos() {
        listaArticulos.clear();
        for (int i = 0; i < filas.size(); i++) {
            String[] get = filas.get(i);
            Telefonos a = new Telefonos();             
            a.setCelular(get[0]);            
            try {                
                listaArticulos.add(a);
            } catch (HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(balance, "Error de tipo de datos en la fila " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                listaArticulos.clear();
                return;
            }
        }
        System.out.println("ok");
    }

    private void eventoActualizar() {
        balance.actuallizar.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);
            if (archivo != null) {
                try {
                    while (dtm.getRowCount() > 0) {
                        dtm.removeRow(0);
                    }
                    lcsv.leerXLSX(archivo.getAbsolutePath());
                    for (int i = 0; i < filas.size(); i++) {
                        String[] get = filas.get(i);
                        dtm.addRow(get);
                    }
                    llenarListaArticulos();
                    fc.LookAndFeel();
                } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            cursorNomal(balance);
        });
    }

    private void eventoBorrar() {

        balance.eliminar.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);

            if (archivo != null) {
                while (dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                balance.nombre.setText("");
                archivo = null;
                balance.eliminar.setEnabled(false);
                balance.actuallizar.setEnabled(false);
            }
            cursorNomal(balance);
        });
    }

    
    
    
}
