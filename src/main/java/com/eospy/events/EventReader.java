package com.eospy.events;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eospy.ProcessjBPMRules;
import com.eospy.model.Devices;
import com.eospy.model.DevicesList;
import com.eospy.model.ServerEvent;

public class EventReader {

	private BufferedReader in;
	private volatile static boolean shutdown = false;
	private String DateFormatString = "yyy-mm-dd hh:mm:ss";

	private ProcessjBPMRules eospy;
	private DevicesList devices;
	private String serverEvent;
	private int eventSleepTimer;

	public EventReader(ProcessjBPMRules eospy, DevicesList devices, String serverEvent, int eventSleepTimer) {
		this.eospy = eospy;
		this.devices = devices;
		this.serverEvent = serverEvent;
		this.eventSleepTimer = eventSleepTimer;
	}

	public void StartEventThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				shutdown = false;
				while (!shutdown) {
					if (EventFileExist()) {
						EventFileRead();
						EventFileDelete();
					}
					try {
						Thread.sleep(eventSleepTimer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void shutdownEventThread() {
		shutdown = true;
	}

	public void EventFileDelete() {
		File file = new File(serverEvent);
		if (!file.delete()) {
			System.err.println("ERROR: Failed to delete the file " + serverEvent);
		}
	}

	private Date parseDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean EventFileExist() {
		File file = new File(serverEvent);
		if (file.exists() && !file.isDirectory()) {
			return true;
		}
		return false;
	}

	public void EventFileRead() {
		try {
			File file = new File(serverEvent);
			in = new BufferedReader(new FileReader(file));

			String line;
			while ((line = in.readLine()) != null) {
				EventParser(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void EventParser(String line) {
		if (line.length() < 50) {
			return;
		}
		ServerEvent serverevent = new ServerEvent();
		String[] tokens = line.split("&");

		for (String token : tokens) {
			try {
				String key = token.substring(0, token.indexOf('='));
				String value = token.substring(token.indexOf('=') + 1);

				switch (key) {
				case "id":
					serverevent.setId(value);
					Devices device = this.devices.getDevice(value);
					serverevent.setName(device.getName());
					break;
				case "event":
					serverevent.setEvent(value);
					break;
				case "protocol":
					serverevent.setProtocol(value);
					break;
				case "servertime":
					serverevent.setServerTime(parseDate(value, DateFormatString));
					break;
				case "timestamp":
					serverevent.setDeviceTime(parseDate(value, DateFormatString));
					break;
				case "fixtime":
					serverevent.setFixTime(parseDate(value, DateFormatString));
					break;
				case "outdated":
					serverevent.setOutdated(Boolean.parseBoolean(value));
					break;
				case "valid":
					serverevent.setValid(Boolean.parseBoolean(value));
					break;
				case "lat":
					serverevent.setLat(Double.parseDouble(value));
					break;
				case "lon":
					serverevent.setLon(Double.parseDouble(value));
					break;
				case "altitude":
					serverevent.setAltitude(Double.parseDouble(value));
					break;
				case "speed":
					serverevent.setSpeed(Double.parseDouble(value));
					break;
				case "course":
					serverevent.setCourse(Double.parseDouble(value));
					break;
				case "address":
					serverevent.setAddress(value);
					break;
				case "accuracy":
					serverevent.setAccuracy(Double.parseDouble(value));
					break;
				case "network":
					serverevent.setNetwork(value);
					break;
				case "batteryLevel":
					serverevent.setBatteryLevel(Double.parseDouble(value));
					break;
				case "textMessage":
					serverevent.setTextMessage(value);
					break;
				case "temp":
					serverevent.setTemp(Double.parseDouble(value));
					break;
				case "ir_temp":
					serverevent.setIr_temp(Double.parseDouble(value));
					break;
				case "humidity":
					serverevent.setHumidity(Double.parseDouble(value));
					break;
				case "mbar":
					serverevent.setMbar(Double.parseDouble(value));
					break;
				case "accel_x":
					serverevent.setAccel_x(Double.parseDouble(value));
					break;
				case "accel_y":
					serverevent.setAccel_y(Double.parseDouble(value));
					break;
				case "accel_z":
					serverevent.setAccel_z(Double.parseDouble(value));
					break;
				case "gyro_x":
					serverevent.setGyro_x(Double.parseDouble(value));
					break;
				case "gyro_y":
					serverevent.setGyro_y(Double.parseDouble(value));
					break;
				case "gyro_z":
					serverevent.setGyro_z(Double.parseDouble(value));
					break;
				case "magnet_x":
					serverevent.setMagnet_x(Double.parseDouble(value));
					break;
				case "magnet_y":
					serverevent.setMagnet_y(Double.parseDouble(value));
					break;
				case "magnet_z":
					serverevent.setMagnet_z(Double.parseDouble(value));
					break;
				case "light":
					serverevent.setLight(Double.parseDouble(value));
					break;
				case "keypress":
					serverevent.setKeypress(Double.parseDouble(value));
					break;
				case "alarm":
					serverevent.setAlarm(value);
					break;
				case "distance":
					serverevent.setDistance(Double.parseDouble(value));
					break;
				case "totalDistance":
					serverevent.setTotalDistance(Double.parseDouble(value));
					break;
				case "motion":
					serverevent.setMotion(Boolean.parseBoolean(value));
					break;

				default:
					System.err.println("ERROR: Unknown Token " + devices.getDevice(value) + " " + serverevent.getName()
							+ " " + token);
				}
			} catch (IndexOutOfBoundsException e) {
				System.err.println("=============================================================");
				System.err.println("Unexpected exception caught: " + e.getMessage());
				e.printStackTrace();
			}
		}
		eospy.receive(serverevent);
	}
}
