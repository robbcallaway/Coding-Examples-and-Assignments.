package edu.georgiasouthern.csci5332;

public class Assignment10 {

	public static int[][] hadamard(int k) {

		if (k == 0) {
			return new int[][] { { 1 } };
		}

		else {

			int[][] h = hadamard(k - 1);

			int n = h.length;

			int[][] result = new int[2 * n][2 * n];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					result[i][j] = h[i][j];
					result[i][j + n] = h[i][j];
					result[i + n][j] = h[i][j];
					result[i + n][j + n] = -h[i][j];
				}
			}

			return result;
		}
	}

	public static void main(String[] args) {
		int[][] h = hadamard(3);
		for (int i = 0; i < h.length; i++) {
			for (int j = 0; j < h[i].length; j++) {
				System.out.printf("%3d", h[i][j]);
			}
			System.out.println();
		}
	}
}
