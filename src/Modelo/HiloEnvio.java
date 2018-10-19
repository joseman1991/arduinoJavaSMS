/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import com.panamahitek.ArduinoException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jssc.SerialPortException;
import vista.Principal;
 


/**
 *
 * @author JOSE
 */
public class HiloEnvio implements Runnable {

    private final List<Telefonos> listaTelefonos;
    private boolean inicio;
    private ConexionAduino conArdAduino;

    public HiloEnvio(List<Telefonos> listaTelefonos) {
        this.listaTelefonos = listaTelefonos;

    }

    @Override
    public synchronized void run() {
        while (inicio) {
            synchronized (listaTelefonos) {
                if (listaTelefonos.isEmpty()) {
                    try {
                        System.out.println("Esperando Dato ok");
                        Principal.jButton1.setEnabled(true);
                        Principal.jLabel1.setText("LISTO PARA ENVIAR");
                        Principal.jLabel3.setText("");
                        Principal.jLabel2.setVisible(false);
                        listaTelefonos.wait();
                    } catch (InterruptedException ex) {
                        System.err.println(ex.getMessage());
                        System.out.println("Esperando Dato");
                    }
                }
                try {
                    Telefonos get = listaTelefonos.get(0);                    
                    Principal.jLabel3.setText(get.getCelular());
                    String dt = get.getCelular();
                    int j;
                    for (j = 0; j < dt.length(); j++) {
                    }
                    j = j * 20;
                    if (conArdAduino != null) {
                        conArdAduino.enviarDatos(get.getCelular());
                    }
                    listaTelefonos.remove(0);
                    TimeUnit.MILLISECONDS.sleep(j+7500);
                } catch (InterruptedException ex) {
                    System.out.println("Que sucede" + ex.getMessage());
                } catch (ArduinoException | SerialPortException ex) {
                    System.out.println("Que sucede?" + ex.getMessage());
                }
            }
        }
    }

    public void setConArdAduino(ConexionAduino conArdAduino) {
        this.conArdAduino = conArdAduino;
    }

    public void setInicio(boolean inicio) {
        this.inicio = inicio;
    }

}
