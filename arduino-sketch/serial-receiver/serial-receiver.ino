const int red = 2;
const int green = 3;

void setup() {
  // put your setup code here, to run once:
  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  Serial.begin(9600);
  Serial.println("ready");
}

void loop() {
  // put your main code here, to run repeatedly:
  while (Serial.available() > 1) {
    //int led = Serial.parseInt();
    int led = Serial.read() - 48;
    int state = Serial.read() - 48;
    Serial.print("received led: ");
    Serial.print(led);
    Serial.print(" state: ");
    Serial.print(state);
    Serial.print(" | ");  
    handleSerial(led, state);
  }

  
}

void handleSerial(int led, int state) {
  if (state == 1) {
    digitalWrite(led, HIGH);
    Serial.print(led);
    Serial.print(" high");
    Serial.print("\n");
  } else {
    digitalWrite(led, LOW);
    Serial.print(led);
    Serial.print(" low");
    Serial.print("\n");
  }
}

