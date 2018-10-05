package com.eospy;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import com.eospy.events.EventReader;
import com.eospy.model.DevicesList;
import com.eospy.ui.MainWindow;
import com.eospy.util.URLConnection;

/**
 * Executive Order Corporation we make Things Smart
 *
 * EOSpy AI-IoT :: Internet of Things Drools-jBPM Expert System using EOSpy Arduino Tron AI-IoT Processing
 * Arduino Tron :: EOSPY-Executive Order Sensor Processor System - Arduino Tron MQTT AI-IoT Client using EOSpy AI-IoT Drools-jBPM
 * Executive Order Corporation - Arduino Tron - Arduino ESP8266 MQTT Telemetry Transport Machine-to-Machine(M2M)/Internet of Things(IoT)
 *
 * Executive Order Corporation
 * Copyright � 1978, 2018: Executive Order Corporation, All Rights Reserved
 */

/**
 * This is the main class for EOSpy AI-IoT Drools-jBPM Expert System
 */
public class EOSpy_AI_IoT {

	EOSpy_AI_IoT eospy_ai_iot;

	private String base_path = "";
	private String appVer = "1.01A";
	private String buildDate = "0304";
	private boolean is64bitJMV = false;
	private boolean knowledgeDebug = false;

	private int eventSleepTimer = 2000;
	private String kSessionName = "ksession-movement";
	private String processID = "com.TrainMovement";
	private String arduinoURL = "http://10.0.0.2";
	private String serverEvent = "C:\\Program Files\\EOSpy Server\\eospy-server event.log";

	public EOSpy_AI_IoT(String[] args) {

		this.eospy_ai_iot = this;
		System.out.println("EOSpy AI-IoT :: Internet of Things Drools-jBPM Expert System"
				+ " using EOSpy Arduino Tron AI-IoT Processing -version: " + appVer + " (" + buildDate + ")");

		getIPAddress();
		readProperties();

		if (knowledgeDebug) {
			System.out.println("os.name: " + System.getProperty("os.name"));
			System.out.println("os.arch: " + System.getProperty("os.arch"));
			is64bitJMV = (System.getProperty("os.arch").indexOf("64") != -1);
			String result = (is64bitJMV == true) ? "64bit" : "32bit";

			System.out.println("java.home: " + System.getProperty("java.home"));
			System.out.println("java.vendor: " + System.getProperty("java.vendor"));
			System.out.println("java.version: " + System.getProperty("java.version") + " " + result);
			long maxHeapBytes = Runtime.getRuntime().maxMemory();
			System.out.println("Max heap memory: " + maxHeapBytes / 1024L / 1024L + "M");
			System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));
			System.out.println("user.home: " + System.getProperty("user.home"));

			base_path = (System.getProperty("user.home") + File.separator);

			System.out.println("base_path: " + base_path);
			System.out.println("File.separator: " + File.separator);
			System.out.println("Local language: " + Locale.getDefault().getLanguage());
		}
	}

	public void readProperties() {
		try {
			File file = new File("eospy.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration<?> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				if (knowledgeDebug) {
					System.out.println(key + "=" + value);
				}

				if (key.indexOf("serverEvent") != -1) {
					serverEvent = value;
				}
				if (key.indexOf("eventSleepTimer") != -1) {
					String eventSleepTimerStr = value;
					eventSleepTimer = Integer.parseInt(eventSleepTimerStr);
					if (eventSleepTimer < 100) {
						eventSleepTimer = 100;
					}
				}

				if (key.indexOf("kSessionName") != -1) {
					kSessionName = value;
				}
				if (key.indexOf("processID") != -1) {
					processID = value;
				}
				if (key.indexOf("arduinoURL") != -1) {
					arduinoURL = value;
				}
				if (key.equals("knowledgeDebug")) {
					if (value.indexOf("true") != -1) {
						knowledgeDebug = true;
					}
					return;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(final boolean exitOnClose) {
		// set up and show main window
		Locale.setDefault(Locale.US);
		final DevicesList devices = new DevicesList();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow(devices.getDevices(), exitOnClose);
					window.show(); // .setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		new URLConnection(arduinoURL, knowledgeDebug);
		ProcessjBPMRules eospy = new ProcessjBPMRules(devices, kSessionName, processID, knowledgeDebug);
		EventReader source = new EventReader(eospy, devices, serverEvent, eventSleepTimer);
		source.StartEventThread();
	}

	public void getIPAddress() {
		// Returns the instance of InetAddress containing local host name and address
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		System.out.print("System IP: " + (localhost.getHostAddress()).trim());

		// Find public IP address
		String systemipaddress = "";
		try {
			URL url_name = new URL("http://bot.whatismyipaddress.com");
			BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));

			// reads system IPAddress
			systemipaddress = sc.readLine().trim();
		} catch (Exception e) {
			systemipaddress = "Cannot Execute Properly";
		}
		System.out.println("  Public IP: " + systemipaddress);
	}

	public static void main(String[] args) {
		System.out.println("Arduino Tron :: EOSPY-Executive Order Sensor Processor System"
				+ " - Arduino Tron MQTT AI-IoT Client using EOSpy AI-IoT Drools-jBPM");

		new EOSpy_AI_IoT(args).init(true);
	}
}
