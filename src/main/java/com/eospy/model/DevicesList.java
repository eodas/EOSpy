package com.eospy.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class to load and return the list of devices
 */
public class DevicesList {

	private Map<String, Devices> devices;

	public DevicesList() {

		this.devices = new HashMap<String, Devices>();
		this.devices.put("1", new Devices("Arduino Tron", "1"));
		this.devices.put("2", new Devices("Arduino NodeMCU", "2"));
		this.devices.put("3", new Devices("Arduino ESP8266", "3"));
		this.devices.put("4", new Devices("EOSpy TI-SensorTag", "4"));
		this.devices.put("5", new Devices("EOSpy TI-BLE Tag", "5"));
		this.devices.put("6", new Devices("EOSpy S6 Sensor", "6"));
		this.devices.put("7", new Devices("EOSpy S7 Android", "7"));
		this.devices.put("8", new Devices("EOSpy S8 Galaxy", "8"));
	}

	public Collection<Devices> getDevices() {
		return Collections.unmodifiableCollection(devices.values());
	}

	public Devices getDevice(String device) {
		return this.devices.get(device);
	}
}
