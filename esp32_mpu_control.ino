#include <Wire.h>
#include <Adafruit_MPU6050.h>
#include <Adafruit_Sensor.h>

Adafruit_MPU6050 mpu;
const float RAD_TO_DEG_PER_SEC = 57.2957795;

void setup() {
  Serial.begin(115200);
  while (!Serial) {
    delay(10);
  }

  if (!mpu.begin()) {
    Serial.println("Falha ao iniciar MPU6050");
    while (1) {
      delay(10);
    }
  }

  mpu.setAccelerometerRange(MPU6050_RANGE_8_G);
  mpu.setGyroRange(MPU6050_RANGE_500_DEG);
  mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);

  Serial.println("MPU pronto");
}

void loop() {
  sensors_event_t accel;
  sensors_event_t gyro;
  sensors_event_t temp;
  mpu.getEvent(&accel, &gyro, &temp);

  Serial.print(accel.acceleration.x, 4);
  Serial.print(",");
  Serial.print(accel.acceleration.y, 4);
  Serial.print(",");
  Serial.print(accel.acceleration.z, 4);
  Serial.print(",");
  Serial.print(gyro.gyro.x * RAD_TO_DEG_PER_SEC, 4);
  Serial.print(",");
  Serial.print(gyro.gyro.y * RAD_TO_DEG_PER_SEC, 4);
  Serial.print(",");
  Serial.println(gyro.gyro.z * RAD_TO_DEG_PER_SEC, 4);

  delay(50);
}
