/********************
  - Executive Order Corp - Arduino Tron - Arduino ESP8266 MQTT Telemetry Transport Machine-to-Machine(M2M)/Internet of Things(IoT)
  - Arduino Tron :: EOSPY-Executive Order Sensor Processor sYstem - Arduino Tron MQTT AI-IoT Client using EOSpy AI-IoT Drools-jBPM
  - Executive Order Corporation
  - Copyright © 1978, 2018: Executive Order Corporation, All Rights Reserved
********************/

#include <SimpleDHT.h>

#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

#define LED0 D0 // NodeMCU pin GPIO16 (D0)
#define LED1 D1 // NodeMCU pin GPIO5 (D1)
#define LED2 D2 // NodeMCU pin GPIO4 (D2)
#define LED3 D3 // NodeMCU pin GPIO0 (D3)
#define LED4 D4 // NodeMCU pin GPIO2 (D4-onboard)
#define LED5 D5 // NodeMCU pin GPIO14 (D5)
#define LED6 D6 // NodeMCU pin GPIO12 (D6)
#define LED7 D7 // NodeMCU pin GPIO13 (D7)
#define LED8 D8 // NodeMCU pin GPIO15 (D8)
#define LED9 D9 // NodeMCU pin GPIO3 (D9-RXD0)
#define LED10 D10 // NodeMCU pin GPIO1 (D10-TXD0)

#define BUTTON0 D0 // NodeMCU pin GPIO16 (D0)
#define BUTTON1 D1 // NodeMCU pin GPIO5 (D1)
#define BUTTON2 D2 // NodeMCU pin GPIO4 (D2)
#define BUTTON3 D3 // NodeMCU pin GPIO0 (D3)
#define BUTTON4 D4 // NodeMCU pin GPIO2 (D4)
#define BUTTON5 D5 // NodeMCU pin GPIO14 (D5)
#define BUTTON6 D6 // NodeMCU pin GPIO12 (D6)
#define BUTTON7 D7 // NodeMCU pin GPIO13 (D7)
#define BUTTON8 D8 // NodeMCU pin GPIO15 (D8)
#define BUTTON9 D9 // NodeMCU pin GPIO3 (D9-RXD0)
#define BUTTON10 D10 // NodeMCU pin GPIO1 (D10-TXD0)

// Update these with WiFi network values
const char* ssid     = "your-ssid"; //  your network SSID (name)
const char* password = "your-password"; // your network password

WiFiClient client;

// Update these with EOSpy service IP address and unique unit id values
byte server[] = { 10, 0, 0, 2 }; // Set EOSpy server IP address as bytes
String id = "334455"; // Device unique unit id

// Update these with LAT/LON GPS position values
// You can find LAT/LON from an address https://www.latlong.net/convert-address-to-lat-long.html
String lat = "38.888160"; // position LAT National Air and Space Museum - 600 Independence Ave SW, Washington, DC 20560
String lon = "-77.019868"; // position LON

// Above are all the fields you need to provide values, the remaining fields are used in the Arduino_Tron application
const bool readPushButton = true; // Values for the digitalRead value from gpiox button
const bool readDHT11Temp = false; // Values for the DHT11 digital temperature/humidity sensor

const int httpPort = 5055; // OsmAnd server is running on default port 5055
// OpenStreetMap Automated Navigation Directions is a map and navigation app for Android

// Values ?id=334455&timestamp=1521212240&lat=38.888160&lon=-77.019868&speed=0.0&bearing=0.0&altitude=0.0&accuracy=0.0&batt=99.8
String timestamp = "1521212240"; // timestamp
String speeds = "0.0";
String bearing = "0.0";
String altitude = "0.0";
String accuracy = "0.0"; // position accuracy
String batt = "99.8"; // battery value

// Additional fields to send - &temp=0.0&ir_temp=0.0&humidity=0.0&mbar=0.0&light=0.0&outdated=false&valid=true&motion=false
// &accel_x=-0.00&accel_y=-0.00&accel_z=0.00&gyro_x=0.0&gyro_y=-0.0&gyro_z=-0.0&magnet_x=-0.00&magnet_y=-0.00&magnet_z=0.00

// Values for the DHT11 digital temperature/humidity sensor; &temp= and &humidity= fields
String temp = "0.0";
String humidity = "0.0";

// Values to send in &textMessage= filed
String textMessage = "text_message";

// Values to send in &keypress= field
const String TYPE_KEYPRESS_1 = "1.0"; // keyPress_1
const String TYPE_KEYPRESS_2 = "2.0"; // keyPress_2
const String TYPE_REED_RELAY = "4.0"; // reedRelay
const String TYPE_PROXIMITY = "8.0"; // proximity

// Values to send in &alarm= field
const String ALARM_GENERAL = "general";
const String ALARM_SOS = "sos";
const String ALARM_VIBRATION = "vibration";
const String ALARM_MOVEMENT = "movement";
const String ALARM_LOW_SPEED = "lowspeed";
const String ALARM_OVERSPEED = "overspeed";
const String ALARM_FALL_DOWN = "fallDown";
const String ALARM_LOW_POWER = "lowPower";
const String ALARM_LOW_BATTERY = "lowBattery";
const String ALARM_FAULT = "fault";
const String ALARM_POWER_OFF = "powerOff";
const String ALARM_POWER_ON = "powerOn";
const String ALARM_DOOR = "door";
const String ALARM_GEOFENCE = "geofence";
const String ALARM_GEOFENCE_ENTER = "geofenceEnter";
const String ALARM_GEOFENCE_EXIT = "geofenceExit";
const String ALARM_GPS_ANTENNA_CUT = "gpsAntennaCut";
const String ALARM_ACCIDENT = "accident";
const String ALARM_TOW = "tow";
const String ALARM_ACCELERATION = "hardAcceleration";
const String ALARM_BRAKING = "hardBraking";
const String ALARM_CORNERING = "hardCornering";
const String ALARM_FATIGUE_DRIVING = "fatigueDriving";
const String ALARM_POWER_CUT = "powerCut";
const String ALARM_POWER_RESTORED = "powerRestored";
const String ALARM_JAMMING = "jamming";
const String ALARM_TEMPERATURE = "temperature";
const String ALARM_PARKING = "parking";
const String ALARM_SHOCK = "shock";
const String ALARM_BONNET = "bonnet";
const String ALARM_FOOT_BRAKE = "footBrake";
const String ALARM_OIL_LEAK = "oilLeak";
const String ALARM_TAMPERING = "tampering";
const String ALARM_REMOVING = "removing";

String ver = "1.03E";
int loopCounter = 1; // loop counter
int timeCounter = 301; // time counter
int switchState = 0; // digitalRead value from gpiox button

// Arduino Time Sync from NTP Server using ESP8266 WiFi module
unsigned int localPort = 2390; // local port to listen for UDP packets
const char* ntpServerName = "time.nist.gov";
IPAddress timeServerIP;

const int NTP_PACKET_SIZE = 48;
byte packetBuffer[NTP_PACKET_SIZE];
WiFiUDP udp;

// DHT11 digital temperature and humidity sensor pin Vout (sense)
int pinDHT11 = 2;
SimpleDHT11 dht11;

// Required for LIGHT_SLEEP_T delay mode
extern "C" {
#include "user_interface.h"
}

void setup(void) {
  pinMode(LED1, OUTPUT); // LED pin as output
  digitalWrite(LED1, LOW); // turn the LED off

  pinMode(BUTTON5, INPUT); // Declaring Arduino Pins as an Input
  pinMode(BUTTON6, INPUT);
  pinMode(BUTTON7, INPUT);
  pinMode(BUTTON8, INPUT);

  // Arduino IDE Serial Monitor window to see what EOSPy code is doing
  Serial.begin(115200); // Serial connection from ESP-01 via 3.3v console cable

  Serial.println("Executive Order Corp - Arduino Tron - Arduino ESP8266 MQTT Telemetry Transport Machine-to-Machine(M2M)/Internet of Things(IoT)");
  Serial.println("Arduino Tron :: EOSPY-Executive Order Sensor Processor sYstem - Arduino Tron MQTT AI-IoT Client using EOSpy AI-IoT Drools-jBPM");
  Serial.println("- Arduino Tron Sensor ver " + ver);
  Serial.println("Copyright © 1978, 2018: Executive Order Corporation, All Rights Reserved");
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
}

void loop(void)
{
  timeCounter++;
  if (timeCounter > 300) {
    timeCounter = 0;
    ntpdervertime(); // get NTP time every 30 seconds
    if (readDHT11Temp) {
      readdht11(); // read DHT11 digital temperature and humidity sensor
      eospysend();
    }
  }

  switchState = 0;
  delay(100); // waits for tenth of a second

  if (readPushButton) {
    if (digitalRead(BUTTON5) == HIGH) // read the pushButton state
    {
      switchState = 5;
    }
    if (digitalRead(BUTTON6) == HIGH)
    {
      switchState = 6;
    }
    if (digitalRead(BUTTON7) == HIGH)
    {
      switchState = 7;
    }
    if (digitalRead(BUTTON8) == HIGH)
    {
      switchState = 8;
    }
    if (switchState != 0)
    {
      eospysend();
    }
  }
}

void eospysend()
{
  digitalWrite(LED1, HIGH); // turn the LED on

  // Explicitly set the ESP8266 to be a WiFi-client
  WiFi.mode(WIFI_STA);

  // Connect to WiFi network
  WiFi.begin(ssid, password);
  Serial.print("\n\r \n\rExecutive Order Corp - Arduino Tron - Arduino ESP8266 MQTT Telemetry Transport Machine-to-Machine(M2M)/Internet of Things(IoT) ");
  Serial.println(timestamp);

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(200);
    Serial.print(".");
  }
  Serial.print("Connected to ");
  Serial.print(ssid);
  Serial.print(" IP address: ");
  Serial.print(WiFi.localIP());
  Serial.print(" ESP8266 Chip Id ");
  Serial.print(ESP.getChipId());
  Serial.print(" gpio ");
  Serial.print(switchState);
  Serial.print(" loop ");
  Serial.println(loopCounter);
  loopCounter++;

  if (!client.connect(server, httpPort)) { // http server is running on default port 5055
    Serial.print("Connection Failed Status: ");
    Serial.println(WiFi.status());
    return;
  }

  Serial.println("Connected");
  client.print("POST /?id=" + id);
  client.print("&timestamp=" + timestamp);
  client.print("&lat=" + lat);
  client.print("&lon=" + lon);
  client.print("&speed=" + speeds);
  client.print("&bearing=" + bearing);
  client.print("&altitude=" + altitude);
  client.print("&accuracy=" + accuracy);
  client.print("&batt=" + batt);

  if (readDHT11Temp) {
    client.print("&temp=" + temp);
    client.print("&humidity=" + humidity);
  }

  if (switchState == 5) // send GPS location and message for keyPress_1 (1.0)
  {
    client.print("&keypress=" + TYPE_KEYPRESS_1);
  }
  if (switchState == 6)
  {
    client.print("&keypress=" + TYPE_KEYPRESS_2);
  }
  if (switchState == 7)
  {
    textMessage = "Temperature_Server_Room";
    client.print("&textMessage=" + textMessage);
    client.print("&alarm=" + ALARM_TEMPERATURE);
  }
  if (switchState == 8) // digitalRead GPIO15(D8) send values for textMessage, keypress and alarm
  {
    textMessage = "Security_Alarm_Movement";
    client.print("&textMessage=" + textMessage);
    client.print("&keypress=" + TYPE_PROXIMITY);
    client.print("&alarm=" + ALARM_MOVEMENT);
  }

  client.println(" HTTP/1.1");

  client.println("User-Agent: Arduino_Tron ESP8266 MQTT Telemetry Transport EOSpy, ver " + ver);
  client.println("Connection: Keep-Alive");
  client.println("Accept-Encoding: gzip");

  client.println(); // empty line for apache server

  int i = 0;
  // Wait up to 10 seconds for server to respond then read response
  while ((!client.available()) && (i < 1000)) {
    delay(10);
    i++;
  }

  while (client.available())
  {
    String Line = client.readStringUntil('\r');
    Serial.print(Line);
  }
  client.stop();

  Serial.println();
  Serial.print("Connection Status: ");
  Serial.println(WiFi.status());
  // WL_NO_SHIELD = 255
  // WL_IDLE_STATUS = 0
  // WL_NO_SSID_AVAIL = 1
  // WL_SCAN_COMPLETED = 2
  // WL_CONNECTED = 3
  // WL_CONNECT_FAILED = 4
  // WL_CONNECTION_LOST = 5
  // WL_DISCONNECTED = 6

  // WiFi.disconnect(); // DO NOT DISCONNECT WIFI IF YOU WANT TO LOWER YOUR POWER DURING LIGHT_SLEEP_T DELLAY !
  // wifi_set_sleep_type(LIGHT_SLEEP_T);
  digitalWrite(LED1, LOW); // turn the LED off
}

void readdht11() {
  byte temperature = 0;
  byte _humidity = 0;
  int error = SimpleDHTErrSuccess;

  if ((error = dht11.read(pinDHT11, &temperature, &_humidity, NULL)) != SimpleDHTErrSuccess) {
    Serial.print("Read DHT11 failed, error = ");
    Serial.println(error);
    return;
  }

  // convert to Fahrenheit
  float temperatureF = (temperature * 9.0 / 5.0) + 32.0;
  temp = String(temperatureF);
  humidity = String(_humidity);
}

void ntpdervertime()
{
  WiFi.begin(ssid, password);

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(200);
    Serial.print(".");
  }

  udp.begin(localPort);
  WiFi.hostByName(ntpServerName, timeServerIP);

  sendntppacket(timeServerIP);
  delay(1000);

  int cb = udp.parsePacket();
  if (!cb) {
    delay(1);
  }
  else {
    udp.read(packetBuffer, NTP_PACKET_SIZE); // read the packet into the buffer
    unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
    unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);
    unsigned long secsSince1900 = highWord << 16 | lowWord;
    const unsigned long seventyYears = 2208988800UL;
    unsigned long epoch = secsSince1900 - seventyYears;
    // Serial.print("UNX");
    // Serial.println(epoch);
    timestamp = String(epoch);
    Serial.print(",");
  }
  udp.stop();
}

unsigned long sendntppacket(IPAddress& address)
{
  // Serial.println("Sending NTP packet...");
  memset(packetBuffer, 0, NTP_PACKET_SIZE);
  packetBuffer[0] = 0b11100011; // LI, Version, Mode
  packetBuffer[1] = 0; // Stratum, or type of clock
  packetBuffer[2] = 6; // Polling Interval
  packetBuffer[3] = 0xEC; // Peer Clock Precision
  packetBuffer[12]  = 49;
  packetBuffer[13]  = 0x4E;
  packetBuffer[14]  = 49;
  packetBuffer[15]  = 52;
  udp.beginPacket(address, 123);
  udp.write(packetBuffer, NTP_PACKET_SIZE);
  udp.endPacket();
}

