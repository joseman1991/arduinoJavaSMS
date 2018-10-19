/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class ConexionDB {

    protected String url;
    protected String usuario;
    protected String clave;

    protected PreparedStatement sentencia;
    protected ResultSet resultado;
    protected CallableStatement procedimiento;

    protected Connection conexion;

    public ConexionDB() {

    }

    protected void abrirConexion() throws SQLException {
        try {
            String host = "localhost";
            String puerto = "5432";
            String baseDatos = "arduinosms";
            usuario = "postgres";
            clave = "sistema";
            url = "jdbc:postgresql://" + host + ":" + puerto + "/" + baseDatos;
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, usuario, clave);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void cerrarConexion() throws SQLException {
        conexion.close();
    }

    protected boolean validarCedula(String cedula) {
        int suma = 0;
        char[] arrayCed = cedula.toCharArray();
        int n;
        int verificador = Integer.parseInt(arrayCed[9] + "");
        try {
            for (int i = 0; i < arrayCed.length - 1; i++) {
                char c = arrayCed[i];
                n = Integer.parseInt(c + "");

                if (i % 2 == 0) {
                    n *= 2;

                    if (n > 9) {
                        n -= 9;
                    }
                }
                suma += n;
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        suma %= 10;        
        if (suma != 0) {
            suma = 10 - suma;
        }
        return verificador == suma;
    }

    public Connection getConexion() {
        return conexion;
    }

    
    
    
}
