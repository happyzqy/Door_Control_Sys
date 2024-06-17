package com.socket;


public class Const {

	public static final String LED_FILENAME = "/dev/ledtest";
	public static final int CMD_OPEN = 0x01;
	public static final int CMD_RUN = 0x02;
	public static final int CMD_CLOSE = 0x03;
	public static final int CMD_WRITE = 0x04;
	public static final int CMD_READ = 0x05;

	public static final int SERVER_PORT = 6109;

	public static final int K60 = 0x70;
	public static final int MATRIX = 0x10;
	public static final int DIGTAL = 0x20;
	public static final int MOTOR = 0x30;
	public static final int BLIGHT = 0x40;
	public static final int NFC = 0x50;
	public static final int ZIGBEE = 0x60;
	/*
	public static final int[] BLIGHTS = {
	R.drawable.green_led_normal, R.drawable.green_selected,
	R.drawable.yellow_led_normal, R.drawable.yellow_selected,
	R.drawable.red_led_normal, R.drawable.red_selected, };*/
	
	public static final int CITYBUS_HEIGHT = 480;
	public static final int CITYBUS_WIDTH = 800;
	public static final int TRAFICLIGHT_H = 40;
	public static final int TRAFICLIGHT_W = 50;
	public static final int BUS_HEIGHT = 50;
	public static final int[] POINTS={200,317,200,263,200,235,200,110,200,60,20,60,20,190,100,190,135,190,321,190,400,190,400,314};

	public static final byte[]nfc_open={(byte) 0xFE,(byte) 0xE0,0x08,0x51,0x72,0x00,0x00,0x0A};
	public static final byte[]nfc_read={(byte) 0xFE,(byte) 0xE0,0x08,0x55,0x72,0x00,0x00,0x0A};
	public static final byte[]moto_right={(byte)0xFE,(byte)0xE0,0x08,0x32,0x72,0x00,0x02,0x0A};
	public static final byte[]moto_left={(byte)0xFE,(byte) 0xE0,0x08,0x32,0x72,0x00,0x03,0x0A};
	public static final byte[]moto_stop={(byte)0xFE,(byte) 0xE0,0x08,0x32,0x72,0x00,0x01,0x0A};
	public static final byte[]matrix_open={(byte)0xFE,(byte) 0xE0,0x08,0x12,0x72,0x00,0x03,0x0A};
	public static final byte[]matrix_close={(byte)0xFE,(byte) 0xE0,0x08,0x13,0x72,0x00,0x00,0x0A};

	public static final byte[]blight_yello={(byte)0xFE,(byte) 0xE0,0x08,0x44,0x72,0x00,0x12,0x0A};
	public static final byte[]blight_red={(byte)0xFE,(byte) 0xE0,0x08,0x44,0x72,0x00,0x09,0x0A};
	public static final byte[]blight_green={(byte)0xFE,(byte) 0xE0,0x08,0x44,0x72,0x00,0x24,0x0A};
	public static final byte[]blight_close={(byte)0xFE,(byte) 0xE0,0x08,0x44,0x72,0x00,0x00,0x0A};
}
