package com.eospy.model;

import java.util.Date;

/**
 * An event informing of a state change due to some operation
 */
public class ServerEvent {

	public String name;

	public String id;
	public String event;
	public String protocol;
	public Date serverTime;
	public Date deviceTime;
	public Date fixTime;
	public boolean outdated;
	public boolean valid;
	public double lat;
	public double lon;
	public double altitude; // value in meters
	public double speed; // value in knots
	public double course;
	public String address;
	public double accuracy;
	public String network;

	public double batteryLevel;
	public String textMessage;
	public double temp;
	public double ir_temp;
	public double humidity;
	public double mbar;
	public double accel_x;
	public double accel_y;
	public double accel_z;
	public double gyro_x;
	public double gyro_y;
	public double gyro_z;
	public double magnet_x;
	public double magnet_y;
	public double magnet_z;
	public double light;
	public double keypress;
	public String alarm;
	public double distance;
	public double totalDistance;
	public boolean motion;

	public ServerEvent() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Date getServerTime() {
		return serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}

	public Date getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Date deviceTime) {
		this.deviceTime = deviceTime;
	}

	public Date getFixTime() {
		return fixTime;
	}

	public void setFixTime(Date fixTime) {
		this.fixTime = fixTime;
	}

	public boolean isOutdated() {
		return outdated;
	}

	public void setOutdated(boolean outdated) {
		this.outdated = outdated;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getCourse() {
		return course;
	}

	public void setCourse(double course) {
		this.course = course;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public double getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(double batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getIr_temp() {
		return ir_temp;
	}

	public void setIr_temp(double ir_temp) {
		this.ir_temp = ir_temp;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getMbar() {
		return mbar;
	}

	public void setMbar(double mbar) {
		this.mbar = mbar;
	}

	public double getAccel_x() {
		return accel_x;
	}

	public void setAccel_x(double accel_x) {
		this.accel_x = accel_x;
	}

	public double getAccel_y() {
		return accel_y;
	}

	public void setAccel_y(double accel_y) {
		this.accel_y = accel_y;
	}

	public double getAccel_z() {
		return accel_z;
	}

	public void setAccel_z(double accel_z) {
		this.accel_z = accel_z;
	}

	public double getGyro_x() {
		return gyro_x;
	}

	public void setGyro_x(double gyro_x) {
		this.gyro_x = gyro_x;
	}

	public double getGyro_y() {
		return gyro_y;
	}

	public void setGyro_y(double gyro_y) {
		this.gyro_y = gyro_y;
	}

	public double getGyro_z() {
		return gyro_z;
	}

	public void setGyro_z(double gyro_z) {
		this.gyro_z = gyro_z;
	}

	public double getMagnet_x() {
		return magnet_x;
	}

	public void setMagnet_x(double magnet_x) {
		this.magnet_x = magnet_x;
	}

	public double getMagnet_y() {
		return magnet_y;
	}

	public void setMagnet_y(double magnet_y) {
		this.magnet_y = magnet_y;
	}

	public double getMagnet_z() {
		return magnet_z;
	}

	public void setMagnet_z(double magnet_z) {
		this.magnet_z = magnet_z;
	}

	public double getLight() {
		return light;
	}

	public void setLight(double light) {
		this.light = light;
	}

	public double getKeypress() {
		return keypress;
	}

	public void setKeypress(double keypress) {
		this.keypress = keypress;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public boolean isMotion() {
		return motion;
	}

	public void setMotion(boolean motion) {
		this.motion = motion;
	}

	@Override
	public String toString() {
		String s = name + "-" + event;
		if (alarm != null && !alarm.isEmpty()) {
			return s = s + "-" + alarm;
		}
		if (textMessage != null && !textMessage.isEmpty()) {
			return s = s + "-" + textMessage;
		}
		return s;
	}
}
