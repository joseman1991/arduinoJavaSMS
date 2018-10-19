
#include <SoftwareSerial.h>
SoftwareSerial SIM900(7, 8);

String numero;
int i = 0;

void setup() {

  // put your setup code here, to run once:
  //digitalWrite(9, HIGH); // Descomentar para activar la alimentación de la tarjeta por Software
  //delay(1000);
  //digitalWrite(9, LOW);
  pinMode(11,OUTPUT);
  digitalWrite(11,HIGH);
  delay (5000);  //Nos damos un tiempo para encender el GPRS y la alimentación de la tarjeta
  SIM900.begin(19200);  //Configura velocidad del puerto serie para el SIM900
  Serial.begin(19200);  //Configura velocidad del puerto serie del Arduino
  Serial.println("Iniciando modulo");  
  delay (1000);
  SIM900.println("AT+CPIN=\"XXXX\"");  //Comando AT para introducir el PIN de la tarjeta
  delay(25000);  //Tiempo para que encuentre una RED
  Serial.println("Esperando mensajes");
  digitalWrite(11,LOW);
}

 


void mensaje_sms(String numer)
{  
  Serial.println("Enviando SMS...");
  SIM900.print("AT+CMGF=1\r");  //Configura el modo texto para enviar o recibir mensajes
  delay(1000);
  String num = String("AT+CMGS=\"");
  num += numer;
  num += "\"";   
  SIM900.println(num);  //Numero al que vamos a enviar el mensaje
  delay(1000);  
  SIM900.println("Mensaje enviado desde JAVA usando Arduino");  // Texto del SMS
  digitalWrite(11,HIGH);
  delay(100);
  SIM900.println((char)26); //Comando de finalización ^Z
  delay(100);
  SIM900.println();
  delay(5000);  // Esperamos un tiempo para que envíe el SMS
  Serial.println("SMS enviado");
  digitalWrite(11,LOW);
}

void loop()
{
  while (Serial.available()) {
    char caracter = Serial.read();
    if (numero.length() < 10) {
      numero += caracter;
    } 
    delay(20);
  }
  if (numero.length() == 10) {
    Serial.println("El numero de celular es: " + numero+"\n");   
    mensaje_sms(numero); //mensaje       
    delay(100);    
    numero = "";
    i = 0;
  }
}


