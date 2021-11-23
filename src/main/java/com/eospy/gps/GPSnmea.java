package com.eospy.gps;

import java.util.HashMap;
import java.util.Map;

/**
 * Executive Order Corporation we make Things Smart
 *
 * Tron AI-IoTBPM Server :: Internet of Things Drools-jBPM Expert System using
 * Arduino Tron AI-IoTBPM Processing GPSnmea - GPS library for Arduino providing
 * universal NMEA parsing from GPS satellites
 *
 * Executive Order Corporation Copyright (c) 1978, 2021: Executive Order
 * Corporation, All Rights Reserved
 */

public class GPSnmea {
	// NMEA acronym for the National Marine Electronics Association. 
	interface SentenceParser {
		public boolean parse(String[] tokens, GPSPosition position);
	}

	// utils
	static double Latitude2Decimal(String lat, String NS) {
		int minutesPosition = lat.indexOf('.') - 2;
		double minutes = Double.parseDouble(lat.substring(minutesPosition));
		double decimalDegrees = Double.parseDouble(lat.substring(minutesPosition)) / 60.0f;

		double degree = Double.parseDouble(lat) - minutes;
		double wholeDegrees = (int) degree / 100;

		double latitudeDegrees = wholeDegrees + decimalDegrees;
		if (NS.startsWith("S")) {
			latitudeDegrees = -latitudeDegrees;
		}
		return latitudeDegrees;
	}

	static double Longitude2Decimal(String lon, String WE) {
		int minutesPosition = lon.indexOf('.') - 2;
		double minutes = Double.parseDouble(lon.substring(minutesPosition));
		double decimalDegrees = Double.parseDouble(lon.substring(minutesPosition)) / 60.0f;

		double degree = Double.parseDouble(lon) - minutes;
		double wholeDegrees = (int) degree / 100;

		double longitudeDegrees = wholeDegrees + decimalDegrees;
		if (WE.startsWith("W")) {
			longitudeDegrees = -longitudeDegrees;
		}
		return longitudeDegrees;
	}

	// parsers
	class GPGGA implements SentenceParser {
		// GPGGA - GPS fix data and undulation
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			try {
				position.utc = Double.parseDouble(tokens[1]);
			} catch (Exception e) {
			}
			try {
				position.lat = Latitude2Decimal(tokens[2], tokens[3]);
			} catch (Exception e) {
			}
			try {
				position.lon = Longitude2Decimal(tokens[4], tokens[5]);
			} catch (Exception e) {
			}
			try {
				position.quality = Integer.parseInt(tokens[6]);
			} catch (Exception e) {
			//	* GPS Quality Indicators
			//	Ind-Description
			//	0 -	Fix not available or invalid
			//	1 -	Single point - Converging PPP (TerraStar-L)
			//	2 -	Pseudorange differential - Converged PPP (TerraStar-L) - Converging PPP (TerraStar-C, TerraStar-C PRO, TerraStar-X)
			//	4 -	RTK fixed ambiguity solution
			//	5 -	RTK doubleing ambiguity solution - Converged PPP (TerraStar-C, TerraStar-C PRO, TerraStar-X)
			//	6 -	Dead reckoning mode
			//	7 -	Manual input mode (fixed position)
			//	8 -	Simulator mode
			//	9 -	WAAS (SBAS)	
			}
			try {
				position.satellites = Integer.parseInt(tokens[7]);
			} catch (Exception e) {
			}
			try {
				position.hdop = Double.parseDouble(tokens[8]);
			} catch (Exception e) {
			}
			try {
				position.altitude = Double.parseDouble(tokens[9]);
			} catch (Exception e) {
			}
			// 10 - a-units - Units of antenna altitude (M = metres)
			try {
				position.geoid = Double.parseDouble(tokens[11]);
			} catch (Exception e) {
			}
			// 12 - u-units - Units of undulation (M = metres)
			return true;
		}
	}

	class GPGLL implements SentenceParser {
		// GPGLL - Geographic position
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			try {
				position.lat = Latitude2Decimal(tokens[1], tokens[2]);
			} catch (Exception e) {
			}
			try {
				position.lon = Longitude2Decimal(tokens[3], tokens[4]);
			} catch (Exception e) {
			}
			try {
				position.utc = Double.parseDouble(tokens[5]);
			} catch (Exception e) {
			}
			try {
				position.valid = tokens[6];
			} catch (Exception e) {
			}
			return true;
		}
	}

	class GPGSA implements SentenceParser {
		// GPGSA - GPS DOP and active satellites
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			try {
				position.modeMA = tokens[1];
			} catch (Exception e) {
			}
			try {
				position.mode123 = Integer.parseInt(tokens[2]);
			} catch (Exception e) {
			}
			return true;
		}
	}

	class GPGSV implements SentenceParser {
		// GPGSV - GPS satellites in view
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			// 0 - $GPGSV - Log header. See Messages for more information.
			// 1 - # msgs - Total number of messages (1-9)
			// 2 - msg # - Message number (1-9)
			// 3 - # sats - Total number of satellites in view.
			// 4 - prn - Satellite PRN number - GPS = SBAS = GLO
			// 5 - elev - Elevation, degrees, 90 maximum
			// 6 - azimuth - Azimuth, degrees True, 000 to 359
			return true;
		}
	}

	class GPRMC implements SentenceParser {
		// GPRMC - GPS specific information
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			try {
				position.utc = Double.parseDouble(tokens[1]);
			} catch (Exception e) {
			}
			try {
				position.valid = tokens[2];
			} catch (Exception e) {
			}
			try {
				position.lat = Latitude2Decimal(tokens[3], tokens[4]);
			} catch (Exception e) {
			}
			try {
				position.lon = Longitude2Decimal(tokens[5], tokens[6]);
			} catch (Exception e) {
			}
			try {
				position.speed = Double.parseDouble(tokens[7]);
			} catch (Exception e) {
			}
			try {
				position.course = Double.parseDouble(tokens[8]);
			} catch (Exception e) {
			}
			try {
				position.gpsdate = Double.parseDouble(tokens[9]);
			} catch (Exception e) {
			}
			return true;
		}
	}

	class GPVTG implements SentenceParser {
		// GPVTG - Track made good and ground speed
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			try {
				position.course = Double.parseDouble(tokens[1]);
			} catch (Exception e) {
			}
			// T - True track indicator
			// track mag - Track made good, degrees Magnetic;
			// Track mag = Track true + (MAGVAR correction)
			// M - Magnetic track indicator
			try {
				position.speed = Double.parseDouble(tokens[5]);
			} catch (Exception e) {
			}
			// N - Nautical speed indicator (N = Knots)
			// speed Km - Speed, kilometres/hour
			// K - Speed indicator (K = km/hr)
			// mode ind - Positioning system mode indicator
			return true;
		}
	}

	class GPTXT implements SentenceParser {
		// GPTXT - GPS message transfers various information on the receiver
		@Override
		public boolean parse(String[] tokens, GPSPosition position) {
			// 1 - 01 numeric xx - Total number of messages in this transmission, 01..99
			// 2 - 01 numeric yy - Message number in this transmission, range 01..xx
			// 3 - 02 numeric zz - Text Identifier 00=ERROR 01=WARNING 02=NOTICE 07=USER
			try {
				position.message = position.message + tokens[4];
			} catch (Exception e) {
			}
			return true;
		}
	}

	public class GPSPosition {

		final double GPS_MPH_PER_KNOT = 1.15077945;
		final double GPS_MPS_PER_KNOT = 0.51444444;
		final double GPS_KMPH_PER_KNOT = 1.852;

		final double GPS_MILES_PER_METER = 0.00062137112;
		final double GPS_KM_PER_METER = 0.001;
		final double GPS_FEET_PER_METER = 3.2808399;

		// $GPGGA GPS Log header
		public double utc = 0; 		// utc time - Status of position (hours/minutes/seconds/decimal seconds)
		public double lat = 0; 		// lat - Latitude
		public double lon = 0; 		// lon - Longitude
		public int quality = 0; 	// position fix 0: Fix not available 1: GPS SPS mode refer to GPS quality table
		public int satellites = 0; 	// sats - Number of satellites in use. May be different to the number in view
		public double hdop = 0; 	// hdop - Horizontal dilution of precision
		public double altitude = 0; // altitude - Antenna altitude above/below mean sea level
		public double geoid = 0; 	// geoid - undulation - the relationship between the geoid ellipsoid
		public double speed = 0; 	// speed Km - Speed over ground, knots
		public double course = 0; 	// track true - Track made good, degrees True
		public double gpsdate = 0; 	// gps device date - Date: dd/mm/yy
		public double age = 0; 		// age - Age of correction data (in seconds) - The maximum age limited 99 seconds.
		public double stnID = 0; 	// stn ID - Differential base station ID
		public String modeMA = ""; 	// mode MA - A = Automatic 2D/3D M = Manual, forced to operate in 2D or 3D
		public int mode123 = 0; 	// mode 123 - 1 = Fix not available; 2 = 2D; 3 = 3D
		public String message = ""; // $GPTXT - message transfers various information on the receiver
		public String valid = ""; 	// data status - Data status: A = Data valid, V = Data invalid 
		public boolean fixed = false; // valid - position fix as boolean refer to GPS quality table

		public void updatefix() {
			fixed = quality > 0;
			// quality is converted to valid fixed
		}

		public double speed_mph() {
			return GPS_MPH_PER_KNOT * speed / 100.0;
		}

		public double speed_mps() {
			return GPS_MPS_PER_KNOT * speed / 100.0;
		}

		public double speed_kmph() {
			return GPS_KMPH_PER_KNOT * speed / 100.0;
		}

		public double altitude_miles() {
			return GPS_MILES_PER_METER * altitude / 100.0;
		}

		public double altitude_kilometers() {
			return GPS_KM_PER_METER * altitude / 100.0;
		}

		public double altitude_feet() {
			return GPS_FEET_PER_METER * altitude / 100.0;
		}

		@Override
		public String toString() {
			return String.format(
					"POSITION: utc: %f, lat: %f, lon: %f, Q: %d, sats: %d, hdop: %f, alt: %f, und: %f, speed: %f, course: %f, gdate: %f, valid: %s",
					utc, lat, lon, quality, satellites, hdop, altitude, geoid, speed, course, gpsdate, valid);
		}
	}

	// * GSV records, which describe satellites 'visible', lack the SNR (signal–to–noise ratio) field for satellite 16 and all data for satellite 36.
    // * GSA record, which lists satellites used for determining a fix (position) and gives a DOP of the fix, contains 12 fields for satellites' 
	//       numbers, but only 8 satellites were taken into account—so 4 fields remain blank.
	public GPSPosition position = new GPSPosition();

	private static final Map<String, SentenceParser> sentenceParsers = new HashMap<String, SentenceParser>();

	public GPSnmea() {
		sentenceParsers.put("GPGGA", new GPGGA()); // GPGGA - GPS fix data and undulation
		sentenceParsers.put("GPGLL", new GPGLL()); // GPGLL - Geographic position
		sentenceParsers.put("GPGSA", new GPGSA()); // GPGSA - GPS DOP and active satellites
		sentenceParsers.put("GPGSV", new GPGSV()); // GPGSV - GPS satellites in view
		sentenceParsers.put("GPRMC", new GPRMC()); // GPRMC - GPS specific information
		sentenceParsers.put("GPVTG", new GPVTG()); // GPVTG - Track made good and ground speed
		sentenceParsers.put("GPTXT", new GPTXT()); // GPTXT - GPS message transfers various information on the receiver
	}

	public GPSPosition parse(String line) {

		if (line.startsWith("$")) {
			String nmea = line.substring(1);
			String[] tokens = nmea.split(",");
			String type = tokens[0];
			// TODO check crc
			if (sentenceParsers.containsKey(type)) {
				sentenceParsers.get(type).parse(tokens, position);
			}
			position.updatefix();
		}
		return position;
	}
}
