package com.eospy.gpio;

import com.eospy.ui.EOSpyUI;

import java.awt.Color;
import java.io.IOException;
import java.util.Scanner;

/**
* PROJECT       :  Executive Order Corporation we make Things Smart
* ORGANIZATION  :  Copyright (c) 1978, 2022: Executive Order Corporation, All Rights Reserved
* ---
* 
* pi@raspberrypi:~ $ pinout
* ,--------------------------------.
* | oooooooooooooooooooo J8   +======
* | 1ooooooooooooooooooo  PoE |   Net
* |  Wi                    1o +======
* |  Fi  Pi Model 4B  V1.1 oo      |
* |        ,----. +---+         +====
* | |D|    |SoC | |RAM|         |USB3
* | |S|    |    | |   |         +====
* | |I|    `----' +---+            |
* |                   |C|       +====
* |                   |S|       |USB2
* | pwr   |hd|   |hd| |I||A|    +====
* `-| |---|m0|---|m1|----|V|-------'
* 
* Revision           : b03111
* SoC                : BCM2711
* Storage            : MicroSD
* USB ports          : 4 (of which 2 USB3)
* Ethernet ports     : 1 (1000Mbps max. speed)
* Wi-fi              : True
* Bluetooth          : True
* Camera ports (CSI) : 1
* Display ports (DSI): 1
* 
* J8:
*    3V3  (1) (2)  5V    
*  GPIO2  (3) (4)  5V    
*  GPIO3  (5) (6)  GND   
*  GPIO4  (7) (8)  GPIO14
*    GND  (9) (10) GPIO15
* GPIO17 (11) (12) GPIO18
* GPIO27 (13) (14) GND   
* GPIO22 (15) (16) GPIO23
*    3V3 (17) (18) GPIO24
* GPIO10 (19) (20) GND   
*  GPIO9 (21) (22) GPIO25
* GPIO11 (23) (24) GPIO8 
*    GND (25) (26) GPIO7 
*  GPIO0 (27) (28) GPIO1 
*  GPIO5 (29) (30) GND   
*  GPIO6 (31) (32) GPIO12
* GPIO13 (33) (34) GND   
* GPIO19 (35) (36) GPIO16
* GPIO26 (37) (38) GPIO20
*    GND (39) (40) GPIO21
* 
* POE:
* TR01 (1) (2) TR00
* TR03 (3) (4) TR02
* 
* For further information, please refer to https://pinout.xyz/
* 
* +-----+-----+---------+------+---+---Pi 4B--+---+------+---------+-----+-----+
* | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
* +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
* |     |     |    3.3v |      |   |  1 || 2  |   |      | 5v      |     |     |
* |   2 |   8 |   SDA.1 |   IN | 1 |  3 || 4  |   |      | 5v      |     |     |
* |   3 |   9 |   SCL.1 |   IN | 1 |  5 || 6  |   |      | 0v      |     |     |
* |   4 |   7 | GPIO. 7 |   IN | 1 |  7 || 8  | 1 | IN   | TxD     | 15  | 14  |
* |     |     |      0v |      |   |  9 || 10 | 1 | IN   | RxD     | 16  | 15  |
* |  17 |   0 | GPIO. 0 |   IN | 0 | 11 || 12 | 0 | IN   | GPIO. 1 | 1   | 18  |
* |  27 |   2 | GPIO. 2 |   IN | 0 | 13 || 14 |   |      | 0v      |     |     |
* |  22 |   3 | GPIO. 3 |   IN | 0 | 15 || 16 | 0 | IN   | GPIO. 4 | 4   | 23  |
* |     |     |    3.3v |      |   | 17 || 18 | 0 | IN   | GPIO. 5 | 5   | 24  |
* |  10 |  12 |    MOSI |   IN | 0 | 19 || 20 |   |      | 0v      |     |     |
* |   9 |  13 |    MISO |   IN | 0 | 21 || 22 | 0 | IN   | GPIO. 6 | 6   | 25  |
* |  11 |  14 |    SCLK |   IN | 0 | 23 || 24 | 1 | IN   | CE0     | 10  | 8   |
* |     |     |      0v |      |   | 25 || 26 | 1 | IN   | CE1     | 11  | 7   |
* |   0 |  30 |   SDA.0 |   IN | 1 | 27 || 28 | 1 | IN   | SCL.0   | 31  | 1   |
* |   5 |  21 | GPIO.21 |  OUT | 0 | 29 || 30 |   |      | 0v      |     |     |
* |   6 |  22 | GPIO.22 |  OUT | 1 | 31 || 32 | 0 | IN   | GPIO.26 | 26  | 12  |
* |  13 |  23 | GPIO.23 |   IN | 0 | 33 || 34 |   |      | 0v      |     |     |
* |  19 |  24 | GPIO.24 |   IN | 0 | 35 || 36 | 0 | IN   | GPIO.27 | 27  | 16  |
* |  26 |  25 | GPIO.25 |   IN | 0 | 37 || 38 | 0 | IN   | GPIO.28 | 28  | 20  |
* |     |     |      0v |      |   | 39 || 40 | 0 | IN   | GPIO.29 | 29  | 21  |
* +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
* | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
* +-----+-----+---------+------+---+---Pi 4B--+---+------+---------+-----+-----+
*
* Jam HAT - 6 LED - 2 Switch - 1 Buzzer Board
* The table below shows the pin numbers for BCM, Board and the matching GPIO Zero objects.
* |Component |GPIO.BCM | BOARD  |GPIO Zero object |WiringPi | Notes 
* |----------|---------|--------|-----------------|---------|---------------------|
* | LED1     | GPIO 5  | Pin 29 | lights_1.red    | GPIO.21 |
* | LED2     | GPIO 6  | Pin 31 | lights_2.red    | GPIO.22 |
* | LED3     | GPIO 12 | Pin 32 | lights_1.yellow | GPIO.26 |
* | LED4     | GPIO 13 | Pin 33 | lights_2.yellow | GPIO.23 |
* | LED5     | GPIO 16 | Pin 36 | lights_1.green  | GPIO.27 |
* | LED6     | GPIO 17 | Pin 11 | lights_2.green  | GPIO.00 |
* | Button 1 | GPIO 19 | Pin 35 | button_1        | GPIO.24 | Connected to R8/R10 |
* | Button 2 | GPIO 18 | Pin 12 | button_2        | GPIO.01 | Connected to R7/ R9 | 
* | Buzzer   | GPIO 20 | Pin 38 | buzzer          | GPIO.28 |
* 
* # BerryClip+ - 6 LED - 2 Switch - 1 Buzzer Board Hardware Reference
* # ====================================
* # The components are connected to the main Pi GPIO header (P1)
* # Component  Pin       BCM    WiringPi
* # ---------|-------|--------|---------
* # LED 1    - P1-07 - GPIO4  - GPIO. 7
* # LED 2    - P1-11 - GPIO17 - GPIO. 0
* # LED 3    - P1-15 - GPIO22 - GPIO. 3
* # LED 4    - P1-19 - GPIO10
* # LED 5    - P1-21 - GPIO9
* # LED 6    - P1-23 - GPIO11
* # Buzzer   - P1-24 - GPIO8
* # Switch 1 - P1-26 - GPIO7
* # Swtich 2 - P1-22 - GPIO25
*/

/**
 * Main window implementation for the Raspberry GPIO example
 */
public class RPiGPIO {
	private static RPiGPIO rpigpio = null;
	private EOSpyUI eospyui = null;
  	private boolean gpioActive = false;

	// provision led gpio pins as an output gpio pins
    private final String RED_LED1 = "05"; // RED1 LED pin GPIO.BCM 05
    private final String RED_LED2 = "06"; // RED2 LED pin GPIO.BCM 06
    private final String YELLOW_LED1 = "12"; // YELLOW1 LED pin GPIO.BCM 12
    private final String YELLOW_LED2 = "13"; // YELLOW2 LED pin GPIO.BCM 13
    private final String GREEN_LED1 = "16"; // GREEN1 LED pin GPIO.BCM 16
    private final String GREEN_LED2 = "16"; // GREEN2 LED pin GPIO.BCM 17

	// provision switch gpio pins as an input pin with its internal pull down resistor enabled
    private final String BUTTON1 = "19"; // Button pin GPIO.BCM 19
    private final String BUTTON2 = "18"; // Button pin GPIO.BCM 18

	// provision buzzer gpio pin as an output pins and buzz
    private final String BUZZER1 = "20"; // RED LED pin GPIO.BCM 20

	public RPiGPIO(EOSpyUI eospyui) {
		this.eospyui = eospyui;
		rpigpio = this;
	}

	public static RPiGPIO getInstance() {
		return rpigpio;
	}

    private enum Action {
        OUTPUT_PIN("op"),
        INPUT_PIN("ip"),
        DIGITAL_HIGH("dh"),
        DIGITAL_LOW("dl");
        private final String action;
        Action(String action) {
            this.action = action;
        }
        String getAction() {
            return action;
        }
    }
	
	public void setGPIOActive(boolean gpioActive) {
		this.gpioActive = gpioActive;
	}

    private void doAction(String pin, Action action) {
        runCommand("raspi-gpio set " + pin + " " + action.getAction().toLowerCase());
    }

    private String runCommand(String cmd) {
        // System.out.println("GPIO Executing: " + cmd);
        Scanner s = null;
        try {
            s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
                .useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception ex) {
            System.err.println("Error during command: " + ex.getMessage());
            return "";
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }
	
	 // This code performs GPIO configuration logic of a GPIO pins
	public void gpioStart() {
		// provision led gpio pins as an output pins and blink
        doAction(RED_LED1, Action.OUTPUT_PIN);
        doAction(RED_LED2, Action.OUTPUT_PIN);
        doAction(YELLOW_LED1, Action.OUTPUT_PIN);
        doAction(YELLOW_LED2, Action.OUTPUT_PIN);
        doAction(GREEN_LED1, Action.OUTPUT_PIN);
        doAction(GREEN_LED2, Action.OUTPUT_PIN);

		// provision switch gpio pins as an input pin with its internal pull down resistor enabled
        doAction(BUTTON1, Action.INPUT_PIN);
        doAction(BUTTON2, Action.INPUT_PIN);

		// provision buzzer gpio pin as an output pins and buzz
        doAction(BUZZER1, Action.OUTPUT_PIN);

        gpioSwitchState();
		gpioActive = true;
	}

	public void gpioStop() {
		// Stop all GPIO activity/threads, shutdown all GPIO monitoring threads and scheduled tasks
        doAction(RED_LED1, Action.DIGITAL_LOW);
        doAction(RED_LED2, Action.DIGITAL_LOW);
        doAction(YELLOW_LED1, Action.DIGITAL_LOW);
        doAction(YELLOW_LED2, Action.DIGITAL_LOW);
        doAction(GREEN_LED1, Action.DIGITAL_LOW);
        doAction(GREEN_LED2, Action.DIGITAL_LOW);

        doAction(BUZZER1, Action.DIGITAL_LOW);

		// Show inactive
		gpioActive = false;
	}

	// RED1 LED pin GPIO.BCM 05
	public void redled1On() {
        doAction(RED_LED1, Action.DIGITAL_HIGH);
	}

	public void redled1Off() {
        doAction(RED_LED1, Action.DIGITAL_LOW);
	}

	// RED2 LED pin GPIO.BCM 06
	public void redled2On() {
        doAction(RED_LED2, Action.DIGITAL_HIGH);
	}

	public void redled2Off() {
        doAction(RED_LED2, Action.DIGITAL_LOW);
	}

	// YELLOW1 LED pin GPIO.BCM 12
	public void yellowled1On() {
        doAction(YELLOW_LED1, Action.DIGITAL_HIGH);
	}

	public void yellowled1Off() {
        doAction(YELLOW_LED1, Action.DIGITAL_LOW);
	}
	
	// YELLOW2 LED pin GPIO.BCM 13	
	public void yellowled2On() {
        doAction(YELLOW_LED2, Action.DIGITAL_HIGH);
	}

	public void yellowled2Off() {
        doAction(YELLOW_LED2, Action.DIGITAL_LOW);
	}

	// GREEN1 LED pin GPIO.BCM 16
	public void greenled1On() {
        doAction(GREEN_LED1, Action.DIGITAL_HIGH);
	}

	public void greenled1Off() {
        doAction(GREEN_LED1, Action.DIGITAL_LOW);
	}

	// GREEN2 LED pin GPIO.BCM 17	
	public void greenled2On() {
        doAction(GREEN_LED2, Action.DIGITAL_HIGH);
	}

	public void greenled2Off() {
        doAction(GREEN_LED2, Action.DIGITAL_LOW);
	}

	public void redled1Blink(final long delay, final long duration) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			boolean blink = true;
			for (int i = 0; i < duration; i++) {
				if (blink) {
			        doAction(RED_LED1, Action.DIGITAL_HIGH);
				} else {
			        doAction(RED_LED1, Action.DIGITAL_LOW);
				}
				blink = !blink;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gpioActive) {
					break;
				}
			}
		}
	}).start();
	}

	public void redled2Blink(final long delay, final long duration) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			boolean blink = true;
			for (int i = 0; i < duration; i++) {
				if (blink) {
			        doAction(RED_LED2, Action.DIGITAL_HIGH);
				} else {
			        doAction(RED_LED2, Action.DIGITAL_LOW);
				}
				blink = !blink;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gpioActive) {
					break;
				}
			}
		}
	}).start();
	}
	
	public void yellowled1Blink(final long delay, final long duration) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			boolean blink = true;
			for (int i = 0; i < duration; i++) {
				if (blink) {
			        doAction(YELLOW_LED1, Action.DIGITAL_HIGH);
				} else {
			        doAction(YELLOW_LED1, Action.DIGITAL_LOW);
				}
				blink = !blink;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gpioActive) {
					break;
				}
			}
		}
	}).start();
	}

	public void yellowled2Blink(final long delay, final long duration) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			boolean blink = true;
			for (int i = 0; i < duration; i++) {
				if (blink) {
			        doAction(YELLOW_LED2, Action.DIGITAL_HIGH);
				} else {
			        doAction(YELLOW_LED2, Action.DIGITAL_LOW);
				}
				blink = !blink;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gpioActive) {
					break;
				}
			}
		}
	}).start();
	}

	public void greenled1Blink(final long delay, final long duration) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			boolean blink = true;
			for (int i = 0; i < duration; i++) {
				if (blink) {
			        doAction(GREEN_LED1, Action.DIGITAL_HIGH);
				} else {
			        doAction(GREEN_LED1, Action.DIGITAL_LOW);
				}
				blink = !blink;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gpioActive) {
					break;
				}
			}
		}
	}).start();
	}

	public void greenled2Blink(final long delay, final long duration) {
	new Thread(new Runnable() {
		@Override
		public void run() {
			boolean blink = true;
			for (int i = 0; i < duration; i++) {
				if (blink) {
			        doAction(GREEN_LED2, Action.DIGITAL_HIGH);
				} else {
			        doAction(GREEN_LED2, Action.DIGITAL_LOW);
				}
				blink = !blink;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gpioActive) {
					break;
				}
			}
		}
	}).start();
	}
	
	public void buzzer(int s1, int s2, int cnt) {
		int i = 0; // loop counter
        while(i < cnt) {
        	try {
        		doAction(BUZZER1, Action.DIGITAL_HIGH);
				Thread.sleep(s1);
        		doAction(BUZZER1, Action.DIGITAL_LOW);
				Thread.sleep(s2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	i++;
        }
	}
	
	// This interface is extension of GPIO Pin interface with operation to read digital states
  	public void gpioSwitchState() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (gpioActive) {
					String result1 = runCommand("raspi-gpio get " + BUTTON1);
					if (result1.contains("level=1")) {
						eospyui.serverSendPost("&keypress=1.0");
						System.out.println("button1.getState().isHigh()");
					}

					String result2 = runCommand("raspi-gpio get " + BUTTON2);
					if (result2.contains("level=1")) {
						eospyui.serverSendPost("&keypress=2.0");
						System.out.println("button2.getState().isHigh()");
					} 
					
					try {
						Thread.sleep(200L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	} 
}

/*	
    // create and register gpio pin listener
	 button1.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
    	 EOSpyUI.getInstance().serverSendPost("&keypress=1.0");
      }
    });

	 button2.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
    	 EOSpyUI.getInstance().serverSendPost("&keypress=2.0");
      }
    });
*/
