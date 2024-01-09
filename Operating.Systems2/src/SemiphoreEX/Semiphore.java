package SemiphoreEX;

import java.util.concurrent.*;
import java.util.Random;

public class Semiphore {
	public static void main(String[] args) {
		Semaphore empty = new Semaphore(4);
		Semaphore full = new Semaphore(0);
		Buf buffer = new Buf();
		Prod prod = new Prod(buffer, empty, full);
		cnsmerline con = new cnsmerline(buffer, empty, full);
		new Thread(prod).start();
		new Thread(con).start();
	}
}

class Buf {
	private Crt[] buffer = new Crt[4];
	private int in = 0, out = 0;

	public void insert(Crt certificate) {
		buffer[in] = certificate;
		in = (in + 1) % buffer.length;
	}

	public Crt remove() {
		Crt certificate = buffer[out];
		out = (out + 1) % buffer.length;
		return certificate;
	}
}

class Prod implements Runnable {
	private Buf buffer;
	private Semaphore empty, full;

	public Prod(Buf buffer, Semaphore empty, Semaphore full) {
		this.buffer = buffer;
		this.empty = empty;
		this.full = full;
	}

	public void run() {
		while (true) {
			Crt certificate = new Crt();
			try {
				empty.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (buffer) {
				buffer.insert(certificate);
			}
			full.release();
			System.out.println("OUTPUT BY PRODUCER: " + certificate);
		}
	}
}

class cnsmerline implements Runnable {
	private Buf buffer;
	private Semaphore empty, full;

	public cnsmerline(Buf buffer, Semaphore empty, Semaphore full) {
		this.buffer = buffer;
		this.empty = empty;
		this.full = full;
	}

	public void run() {
		while (true) {
			try {
				full.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Crt certificate;
			synchronized (buffer) {
				certificate = buffer.remove();
			}
			empty.release();
			CertProc processor = new CertProc(certificate);
			processor.processCertificate();
			processor.printOutput();
		}
	}
}

class Crt {
	String model;
	String serial;
	String color;
	String code;
	int warranty;
	double manufacturerPrice;

	public Crt() {
		String[] models = { "M1", "M2", "M3" };
		String[] colors = { "Blue", "Orange", "Green" };
		Random rand = new Random();
		model = models[rand.nextInt(3)];
		serial = getRandomAlphanumeric(5);
		color = colors[rand.nextInt(3)];
		code = generateCode(model);
		warranty = getWarranty(model);
		manufacturerPrice = getManufacturerPrice(model);
	}

	private String getRandomAlphanumeric(int length) {
		String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(alphanumeric.charAt(rand.nextInt(alphanumeric.length())));
		}

		return sb.toString();
	}

	private String generateCode(String model) {
		switch (model) {
		case "M1":
			return "0000";
		case "M2":
			return getRandomAlphabetic(4);
		case "M3":
			return getRandomUniqueAlphabetic(8);
		default:
			return "";
		}
	}

	private String getRandomAlphabetic(int length) {
		String alphabetic = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();

		for (int i = 0; i < length; i++) {
			sb.append(alphabetic.charAt(rand.nextInt(alphabetic.length())));
		}

		return sb.toString();
	}

	private String getRandomUniqueAlphabetic(int length) {
		String alphabetic = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		int[] usedIndices = new int[length];
		int index;

		for (int i = 0; i < length; i++) {
			do {
				index = rand.nextInt(alphabetic.length());
			} while (contains(usedIndices, index));
			usedIndices[i] = index;
			sb.append(alphabetic.charAt(index));
		}
		return sb.toString();
	}

	private boolean contains(int[] array, int value) {
		for (int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

	private int getWarranty(String model) {
		switch (model) {
		case "M1":
			return 1;
		case "M2":
			return 2;
		case "M3":
			return 3;
		default:
			return 0;
		}
	}

	private double getManufacturerPrice(String model) {
		switch (model) {
		case "M1":
			return 850;
		case "M2":
			return 1325;
		case "M3":
			return 1950;
		default:
			return 0;
		}
	}

	@Override
	public String toString() {
		return model + " " + serial + " " + color + " " + code + " " + warranty + " "
				+ String.format("%.2f", manufacturerPrice);
	}
}

class CertProc {
	private Crt certificate;
	private double storageFee, transportationFee, warrantyFee, emergencyFee, colorFee, retailPrice;
	private String revealedCode;

	public CertProc(Crt certificate) {
		this.certificate = certificate;
	}

	public void processCertificate() {
		storageFee = calculateStorageFee();
		transportationFee = calculateTransportationFee();
		warrantyFee = calculateWarrantyFee();
		emergencyFee = calculateEmergencyFee();
		colorFee = calculateColorFee();
		retailPrice = calculateRetailPrice();
		revealedCode = revealCode();
	}

	private double calculateStorageFee() {
		switch (certificate.model) {
		case "M1":
			return certificate.manufacturerPrice * 0.04;
		case "M2":
			return certificate.manufacturerPrice * 0.08;
		case "M3":
			return certificate.manufacturerPrice * 0.10;
		default:
			return 0;
		}
	}

	private double calculateTransportationFee() {
		switch (certificate.model) {
		case "M1":
			return 0;
		case "M2":
			return certificate.manufacturerPrice * 0.01;
		case "M3":
			return certificate.manufacturerPrice * 0.04;
		default:
			return 0;
		}
	}

	private double calculateWarrantyFee() {
		switch (certificate.model) {
		case "M1":
			return 60;
		case "M2":
			return 30;
		case "M3":
			return 0;
		default:
			return 0;
		}
	}

	private double calculateEmergencyFee() {
		switch (certificate.model) {
		case "M1":
			return 0;
		case "M2":
			return 50;
		case "M3":
			return 70;
		default:
			return 0;
		}
	}

	private double calculateColorFee() {
		switch (certificate.color) {
		case "Blue":
			return certificate.manufacturerPrice * 0.02;
		case "Orange":
			return certificate.manufacturerPrice * 0.05;
		case "Green":
			return certificate.manufacturerPrice * 0.07;
		default:
			return 0;
		}
	}

	private double calculateRetailPrice() {
		return certificate.manufacturerPrice + storageFee + transportationFee + warrantyFee + emergencyFee + colorFee;
	}

	private String revealCode() {
		switch (certificate.model) {
		case "M1":
			return "0000";
		case "M2":
			return revealCodeForM2();
		case "M3":
			return revealCodeForM3();
		default:
			return "";
		}
	}

	private String revealCodeForM2() {
		StringBuilder code = new StringBuilder();
		int index;
		for (char ch : certificate.code.toCharArray()) {
			index = (ch - 'A' + 1) % 10;
			code.append(index);
		}
		return code.toString();
	}

	private String revealCodeForM3() {
		int[] indices = new int[4];
		for (int i = 0; i < 4; i++) {
			indices[i] = (certificate.code.charAt(i * 2) - 'A' + 1) + (certificate.code.charAt(i * 2 + 1) - 'A' + 1);
			indices[i] %= 10;
		}
		StringBuilder code = new StringBuilder();
		for (int i = 3; i >= 0; i--) {
			code.append(indices[i]);
		}
		return code.toString();
	}

	public void printOutput() {
		System.out.println("OUTPUT BY CONSUMER: " + certificate);
		System.out.printf("Storage fee (%%) = $%.2f, Transportation fee (%%) = $%.2f%n", storageFee, transportationFee);
		System.out.printf("Warranty fee = $%.2f, Emergency fee = $%.2f, Color fee (%%) = $%.2f%n", warrantyFee,
				emergencyFee, colorFee);
		System.out.printf("Retail price = $%.2f, Revealed code: %s%n", retailPrice, revealedCode);
	}
}
