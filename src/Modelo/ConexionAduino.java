package Modelo;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class ConexionAduino {

    private final PanamaHitek_Arduino arduino = new PanamaHitek_Arduino();
    private String puerto;
    private int serial;

    
    
    private final SerialPortEventListener evento;

    public ConexionAduino(SerialPortEventListener evento) {
        this.evento=evento ;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public void iniciaArduino() throws ArduinoException {
        arduino.arduinoRXTX(puerto, serial, evento);      
    }

    public void enviarDatos(String dato) throws ArduinoException, SerialPortException {
        arduino.sendData(dato);        
    } 

    public PanamaHitek_Arduino getArduino() {
        return arduino;        
    }

    
    
}
