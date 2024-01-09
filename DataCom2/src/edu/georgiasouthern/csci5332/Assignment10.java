package edu.georgiasouthern.csci5332;

public class Assignment10 {
	 public static int[][] hadamard(int k) {
	        int n = (int) Math.pow(2, k);
	        int[][] matrix = new int[n][n];

	        if (k == 0) {
	            matrix[0][0] = 1;
	            return matrix;
	        }

	        int[][] smallerMatrix = hadamard(k - 1);

	        for (int i = 0; i < n; i++) {
	            for (int j = 0; j < n; j++) {
	                matrix[i][j] = smallerMatrix[i % (n / 2)][j % (n / 2)];
	                if (i >= n / 2) {
	                    matrix[i][j] *= -1;
	                }
	                if (j >= n / 2) {
	                    matrix[i][j] *= -1;
	                }
	            }
	        }

	        return matrix;
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