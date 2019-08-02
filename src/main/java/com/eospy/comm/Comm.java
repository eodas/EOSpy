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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.eospy.ui.EOSpyUI;

public class Comm {

	private SerialPort serialPort;

	public Comm() {
		super();
	}

	public String[] CommListPorts() {
		int count = 0;
		String[] portList = new String[20];

		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			portList[count] = portIdentifier.getName();
			System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
		}
		return portList;
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
				serialPort = (SerialPort) commPort;
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

	public void CommClose() {
		serialPort.close();
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

				EOSpyUI.commline(new String(buffer, 0, len));

			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

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
}