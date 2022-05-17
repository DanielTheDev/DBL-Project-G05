const int CLOCK = 3;
const int DATA = 2;

void setup() {
  Serial.begin(9600);
  pinMode(CLOCK, OUTPUT);
  pinMode(DATA, OUTPUT);
}

void loop() {
  Serial.println("send 0x01");
  writeCode(0x01);
  delay(1000);
  Serial.println("send 0x02");
  writeCode(0x02);
  delay(1000);
  Serial.println("send 0x03");
  writeCode(0x03);
  delay(1000);
  Serial.println("send 0x04");
  writeCode(0x04);
  delay(1000);
  Serial.println("done send sequence");
  delay(5000);
}

void writeCode(int opcode) {
  for(int t = 0; t < 2; t++) {
      digitalWrite(CLOCK, LOW);
      delay(10);
      Serial.println(((opcode >> t) & 0b00000001) == 1);
      digitalWrite(DATA, ((opcode >> t) & 0b00000001) == 1 ? HIGH : LOW);
      digitalWrite(CLOCK, HIGH);
      delay(10);
  }
}
