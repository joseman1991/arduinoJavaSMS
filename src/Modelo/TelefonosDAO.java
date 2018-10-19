/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author JOSE-MA
 */
public class TelefonosDAO extends ConexionDB {

    public List<Telefonos> obtenerNumeros(List<Telefonos> lista) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from telefonos");
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Telefonos t = new Telefonos();
            t.setCelular(resultado.getString(1));
            lista.add(t);
        }
        cerrarConexion();
        return lista;
    }

    public int insertarNumeros(List<Telefonos> lista) throws SQLException {
        abrirConexion();
        conexion.setAutoCommit(false);
        sentencia = conexion.prepareStatement("insert into telefonos values(?);");
        int res = 0;
        for (int i = 0; i < lista.size(); i++) {
            Telefonos t = lista.get(i);
            sentencia.setString(1, t.getCelular());
            res = sentencia.executeUpdate();
        }         
        return res;
    }

}
