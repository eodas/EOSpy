package com.eospy.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.eospy.EOSpyGPS;
import com.eospy.comm.jSerialComm;
import com.eospy.gps.GPSnmea;
import com.eospy.ui.AboutDialog;
import com.eospy.util.WebBrowser;
import com.eospy.model.DeviceEvent;
import com.eospy.server.AgentConnect;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import java.awt.Color;

/**
 * Main window implementation for the EOSpy GPS AI-IoT example
 */
public class EOSpyUI {

	private JFrame gpsFrame;

	private jSerialComm comm;
	private GPSnmea gpsnmea;
	private DeviceEvent deviceEvent;
	private static EOSpyUI eospyui;

	// private JTextField textField_ID;
	// private JTextField textField_URL;
	private JTextField textField_Lat;
	private JTextField textField_Lon;
	// private JTextField textField_COMPort;
	private JTextField textField_ServerEvent;
	private static JTextField textField_FixStatus;

	private JLabel lblLabel_FixStatus;
	private JLabel lblLabel_Progress;
	private JLabel lblLabel_ServerStatus;
	// private JSpinner spinner_FreqInterval;
	private JToggleButton tglbtnServerToggleButton;

	private int freqInterval = 300;
	private long lastSendTime = 0;
	private int progressCount = 0;
	private int progressBar = 0;

	private String LatStr = "38.888160";
	private String LonStr = "-77.019868";
	private boolean serverService = false;
	private boolean lastfixed = false;

	public EOSpyUI(boolean exitOnClose) {
		this.gpsFrame = buildFrame(exitOnClose);

		eospyui = this;
		comm = new jSerialComm();
		gpsnmea = new GPSnmea();
		deviceEvent = new DeviceEvent();
	}

	// Initialize the contents of the frame
	private JFrame buildFrame(boolean exitOnClose) {
		gpsFrame = new JFrame();

		gpsFrame.setTitle("EOSpy GPS AI-IoT :: Internet of Things");
		gpsFrame.setBounds(100, 100, 408, 430);
		gpsFrame.setDefaultCloseOperation(exitOnClose ? JFrame.EXIT_ON_CLOSE : WindowConstants.DISPOSE_ON_CLOSE);

		gpsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 5, 234, -69, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 32, 17, 14, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 42, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gpsFrame.getContentPane().setLayout(gridBagLayout);

		textField_FixStatus = new JTextField();
		textField_FixStatus.setForeground(Color.WHITE);
		textField_FixStatus.setBackground(Color.RED);
		GridBagConstraints gbc_textField_FixStatus = new GridBagConstraints();
		gbc_textField_FixStatus.gridwidth = 4;
		gbc_textField_FixStatus.insets = new Insets(0, 0, 5, 0);
		gbc_textField_FixStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_FixStatus.gridx = 0;
		gbc_textField_FixStatus.gridy = 0;
		gpsFrame.getContentPane().add(textField_FixStatus, gbc_textField_FixStatus);
		textField_FixStatus.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Service Status");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		gpsFrame.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

		lblLabel_ServerStatus = new JLabel("Service Stopped");
		GridBagConstraints gbc_lblLabel_ServerStatus = new GridBagConstraints();
		gbc_lblLabel_ServerStatus.anchor = GridBagConstraints.NORTH;
		gbc_lblLabel_ServerStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLabel_ServerStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabel_ServerStatus.gridx = 1;
		gbc_lblLabel_ServerStatus.gridy = 2;
		gpsFrame.getContentPane().add(lblLabel_ServerStatus, gbc_lblLabel_ServerStatus);

		tglbtnServerToggleButton = new JToggleButton("Off");
		tglbtnServerToggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				serverServiceAction(arg0);
			}
		});
		GridBagConstraints gbc_tglbtnServerToggleButton = new GridBagConstraints();
		gbc_tglbtnServerToggleButton.anchor = GridBagConstraints.NORTHEAST;
		gbc_tglbtnServerToggleButton.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtnServerToggleButton.gridx = 2;
		gbc_tglbtnServerToggleButton.gridy = 2;
		gpsFrame.getContentPane().add(tglbtnServerToggleButton, gbc_tglbtnServerToggleButton);

		// JLabel lblNewLabel_3 = new JLabel("Device Identifier");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_3.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 4;
		// gpsFrame.getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);

		// textField_ID = new JTextField();
		// textField_ID.setText(EOSpy_GPS.id); // "100111");
		// textField_ID.setText("100111");
		GridBagConstraints gbc_textField_ID = new GridBagConstraints();
		gbc_textField_ID.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_ID.insets = new Insets(0, 0, 5, 5);
		gbc_textField_ID.gridx = 1;
		gbc_textField_ID.gridy = 5;

		// JLabel lblServerUrl = new JLabel("Server URL");
		GridBagConstraints gbc_lblServerUrl = new GridBagConstraints();
		gbc_lblServerUrl.anchor = GridBagConstraints.NORTH;
		gbc_lblServerUrl.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblServerUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerUrl.gridx = 1;
		gbc_lblServerUrl.gridy = 7;
		// gpsFrame.getContentPane().add(lblServerUrl, gbc_lblServerUrl);

		// JLabel lblNewLabel_2 = new JLabel("COM Port");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 7;
		// gpsFrame.getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);

		// textField_URL = new JTextField();
		// textField_URL.setText(EOSpy_GPS.server); // "http://10.0.0.2:5055");
		// textField_URL.setColumns(15);
		GridBagConstraints gbc_textField_URL = new GridBagConstraints();
		gbc_textField_URL.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_URL.insets = new Insets(0, 0, 5, 5);
		gbc_textField_URL.gridx = 1;
		gbc_textField_URL.gridy = 8;
		// gpsFrame.getContentPane().add(textField_URL, gbc_textField_URL);

		// textField_COMPort = new JTextField();
		// textField_COMPort.setText(EOSpy_GPS.portName); // "COM3");
		GridBagConstraints gbc_textField_COMPort = new GridBagConstraints();
		gbc_textField_COMPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_COMPort.insets = new Insets(0, 0, 5, 5);
		gbc_textField_COMPort.gridx = 2;
		gbc_textField_COMPort.gridy = 8;
		// gpsFrame.getContentPane().add(textField_COMPort, gbc_textField_COMPort);

		// JLabel lblNewLabel_11 = new JLabel("Tracking Server URL");
		GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
		gbc_lblNewLabel_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_11.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_11.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_11.gridx = 1;
		gbc_lblNewLabel_11.gridy = 9;

		// JLabel lblNewLabel_4 = new JLabel("Frequence");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 11;
		// gpsFrame.getContentPane().add(lblNewLabel_4, gbc_lblNewLabel_4);

		/* spinner_FreqInterval = new JSpinner();
		spinner_FreqInterval.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				freqIntervalSpinnerChanged(arg0);
			}
		}); */
		// spinner_FreqInterval.setModel(new SpinnerNumberModel(freqInterval, 1, 100000, 1));
		GridBagConstraints gbc_spinner_FreqInterval = new GridBagConstraints();
		gbc_spinner_FreqInterval.anchor = GridBagConstraints.NORTHWEST;
		gbc_spinner_FreqInterval.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_FreqInterval.gridx = 1;
		gbc_spinner_FreqInterval.gridy = 12;
		// gpsFrame.getContentPane().add(spinner_FreqInterval, gbc_spinner_FreqInterval);

		// JLabel lblNewLabel_5 = new JLabel("Reporting Interval in Seconds");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 13;
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLUE);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 1;
		gbc_separator_1.gridy = 3;
		gpsFrame.getContentPane().add(separator_1, gbc_separator_1);

		JLabel lblNewLabel_6 = new JLabel("Lat");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 1;
		gbc_lblNewLabel_6.gridy = 4;
		gpsFrame.getContentPane().add(lblNewLabel_6, gbc_lblNewLabel_6);

		textField_Lat = new JTextField();
		textField_Lat.setText(LatStr);
		textField_Lat.setColumns(10);
		GridBagConstraints gbc_textField_Lat = new GridBagConstraints();
		gbc_textField_Lat.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_Lat.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Lat.gridx = 1;
		gbc_textField_Lat.gridy = 5;
		gpsFrame.getContentPane().add(textField_Lat, gbc_textField_Lat);

		lblLabel_FixStatus = new JLabel("No Fix");
		lblLabel_FixStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLabel_FixStatus.setForeground(Color.RED);
		GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
		gbc_lblNewLabel_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_10.gridx = 2;
		gbc_lblNewLabel_10.gridy = 5;
		gpsFrame.getContentPane().add(lblLabel_FixStatus, gbc_lblNewLabel_10);

		JLabel lblNewLabel_10 = new JLabel("Lon");
		GridBagConstraints gbc_lblNewLabel_13 = new GridBagConstraints();
		gbc_lblNewLabel_13.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_13.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_13.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_13.gridx = 1;
		gbc_lblNewLabel_13.gridy = 6;
		gpsFrame.getContentPane().add(lblNewLabel_10, gbc_lblNewLabel_13);

		JLabel lblNewLabel_12 = new JLabel("Status");
		GridBagConstraints gbc_lblNewLabel_12 = new GridBagConstraints();
		gbc_lblNewLabel_12.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_12.gridx = 2;
		gbc_lblNewLabel_12.gridy = 6;
		gpsFrame.getContentPane().add(lblNewLabel_12, gbc_lblNewLabel_12);

		textField_Lon = new JTextField();
		textField_Lon.setText(LonStr);
		textField_Lon.setColumns(10);
		GridBagConstraints gbc_textField_Lon = new GridBagConstraints();
		gbc_textField_Lon.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_Lon.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Lon.gridx = 1;
		gbc_textField_Lon.gridy = 7;
		gpsFrame.getContentPane().add(textField_Lon, gbc_textField_Lon);

		lblLabel_Progress = new JLabel("@");
		lblLabel_Progress.setForeground(Color.BLUE);
		lblLabel_Progress.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblLabel_Progress = new GridBagConstraints();
		gbc_lblLabel_Progress.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLabel_Progress.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabel_Progress.gridx = 2;
		gbc_lblLabel_Progress.gridy = 7;
		gpsFrame.getContentPane().add(lblLabel_Progress, gbc_lblLabel_Progress);

		JLabel lblNewLabel_7 = new JLabel("GPS Lat / Lon Position Track Values");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 1;
		gbc_lblNewLabel_7.gridy = 8;
		gpsFrame.getContentPane().add(lblNewLabel_7, gbc_lblNewLabel_7);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.BLUE);
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.gridwidth = 2;
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 9;
		gpsFrame.getContentPane().add(separator_2, gbc_separator_2);

		JLabel lblNewLabel_8 = new JLabel("Server Event Model Data Fields");
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.gridx = 1;
		gbc_lblNewLabel_8.gridy = 10;
		gpsFrame.getContentPane().add(lblNewLabel_8, gbc_lblNewLabel_8);

		textField_ServerEvent = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.gridwidth = 2;
		gbc_textField_4.anchor = GridBagConstraints.NORTH;
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 11;
		gpsFrame.getContentPane().add(textField_ServerEvent, gbc_textField_4);
		textField_ServerEvent.setColumns(10);

		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.BLUE);
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_3.gridwidth = 2;
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 1;
		gbc_separator_3.gridy = 12;
		gpsFrame.getContentPane().add(separator_3, gbc_separator_3);

		JLabel lblNewLabel_9 = new JLabel("Key Press Server Alerts");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
		gbc_lblNewLabel_9.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_9.gridx = 1;
		gbc_lblNewLabel_9.gridy = 13;
		gpsFrame.getContentPane().add(lblNewLabel_9, gbc_lblNewLabel_9);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridheight = 2;
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 14;
		gpsFrame.getContentPane().add(panel_1, gbc_panel_1);

		JButton btnKey1 = new JButton("Key1");
		btnKey1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverSendPost("&keypress=1.0");
			}
		});
		btnKey1.setBounds(0, 11, 76, 23);
		panel_1.add(btnKey1);

		JButton btnKey2 = new JButton("Key2");
		btnKey2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverSendPost("&keypress=2.0");
			}
		});
		btnKey2.setBounds(90, 11, 76, 23);
		panel_1.add(btnKey2);

		JButton btnReed = new JButton("Reed");
		btnReed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverSendPost("&keypress=4.0");
			}
		});
		btnReed.setBounds(178, 11, 76, 23);
		panel_1.add(btnReed);

		JButton btnProx = new JButton("Prox");
		btnProx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverSendPost("&keypress=8.0");
			}
		});
		btnProx.setBounds(267, 11, 76, 23);
		panel_1.add(btnProx);

		JButton Map = new JButton("Map");
		Map.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPositionAction(e);
			}
		});
		Map.setBounds(0, 45, 76, 23);
		panel_1.add(Map);

		JButton Help = new JButton("Help");
		Help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpContentsAction(e);
			}
		});
		Help.setBounds(90, 45, 76, 23);
		panel_1.add(Help);

		JButton About = new JButton("GPS");
		About.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutIoTBPMAction(e);
			}
		});
		About.setBounds(178, 45, 76, 23);
		panel_1.add(About);

		JButton Exit = new JButton("Exit");
		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		Exit.setBounds(267, 45, 76, 23);
		panel_1.add(Exit);

		showServerService();

		return gpsFrame;
	}

	void showServerService() {
		if (serverService) {
			lblLabel_ServerStatus.setText("Service Started");
			tglbtnServerToggleButton.setText("On");
		} else {
			lblLabel_ServerStatus.setText("Service Stopped");
			tglbtnServerToggleButton.setText("Off");

			lblLabel_FixStatus.setText("No Fix");
			lblLabel_FixStatus.setForeground(Color.RED);
			textField_FixStatus.setBackground(Color.RED);
		}
	}

	void showFixStatus(boolean fixed) {
		if (lastfixed == fixed) {
			return;
		}
		if (fixed) {
			lblLabel_FixStatus.setText("Good Fix");
			lblLabel_FixStatus.setForeground(Color.BLUE);
			textField_FixStatus.setBackground(Color.BLUE);
		} else {
			lblLabel_FixStatus.setText("No Fix");
			lblLabel_FixStatus.setForeground(Color.RED);
			textField_FixStatus.setBackground(Color.RED);
			lastfixed = fixed;
		}
	}

	void showPregress() {
		String str = lblLabel_Progress.getText();
		progressCount++;
		if (progressCount > 10) {
			progressCount = 1;
			str = "@";
		}
		str = " " + str;
		lblLabel_Progress.setText(str);
	}

	/* void freqIntervalSpinnerChanged(ChangeEvent arg0) {
		freqInterval = (Integer) spinner_FreqInterval.getValue();
	} */

	void serverServiceAction(ActionEvent arg0) {
		if (EOSpyGPS.portName.equals(null) || EOSpyGPS.portName.equals("")) {
			return;
		}
		serverService = !serverService;
		showServerService();
		if (serverService) {
			comm.openCommPort();
		} else {
			comm.closeCommPort();
		}
	}

	public static void commLine(String line) {
		eospyui.UpdateDevice(line);
	}

	public void UpdateDevice(String line) {
		gpsnmea.parse(line); // GPS nmea.parse line

		// $GPGGA GPS Log header
		// public double utc = 0; // UTC time status of position (hours/minutes/seconds/decimal seconds)
		deviceEvent.setLat(gpsnmea.position.lat); // lat Latitude
		deviceEvent.setLon(gpsnmea.position.lon); // lon Longitude

		// public int quality = 0; // position fix 0: Fix not available 1: GPS SPS mode refer to GPS quality table
		deviceEvent.setSatellites(gpsnmea.position.satellites); // sats Number of satellites in use. May be different to the number in view
		deviceEvent.setHdop(gpsnmea.position.hdop); // hdop Horizontal dilution of precision 
		deviceEvent.setAltitude(gpsnmea.position.altitude); // altitude Antenna altitude above/below mean sea level
		// public double geoid = 0; // geoid - undulation - the relationship between the geoid ellipsoid
		deviceEvent.setSpeed(gpsnmea.position.speed); // speed Km - Speed over ground, knots
		deviceEvent.setCourse(gpsnmea.position.course); // track true - Track made good, degrees True

		// public double gpsdate = 0; // gps device date - Date: dd/mm/yy
		// public double age = 0; // age Age of correction data (in seconds) - The maximum age limited 99 seconds
		// public double stnID = 0; // stn ID Differential base station ID
		// public String modeMA = ""; // mode MA A = Automatic 2D/3D M = Manual, forced to operate in 2D or 3D
		// public int mode123 = 0; // mode 123 Mode: 1 = Fix not available; 2 = 2D; 3 = 3D

		// public String valid = ""; // data status - Data status: A = Data valid, V = Data invalid
		// public String message; // $GPTXT - message transfers various information on the receiver

		deviceEvent.setValid(gpsnmea.position.fixed); // valid - position fix as boolean refer to GPS quality table
		showFixStatus(gpsnmea.position.fixed);

		DecimalFormat lf = new DecimalFormat("0.000000");
		LatStr = lf.format(deviceEvent.getLat());
		LonStr = lf.format(deviceEvent.getLon());
		textField_Lat.setText(LatStr);
		textField_Lon.setText(LonStr);
		
		if (!gpsnmea.position.fixed) {
			lastSendTime = 0;
		}

		freqInterval = Integer.parseInt(EOSpyGPS.frequence);

		if (((System.currentTimeMillis() - lastSendTime) / 1000F) > freqInterval) {
			lastSendTime = 0;
		}

		if (freqInterval == 0) {
			lastSendTime = 10;
		}
		
		if ((lastSendTime == 0) && (gpsnmea.position.fixed)) {
			lastSendTime = System.currentTimeMillis();
			serverSendPost("");
		}

		if (EOSpyGPS.gpsDebug.indexOf("none") == -1) {
			System.out.println(">" + gpsnmea.position.toString());
		}
		progressBar++;
		if (progressBar > 5) {
			showPregress();
			progressBar = 0;
		}
	}

	void serverSendPost(String IoTEvent) {
		DecimalFormat lf = new DecimalFormat("0.000000");
		DecimalFormat sf = new DecimalFormat("0.00");
		String postMsg = "/?id=" + EOSpyGPS.id; //textField_ID.getText();

		java.util.Date date = new Date();
		long fixtime = date.getTime();
		fixtime = (long) (fixtime * 0.001);
		postMsg = postMsg + "&timestamp=" + Long.toString(fixtime);

		LatStr = lf.format(deviceEvent.getLat());
		LonStr = lf.format(deviceEvent.getLon());
		textField_Lat.setText(LatStr);
		textField_Lon.setText(LonStr);

		postMsg = postMsg + "&lat=" + LatStr;
		postMsg = postMsg + "&lon=" + LonStr;
		postMsg = postMsg + "&speed=" + sf.format(deviceEvent.getSpeed());
		postMsg = postMsg + "&bearing=" + sf.format(deviceEvent.getCourse());
		postMsg = postMsg + "&altitude=" + sf.format(deviceEvent.getAltitude());
		postMsg = postMsg + "&accuracy=0.0&valid=true&batt=89.7";

		String serverEvent = textField_ServerEvent.getText();
		if (!serverEvent.equals(null) || !serverEvent.equals("")) {
			postMsg = postMsg + serverEvent;
		}
		if (!IoTEvent.equals("")) {
			postMsg = postMsg + IoTEvent;
			if (EOSpyGPS.gpsDebug.indexOf("none") == -1) {
				System.out.println();
			}
		}

		String postURL = EOSpyGPS.server; // textField_URL.getText();

		AgentConnect agentConnect = new AgentConnect();
		agentConnect.sendPost(postURL, postMsg);
		// agentConnect.sendGet(postURL, postMsg);
	}

	void mapPositionAction(ActionEvent e) {
		String mapurl = "http://www.google.com/maps?q=";
		WebBrowser wb = new WebBrowser();
		mapurl = mapurl + LatStr + "," + LonStr;
		wb.url(mapurl);
	}

	void helpContentsAction(ActionEvent e) {
		WebBrowser wb = new WebBrowser();
		wb.url("http://www.eospy.com/help/");
	}

	void aboutIoTBPMAction(ActionEvent e) {
		try {
			AboutDialog dialog = new AboutDialog(gpsnmea.position.message);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void updateCOMPort(String portName) {
		textField_FixStatus.setText("      " + portName);
	}

	public void show() {
		this.gpsFrame.setVisible(true);
	}
}
