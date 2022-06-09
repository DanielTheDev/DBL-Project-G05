#include <TimerOne.h>
#include <Wire.h>

#define ECHO 8
#define TRIGGER 9
#define S0 3
#define S1 4
#define S2 5
#define S3 6
#define OUT 2
#define LED 7
#define DEVICE_ID 0x08

int distance = 0;
int g_count = 0;
int g_array[3];
int g_flag = 0;
float g_SF[3];
int randomInt = 0;

void requestEvent() {
  writeShort(randomInt);
  for (int i = 0; i < 3; i++) {
    writeShort(int(g_array[i] * g_SF[i]));
  }
  writeShort(distance);
}

void writeShort(int val) {
  Wire.write((byte)((val & 0x0000FF00) >> 8)); 
  Wire.write((byte)(val & 0x000000FF));  
}


void TSC_Init() {
  pinMode(S0, OUTPUT);
  pinMode(S1, OUTPUT);
  pinMode(S2, OUTPUT);
  pinMode(S3, OUTPUT);
  pinMode(OUT, INPUT);
  pinMode(LED, OUTPUT);
  digitalWrite(S0, LOW);
  digitalWrite(S1, HIGH);
  digitalWrite(LED,LOW);
}
// Select the filter color//
void TSC_FilterColor(int Level01, int Level02) {
  if (Level01 != 0)
    Level01 = HIGH;
  if (Level02 != 0)
    Level02 = HIGH;

  digitalWrite(S2, Level01);
  digitalWrite(S3, Level02);
}
void TSC_Count() {
  g_count++;
}
void TSC_Callback() {
  switch (g_flag) {
  case 0:
    TSC_WB(LOW, LOW);
    break;
  case 1:
    g_array[0] = g_count;
    TSC_WB(HIGH, HIGH);
    break;
  case 2:
    g_array[1] = g_count;
    TSC_WB(LOW, HIGH);
    break;
  case 3:
    g_array[2] = g_count;
    TSC_WB(HIGH, LOW);
    setRandomInt();
    break;
  default:
    g_count = 0;
    break;
  }
}
void setRandomInt() {
    int ran = 0;
    do {
      ran = random(0, 1000);
    } while(ran == randomInt);
    randomInt = ran;
}

void TSC_WB(int Level0, int Level1) //White Balance
{
  g_count = 0;
  g_flag++;
  TSC_FilterColor(Level0, Level1);
  Timer1.setPeriod(100000);
}
void setup() {
  Serial.begin(9600);
  pinMode(TRIGGER, OUTPUT);
  pinMode(ECHO, INPUT);
  Wire.begin(DEVICE_ID); 
  Wire.onRequest(requestEvent);
  TSC_Init();
  delay(100);
  delay(100);
  Timer1.initialize();
  Timer1.attachInterrupt(TSC_Callback);
  attachInterrupt(0, TSC_Count, RISING);
  delay(4000);
  g_SF[0] = 255.0 / g_array[0];
  g_SF[1] = 255.0 / g_array[1];
  g_SF[2] = 255.0 / g_array[2];
}
void loop() {
  g_flag = 0;
  delay(500);
  digitalWrite(TRIGGER, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIGGER, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIGGER, LOW);
  int duration = pulseIn(ECHO, HIGH);
  distance = (duration * 0.034 / 2)*10;
  Serial.println(distance);
}
