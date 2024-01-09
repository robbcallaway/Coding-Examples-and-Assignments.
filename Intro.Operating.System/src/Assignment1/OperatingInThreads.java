package Assignment1;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class OperatingInThreads {
	public static void main(String[] args) {
		String section1 = "";
		String section2 = "";
		String section3 = "";
		Thread Reveal = new Thread();
		Thread Expose = new Thread();
		Thread Discover = new Thread();
		// text file to be read
		Path FILE_NAME = Path.of("C:\\Intro.OperatingSystems txt file..txt");
		try {
			// reads the file
			Scanner scnr = new Scanner(FILE_NAME);
			while (scnr.hasNext()) {
				String currLine = scnr.nextLine();
				int firstIn = 0;
				int starcount = 0;
				for (int i = 0; i < currLine.length(); i++) {
					char c = currLine.charAt(i);
					if (c == '*') {
						starcount++;
						firstIn = i;
					}
					if (c == '$') {
						String s = currLine.substring(firstIn + 1, i);
						// if statement that creates initializes the section1 string
						if (starcount == 1) {
							section1 = s;
							section1 = section1.trim();
							Runnable r = new revealS(section1);
							Reveal = new Thread(r);
							System.out.print("Reveal: ");
							Reveal.run();
						} else if (starcount == 2) {
							section2 = s;
							section2 = section2.trim();
							Runnable r = new exposeTask(section2);
							Expose = new Thread(r);
							System.out.print("Expose: ");
							Expose.run();
						} else if (starcount == 3) {
							section3 = s;
							section3 = section3.trim();
							Runnable r = new discoverTask(section3);
							Discover = new Thread(r);
							System.out.print("Discover: ");
							Discover.run();
						}
						starcount = 0;
					}
				}
			}
			scnr.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}

class revealS implements Runnable {
	String s1;
	String[] c;

	revealS(String s) {
		s1 = s;
		// replace the commas with " " to make it possible to split later
		s1 = s1.replaceAll(", ", " ");
		s1 = s1.replaceAll(",", " ");
		s1 = s1.replaceAll(" ,", " ");
		c = s1.split(" ");
	}

	@Override
	public void run() {
		// create an int array for the numbers to be calculated [modulo] % 26
		int[] modulo = new int[6];
		// alphabet used to print the m
		char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		int total = 0;
		String er = "";
		String message = "";

		for (int i = 0; i < c.length; i++) {
			er = er + c[i] + " ";

			try {
				modulo[i] = Integer.parseInt(c[i]);
			} catch (NumberFormatException e) {
				System.out.println("Error: Invalid * ");
				return;
			}
		}

		for (int i = 0; i < c.length; i++) {
			er = er + c[i] + " ";
			modulo[i] = Integer.parseInt(c[i]);
		}
		// get the total to subtract
		for (int j = 0; j < modulo.length; j++) {
			total += modulo[j];
		}
		int mod = total % 26;
		int[] alph = new int[6];
		try {
			for (int k = 0; k < modulo.length; k++) {
				alph[k] = (mod - modulo[k]) % 26;
				message = message + alphabet[alph[k]];
				System.out.print(alphabet[alph[k]] + " ");
			}
			System.out.print("Section 1 is : " + message + " -This is valid");
		} catch (ArrayIndexOutOfBoundsException e) {
			// if an out of bounds error is detected we exit the thread
			System.out.print("Section 1 is : " + er + " -This section is invalid");
		}
		System.out.println();
	}
}

class discoverTask implements Runnable {
	String x;
	String[] sA;

	discoverTask(String s) {
		x = s;
		// turns the string into a string array
		sA = x.split(" ");
	}

	@Override
	public void run() {

		String valid = "";

		// turns written numbers into digits
		for (int i = 0; i < sA.length; i++) {
			if (sA[i].contains("one")) {
				sA[i] = "1";
			} else if (sA[i].contains("two")) {
				sA[i] = "2";
			} else if (sA[i].contains("three")) {
				sA[i] = "3";
			} else if (sA[i].contains("four")) {
				sA[i] = "4";
			} else if (sA[i].contains("five")) {
				sA[i] = "5";
			} else if (sA[i].contains("six")) {
				sA[i] = "6";
			} else if (sA[i].contains("seven")) {
				sA[i] = "7";
			} else if (sA[i].contains("eight")) {
				sA[i] = "8";
			} else if (sA[i].contains("nine")) {
				sA[i] = "9";
			}
		}

		// creates the initial password
		String password = "";
		for (int i = 0; i < sA.length; i++) {
			if (Character.isUpperCase(sA[i].charAt(0)) == false) {
				password += sA[i].charAt(0);
			}
		}

		// checks if the password is longer than 8 characters.
		if (password.length() > 8) {
			String test = password.substring(0, 8);
			if (discoverPasswordTest(test)) {
				password = test;
				valid = "valid";
			} else {
				String reverse = "";
				// reverse the string
				for (int i = password.length() - 1; i >= 0; i--) {
					reverse += password.charAt(i);
				}
				test = reverse.substring(0, 8);

				if (discoverPasswordTest(test)) {
					password = test;
					valid = "valid";
				} else {
					password = test;
					valid = "invalid";
				}
			}
		} else {
			if (discoverPasswordTest(password)) {
				valid = "valid";
			} else {
				valid = "invalid";
			}
		}

		System.out.println("Discover - Section 3 is: " + x + " - password is: " + password + " - " + valid);
	}

	static boolean discoverPasswordTest(String password) {
		if (Character.isDigit(password.charAt(0))) {
			return false;
		}
		if (password.length() <= 1) {
			return false;
		}
		for (int i = 1; i < password.length(); i++) {
			if (Character.isDigit(password.charAt(i))) {
				return true;
			}
		}
		return false;
	}
}

class exposeTask implements Runnable {
	static String x;
	String[] sX;

	static String part1 = "";
	static String part2 = "";
	//REJEX
	static char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	static int[][] modAns = new int[2][1];

	static String finalResult = "";

	exposeTask(String input) {
		x = input;
		sX = x.split("s");
	}

	public void run() {

		boolean foundSpace = false;
		int index = 0;

		for (int i = 0; i < x.length() && foundSpace == false; i++) {
			if (x.charAt(i) == ' ') {
				index = i;
				foundSpace = true;
			}
		}
		part1 = x.substring(0, index);
		part2 = x.substring(index + 1, x.length());
		String[] pt1SA = part1.split("");
		String[] pt2SA = part2.split("[ $]+");
		int[] pt1 = new int[part1.length()];

		//INVALID/VALID if statements. 
		int pt1M[][] = new int[part1.length()][part1.length()];

		if ((pt1SA.length % 2) != 0) {
			System.out.println("Expose - Section 2 is: " + x + " - invalid");
			return;
		}
		if ((part1.matches("[a-zA-Z]+") != true)) {
			System.out.println("Expose - Section 2 is: " + x + " - invalid");
			return;
		}
		if (pt2SA.length != 4) {
			System.out.println("Expose - Section 2 is: " + x + " - invalid");
			return;
		}
		for (int i = 0; i < pt2SA.length; i++) {
			try {
				Integer.parseInt(pt2SA[i]);
			} catch (NumberFormatException e) {
				System.out.println("Expose - Section 2 is: " + x + " - invalid");
				return;
			}
		}

		for (int t = 0; t < pt1.length / 2; t++) {
			exposeOperation(pt1, pt1M, pt2SA, t * 2);
		}
		System.out.println("Expose - Section 2 is: " + x + " - " + finalResult + " - valid");
		finalResult = "";

	}

	static void exposeOperation(int[] pt1, int[][] pt1M, String[] pt2SA, int offset) {
		for (int i = 0; i < part1.length(); i++) {
			pt1[i] = part1.charAt(i);

			for (int j = 0; j < part1.length(); j++) {
				pt1M[j][0] = pt1[j] - 'A';
			}

		}
		//Multiplication of two matrices. 2x2.
		int[][] matrix = new int[2][2];
		int Y = 0;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				try {
					matrix[i][j] = Integer.parseInt(pt2SA[Y]);
					Y++;
				} catch (NumberFormatException e) {
					System.err.println("Expose - Section 2 is: " + x + " - Invalid ");
					return;
				}
			}
		}

		multiplyMatrix(2, 2, matrix, 2, 1, pt1M, offset);

	}

	static void multiplyMatrix(int row1, int col1, int A[][], int row2, int col2, int B[][], int offset) {
		int i, j, k;
		// Check if multiplication is Possible
		if (row2 != col1) {

			System.out.println("\nMultiplication Not Possible");
			return;
		}

		// Matrix to store the result
		// The product matrix will

		int C[][] = new int[row1][col2];

		// Multiply the two matrices
		for (i = 0; i < row1; i++) {
			for (j = 0; j < col2; j++) {
				for (k = 0; k < row2; k++) {
					C[i][j] += A[i][k] * B[k + offset][j];
				}
			}
		}

		for (int P = 0; P < 2; P++) {
			for (j = 0; j < 1; j++) {
				modAns[P][j] = C[P][j] % 26;
			}
		}

		for (int x = 0; x < modAns.length; x++) {
			for (int y = 0; y < modAns[x].length; y++) {
				finalResult += alphabet[modAns[x][y]];
			}
		}
	}
}
