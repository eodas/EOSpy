package com.eospy.util;

import java.awt.Desktop;
import java.net.URI;

public class Browser {
	public static void url_(String url) {
		try {
			Runtime.getRuntime().exec("cmd.exe /c start iexplore -new \"" + url + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void url(String url) {
		Desktop desktop = null;
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
