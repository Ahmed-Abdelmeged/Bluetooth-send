//call the library and define the rx and tx pins for send and receive
#include <SoftwareSerial.h>
SoftwareSerial bluetooth(10,11);  //RX,TX

//char used to store the input data 
char data=0;

//put a led in pin 5 in arduino
int led = 5;

void setup() {
  // put your setup code here, to run once:
//intainalize the bluetooth with 9600 boot rat 
Serial.begin(9600);
bluetooth.begin(9600);

//define the input and the output for the arduio
pinMode(led,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  
  //if the bluetooth is conncted
 if(bluetooth.available()){

  //read the incoming data 
  data = bluetooth.read();

  //perform an action based on the incoming data
  if(data == 'o'){
    digitalWrite(led,HIGH);
    }else if (data == 'f'){
      digitalWrite(led,LOW);
    }
  }
}
