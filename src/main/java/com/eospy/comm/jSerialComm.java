package com.eospy.comm;

/**
 * 
 * Open a connection to a serial device and then interact with it (receiving data and sending data). 
 * One thing to note is that the package gnu.io is used instead of javax.comm, 
 * though other than the change in package name the API follows the Java Communication API. 
 * To find the names of the available ports, see the Discovering comm ports example.
 * 
 * This varies from the other 'Two Way Communication' example in that this uses an event to trigger the reading. 
 * One advantage of this approach is that you are not having to poll the device to see if data is available.
 * 
 * Note: Make sure that you call the notifyOnDataAvailable() method of the SerialPort with a boolean true parameter. 
 * Based on my experience with RXTX, 
 * just registering to the SerialPort as a SerialPortEventListener is not enough -- the event will not be propagated unless you do this.
 * 
 * Note2: When all is done be sure to unregister the listener ( method removeEventListener()). 
 * otherwise its possible that your program hangs during exiting causing the serial port to be blocked. 
 * 
 */

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.eospy.EOSpyGPS;
import com.eospy.ui.EOSpyUI;

public class jSerialComm {

	private SerialPort commPort;
	private String commString = "";

	public jSerialComm() {
	}

	public void openCommPort() {
		commPort = SerialPort.getCommPorts()[0];
		System.out.println("SerialPort: " + commPort);
		EOSpyGPS.portname = commPort.toString();
		EOSpyUI.updateCOMPort(commPort.toString());
		commPort.openPort();
		commPort.addDataListener(new SerialPortDataListener() {

			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;
				byte[] newData = new byte[commPort.bytesAvailable()];
				int numRead = commPort.readBytes(newData, newData.length);
				for (int i = 0; i < newData.length; ++i) {
					commString = commString + (char) newData[i];
					if ((char) newData[i] == '\n') {
					//	System.out.print(commString);
						EOSpyUI.commLine(commString);
						commString = "";
					}
				}
			//	System.out.println("Read " + numRead + " bytes");
			}
		});
	}

	public void closeCommPort() {
		commPort.removeDataListener();
		commPort.closePort();
	}
	
/*	

	// Retrieve available comms ports on your computer.
	// A CommPort is available if it is not being used by another application.
	public String[] CommListPorts() {
		String[] portList = new String[20];
		int portCount = 0;

		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();

			if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println(portIdentifier.getName());
				portList[portCount] = portIdentifier.getName();
				portCount++;
			}
		}
		return portList;
	}

	// windows - COM1
	// linux - /dev/term/a
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


	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	/*public static class SerialReader implements SerialPortEventListener {
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

				EOSpyUI.commLine(new String(buffer, 0, len));

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
	} */
}