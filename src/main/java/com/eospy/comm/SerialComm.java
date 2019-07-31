package com.eospy.comm;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 */

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//import gps.GPSnmea;
//import model.DeviceEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialComm {

	public String portName = "COM6";

	public static SerialComm serialcomm;
//	public GPSnmea gpsnmea;
//	public DeviceEvent deviceEvent;
	
	public SerialComm() {
		super();
		serialcomm = this;
	}

	public void StartComm() {
		CommListPorts(); // List available comms ports on system

//		gpsnmea = new GPSnmea();
//		deviceEvent = new DeviceEvent();

		try {
			CommConnect(portName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CommListPorts() {
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
		}
	}

	public String getPortTypeName(int portType) {
		switch (portType) {
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		default:
			return "unknown type";
		}
	}

	public void CommConnect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialWriter(out))).start();

				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);
			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public void UpdateDevice(String line) {
/*		gpsnmea.parse(line); // GPS nmea.parse line
		
		// public double utc = 0; // UTC time status of position (hours/minutes/seconds/decimal seconds)
		deviceEvent.setLat(gpsnmea.position.lat); // lat Latitude
		deviceEvent.setLon(gpsnmea.position.lon); // lon Longitude

		// public int quality = 0; // position fix 0: Fix not available 1: GPS SPS mode refer to GPS quality table
		// public int satellites = 0; // sats Number of satellites in use. May be different to the number in view
		deviceEvent.setHdop(gpsnmea.position.hdop); // hdop Horizontal dilution of precision
		deviceEvent.setAltitude(gpsnmea.position.altitude); // altitude Antenna altitude above/below mean sea level
		// public double geoid = 0; // geoid - undulation - the relationship between the geoid ellipsoid
		deviceEvent.setSpeed(gpsnmea.position.speed); // speed Kn - Speed over ground, knots
		deviceEvent.setCourse(gpsnmea.position.course); // track true - Track made good, degrees True

		// public double gpsdate = 0; // device date - Date: dd/mm/yy
		// public double age = 0; // age Age of correction data (in seconds) - The maximum age limited 99 seconds.
		// public double stnID = 0; // stn ID Differential base station ID
		// public String modeMA = ""; // mode MA A = Automatic 2D/3D M = Manual, forced to operate in 2D or 3D
		// public int mode123 = 0; // mode 123 Mode: 1 = Fix not available; 2 = 2D; 3 = 3D

		// public String valid = ""; // data status - Data status: A = Data valid, V = Data invalid
		// public String message; // $GPTXT - message transfers various information on the receiver
		deviceEvent.setValid(gpsnmea.position.fixed); // valid - position fix as boolean refer to GPS quality table 	
		
		System.out.println(gpsnmea.position); */
	}
	
	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public static class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		@Override
		public void serialEvent(SerialPortEvent arg0) {
			int data;

			try {
				int len = 0;
				while ((data = in.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
				}
				serialcomm.UpdateDevice(new String(buffer, 0, len));
				// System.out.print(new String(buffer, 0, len));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		@Override
		public void run() {
			try {
				int c = 0;
				while ((c = System.in.read()) > -1) {
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SerialComm tsc = new SerialComm();
		tsc.StartComm();
	}
}