package com.eospy.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.eospy.ui.AboutDialog;
import com.eospy.util.WebBrowser;
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

/**
 * Main window implementation for the EOSpy AI-IoT example
 */
public class EOSpyWindow {

	private JFrame frameIoT;

	private JTextField textField_ID;
	private JTextField textField_URL;
	private JTextField textField_Lat;
	private JTextField textField_Lon;
	private JTextField textField_ServerEvent;

	private JLabel lblLabel_ServerStatus;
	private JSpinner spinner_TrackLat;
	private JSpinner spinner_TrackLon;
	private JSpinner spinner_FreqInterval;
	private JToggleButton tglbtnServerToggleButton;

	private int trackLat = 35;
	private int trackLon = -35;
	private int freqInterval = 300;
	private String LatPosition = "38.888160";
	private String LonPosition = "-77.019868";
	private boolean serverService = false;

	public EOSpyWindow(boolean exitOnClose) {
		this.frameIoT = buildFrame(exitOnClose);
	}

	// Initialize the contents of the frame
	private JFrame buildFrame(boolean exitOnClose) {
		frameIoT = new JFrame();

		frameIoT.setTitle("EOSpy AI-IoT :: Internet of Things Arduino Tron");
		frameIoT.setBounds(100, 100, 450, 585);
		frameIoT.setDefaultCloseOperation(exitOnClose ? JFrame.EXIT_ON_CLOSE : WindowConstants.DISPOSE_ON_CLOSE);

		frameIoT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 5, 234, -69, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 17, 14, 2, 14, 20, 2, 14, 20, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 42, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frameIoT.getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel_1 = new JLabel("Service status");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		frameIoT.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

		lblLabel_ServerStatus = new JLabel("Service stopped");
		GridBagConstraints gbc_lblLabel_ServerStatus = new GridBagConstraints();
		gbc_lblLabel_ServerStatus.anchor = GridBagConstraints.NORTH;
		gbc_lblLabel_ServerStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLabel_ServerStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabel_ServerStatus.gridx = 1;
		gbc_lblLabel_ServerStatus.gridy = 2;
		frameIoT.getContentPane().add(lblLabel_ServerStatus, gbc_lblLabel_ServerStatus);

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
		frameIoT.getContentPane().add(tglbtnServerToggleButton, gbc_tglbtnServerToggleButton);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.anchor = GridBagConstraints.NORTH;
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.gridx = 1;
		gbc_separator_1.gridy = 3;
		frameIoT.getContentPane().add(separator_1, gbc_separator_1);

		JLabel lblNewLabel_3 = new JLabel("Device identifier");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_3.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 4;
		frameIoT.getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);

		textField_ID = new JTextField();
		textField_ID.setText("100111");
		GridBagConstraints gbc_textField_ID = new GridBagConstraints();
		gbc_textField_ID.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_ID.insets = new Insets(0, 0, 5, 5);
		gbc_textField_ID.gridx = 1;
		gbc_textField_ID.gridy = 5;
		frameIoT.getContentPane().add(textField_ID, gbc_textField_ID);
		textField_ID.setColumns(10);

		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.anchor = GridBagConstraints.NORTH;
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridwidth = 2;
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 6;
		frameIoT.getContentPane().add(separator_2, gbc_separator_2);

		JLabel lblServerUrl = new JLabel("Server URL");
		lblServerUrl.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblServerUrl = new GridBagConstraints();
		gbc_lblServerUrl.anchor = GridBagConstraints.NORTH;
		gbc_lblServerUrl.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblServerUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerUrl.gridx = 1;
		gbc_lblServerUrl.gridy = 7;
		frameIoT.getContentPane().add(lblServerUrl, gbc_lblServerUrl);

		textField_URL = new JTextField();
		textField_URL.setText("http://10.0.0.2:5055");
		textField_URL.setColumns(15);
		GridBagConstraints gbc_textField_URL = new GridBagConstraints();
		gbc_textField_URL.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_URL.insets = new Insets(0, 0, 5, 5);
		gbc_textField_URL.gridx = 1;
		gbc_textField_URL.gridy = 8;
		frameIoT.getContentPane().add(textField_URL, gbc_textField_URL);

		JLabel lblNewLabel = new JLabel("Tracking server URL");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 9;
		frameIoT.getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		JSeparator separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_3.gridwidth = 2;
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 1;
		gbc_separator_3.gridy = 10;
		frameIoT.getContentPane().add(separator_3, gbc_separator_3);

		JLabel lblNewLabel_4 = new JLabel("Frequence");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 11;
		frameIoT.getContentPane().add(lblNewLabel_4, gbc_lblNewLabel_4);

		spinner_FreqInterval = new JSpinner();
		spinner_FreqInterval.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				freqIntervalSpinnerChanged(arg0);
			}
		});
		spinner_FreqInterval.setModel(new SpinnerNumberModel(freqInterval, 1, 100000, 1));
		GridBagConstraints gbc_spinner_FreqInterval = new GridBagConstraints();
		gbc_spinner_FreqInterval.anchor = GridBagConstraints.NORTHWEST;
		gbc_spinner_FreqInterval.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_FreqInterval.gridx = 1;
		gbc_spinner_FreqInterval.gridy = 12;
		frameIoT.getContentPane().add(spinner_FreqInterval, gbc_spinner_FreqInterval);

		JLabel lblNewLabel_5 = new JLabel("Reporting interval in seconds");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 13;
		frameIoT.getContentPane().add(lblNewLabel_5, gbc_lblNewLabel_5);

		JSeparator separator_4 = new JSeparator();
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.gridwidth = 2;
		gbc_separator_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_4.insets = new Insets(0, 0, 5, 5);
		gbc_separator_4.gridx = 1;
		gbc_separator_4.gridy = 14;
		frameIoT.getContentPane().add(separator_4, gbc_separator_4);

		JLabel lblNewLabel_6 = new JLabel("Lat");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 1;
		gbc_lblNewLabel_6.gridy = 15;
		frameIoT.getContentPane().add(lblNewLabel_6, gbc_lblNewLabel_6);

		JLabel lblNewLabel_2 = new JLabel("GPS Track");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 15;
		frameIoT.getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);

		textField_Lat = new JTextField();
		textField_Lat.setText(LatPosition);
		textField_Lat.setColumns(10);
		GridBagConstraints gbc_textField_Lat = new GridBagConstraints();
		gbc_textField_Lat.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_Lat.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Lat.gridx = 1;
		gbc_textField_Lat.gridy = 16;
		frameIoT.getContentPane().add(textField_Lat, gbc_textField_Lat);

		spinner_TrackLat = new JSpinner();
		spinner_TrackLat.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				trackLatChange(arg0);
			}
		});
		spinner_TrackLat.setModel(new SpinnerNumberModel(trackLat, -100, 100, 1));
		GridBagConstraints gbc_spinner_Lat = new GridBagConstraints();
		gbc_spinner_Lat.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_Lat.gridx = 2;
		gbc_spinner_Lat.gridy = 16;
		frameIoT.getContentPane().add(spinner_TrackLat, gbc_spinner_Lat);

		JLabel lblLon = new JLabel("Lon");
		lblLon.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblLon = new GridBagConstraints();
		gbc_lblLon.anchor = GridBagConstraints.NORTH;
		gbc_lblLon.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLon.insets = new Insets(0, 0, 5, 5);
		gbc_lblLon.gridx = 1;
		gbc_lblLon.gridy = 17;
		frameIoT.getContentPane().add(lblLon, gbc_lblLon);

		textField_Lon = new JTextField();
		textField_Lon.setText(LonPosition);
		textField_Lon.setColumns(10);
		GridBagConstraints gbc_textField_Lon = new GridBagConstraints();
		gbc_textField_Lon.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_Lon.insets = new Insets(0, 0, 5, 5);
		gbc_textField_Lon.gridx = 1;
		gbc_textField_Lon.gridy = 18;
		frameIoT.getContentPane().add(textField_Lon, gbc_textField_Lon);

		spinner_TrackLon = new JSpinner();
		spinner_TrackLon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				trackLonChange(arg0);
			}
		});
		spinner_TrackLon.setModel(new SpinnerNumberModel(trackLon, -100, 100, 1));
		GridBagConstraints gbc_spinner_Lon = new GridBagConstraints();
		gbc_spinner_Lon.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_Lon.gridx = 2;
		gbc_spinner_Lon.gridy = 18;
		frameIoT.getContentPane().add(spinner_TrackLon, gbc_spinner_Lon);

		JLabel lblNewLabel_7 = new JLabel("Lat / Lon GPS position track values");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 1;
		gbc_lblNewLabel_7.gridy = 19;
		frameIoT.getContentPane().add(lblNewLabel_7, gbc_lblNewLabel_7);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 2;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 20;
		frameIoT.getContentPane().add(separator, gbc_separator);

		JLabel lblNewLabel_8 = new JLabel("Server event model data fields");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.gridx = 1;
		gbc_lblNewLabel_8.gridy = 21;
		frameIoT.getContentPane().add(lblNewLabel_8, gbc_lblNewLabel_8);

		textField_ServerEvent = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.gridwidth = 2;
		gbc_textField_4.anchor = GridBagConstraints.NORTH;
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 22;
		frameIoT.getContentPane().add(textField_ServerEvent, gbc_textField_4);
		textField_ServerEvent.setColumns(10);

		JSeparator separator_5 = new JSeparator();
		GridBagConstraints gbc_separator_5 = new GridBagConstraints();
		gbc_separator_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_5.gridwidth = 2;
		gbc_separator_5.insets = new Insets(0, 0, 5, 5);
		gbc_separator_5.gridx = 1;
		gbc_separator_5.gridy = 23;
		frameIoT.getContentPane().add(separator_5, gbc_separator_5);

		JLabel lblNewLabel_9 = new JLabel("Key press server alerts");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
		gbc_lblNewLabel_9.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_9.gridx = 1;
		gbc_lblNewLabel_9.gridy = 24;
		frameIoT.getContentPane().add(lblNewLabel_9, gbc_lblNewLabel_9);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridheight = 2;
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 25;
		frameIoT.getContentPane().add(panel_1, gbc_panel_1);

		JButton btnKey1 = new JButton("Key1");
		btnKey1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				serverIoTSendPost("&keypress=1.0");
			}
		});
		btnKey1.setBounds(0, 11, 65, 23);
		panel_1.add(btnKey1);

		JButton btnKey2 = new JButton("Key2");
		btnKey2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverIoTSendPost("&keypress=2.0");
			}
		});
		btnKey2.setBounds(90, 11, 65, 23);
		panel_1.add(btnKey2);

		JButton btnReed = new JButton("Reed");
		btnReed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverIoTSendPost("&keypress=4.0");
			}
		});
		btnReed.setBounds(178, 11, 65, 23);
		panel_1.add(btnReed);

		JButton btnProx = new JButton("Prox");
		btnProx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverIoTSendPost("&keypress=8.0");
			}
		});
		btnProx.setBounds(267, 11, 65, 23);
		panel_1.add(btnProx);

		JButton Post = new JButton("Post");
		Post.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				serverIoTSendPost("");
			}
		});
		Post.setBounds(0, 45, 65, 23);
		panel_1.add(Post);

		JButton Help = new JButton("Help");
		Help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpContentsAction(e);
			}
		});
		Help.setBounds(90, 45, 65, 23);
		panel_1.add(Help);

		JButton About = new JButton("About");
		About.setFont(new Font("Tahoma", Font.PLAIN, 10));
		About.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutIoTBPMAction(e);
			}
		});
		About.setBounds(178, 45, 65, 23);
		panel_1.add(About);

		JButton Exit = new JButton("Exit");
		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		Exit.setBounds(267, 45, 65, 23);
		panel_1.add(Exit);

		showServerService();

		return frameIoT;
	}

	void serverServiceAction(ActionEvent arg0) {
		serverService = !serverService;
		showServerService();
		if (serverService) {
			runServer();
		}
	}

	void showServerService() {
		if (serverService) {
			lblLabel_ServerStatus.setText("Service started");
			tglbtnServerToggleButton.setText("On");
		} else {
			lblLabel_ServerStatus.setText("Service stopped");
			tglbtnServerToggleButton.setText("Off");
		}
	}

	void runServer() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (serverService) {
					// keep doing thread post
					serverIoTSendPost("");
					calcLatLonAction(null);

					try {
						Thread.sleep(freqInterval * 1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	void serverIoTSendPost(String IoTEvent) {
		String postMsg = "/?id=" + textField_ID.getText();

		java.util.Date date = new Date();
		long fixtime = date.getTime();
		fixtime = (long) (fixtime * 0.001);
		postMsg = postMsg + "&timestamp=" + Long.toString(fixtime);

		postMsg = postMsg + "&lat=" + LatPosition;
		postMsg = postMsg + "&lon=" + LonPosition;
		postMsg = postMsg + "&speed=0.0&bearing=0.0&altitude=0.0&accuracy=0.0&batt=89.7";

		String serverEvent = textField_ServerEvent.getText();
		if (!serverEvent.equals(null) || !serverEvent.equals("")) {
			postMsg = postMsg + serverEvent;
		}
		if (!IoTEvent.equals("")) {
			postMsg = postMsg + IoTEvent;
		}

		String postURL = textField_URL.getText();

		AgentConnect agentConnect = new AgentConnect();
		agentConnect.sendPost(postURL, postMsg);
	}

	void freqIntervalSpinnerChanged(ChangeEvent arg0) {
		freqInterval = (Integer) spinner_FreqInterval.getValue();
	}

	void trackLatChange(ChangeEvent arg0) {
		trackLat = (Integer) spinner_TrackLat.getValue();
	}

	void trackLonChange(ChangeEvent arg0) {
		trackLon = (Integer) spinner_TrackLon.getValue();
	}

	void calcLatLonAction(ActionEvent arg0) {
		DecimalFormat df = new DecimalFormat("0.000000");

		String Lat_str = textField_Lat.getText();
		double Lat_num = new Double(Lat_str).doubleValue();
		double Lat_new = (trackLat * 0.000001);
		if (trackLat > 0) {
			Lat_num = Lat_num + Lat_new;
		} else {
			Lat_num = Lat_num - Lat_new;
		}
		LatPosition = df.format(Lat_num);
		// LatPosition = new Double(Lat_num).toString();
		textField_Lat.setText(LatPosition);
		//
		String Lon_str = textField_Lon.getText();
		double Lon_num = new Double(Lon_str).doubleValue();
		double Lon_new = (trackLon * 0.000001);
		if (trackLon > 0) {
			Lon_num = Lon_num + Lon_new;
		} else {
			Lon_num = Lon_num - Lon_new;
		}
		LonPosition = df.format(Lon_num);
		// LonPosition = new Double(Lon_num).toString();
		textField_Lon.setText(LonPosition);
	}

	void helpContentsAction(ActionEvent e) {
		WebBrowser wb = new WebBrowser();
		wb.url("http://www.eospy.com/help/");
	}

	void aboutIoTBPMAction(ActionEvent e) {
		try {
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void show() {
		this.frameIoT.setVisible(true);
	}

}
