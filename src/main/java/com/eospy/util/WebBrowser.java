package com.eospy.util;

import java.awt.Desktop;
import java.net.URI;

public class WebBrowser {

	public WebBrowser() {
	}

	public void url_(String url) {
		try {
			Runtime.getRuntime().exec("cmd.exe /c start iexplore -new \"" + url + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void url(String url) {
		Desktop desktop = null;
		// open default OS browser
		if (Desktop.isDesktopSupported())
			try {
				desktop = Desktop.getDesktop();
				desktop.browse(new URI(url));
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			url_(url);
	}
}
