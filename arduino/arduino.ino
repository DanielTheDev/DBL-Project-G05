#include <Wire.h>
#define DEVICE_ID 0x08

int rgbSensor[3] = {999, 12,222};
int lightSensor = 400;

void setup() {
  Serial.begin(9600);
  Wire.begin(DEVICE_ID); 
  Wire.onRequest(requestEvent);
}

void requestEvent() {
  for(int x = 0; x < 3; x++) {
    writeShort(rgbSensor[x]);
  }
  writeShort(lightSensor);
}

void writeShort(int val) {
  Serial.println((val & 0x0000FF00) >> 8);
  Serial.println(val & 0x000000FF);
  Wire.write((byte)((val & 0x0000FF00) >> 8)); 
  Wire.write((byte)(val & 0x000000FF));  
}

void loop() {}
