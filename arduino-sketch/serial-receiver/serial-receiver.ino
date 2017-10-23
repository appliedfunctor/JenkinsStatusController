const int red = 2;
const int green = 3;
const int delayInterval = 250;
int state = 3;

//state 0 Failing build
//state 1 Passing build
//state 2 Build in progress
//state 3 Build unknown
String states[4] = { "Failed", "Passing", "Building", "Unknown" };

String getState(int state) {
  if(state > 3) {
     state = 3;
  }
  return states[state];
}

void setup() {
  // put your setup code here, to run once:
  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  Serial.begin(9600);
  Serial.println("ready");
}

void loop() {
  // put your main code here, to run repeatedly:
  while (Serial.available() > 0) {
    state = Serial.read() - 48;
    Serial.print("received state: ");
    Serial.print(getState(state));
    Serial.print("\n");
  }
  handleState();
}

void runBuildFailed() {
  digitalWrite(green, LOW);
  digitalWrite(red, HIGH);
}

void runBuildPassed() {
  digitalWrite(red, LOW);
  digitalWrite(green, HIGH);
}

void runBuilding() {
  runBuildFailed();
  delay(delayInterval);  
  runBuildPassed();
  delay(delayInterval);
}

void runUnknown() {
  digitalWrite(red, HIGH);
  digitalWrite(green, HIGH);
  delay(100);
  digitalWrite(red, LOW);
  digitalWrite(green, LOW);
  delay(2000);
}

void handleState() {
  if (state == 1) {
    runBuildPassed();
  } else if (state == 2) {
    runBuilding();
  } else if (state == 3) {
    runUnknown();
  } else {
    runBuildFailed();
  }
}

