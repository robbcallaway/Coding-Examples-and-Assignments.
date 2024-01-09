package Homework;

import java.util.Scanner;

public class Assignment5 {
	public static void main(String[] args) {
		int n;
		try (Scanner sc = new Scanner(System.in)) {
			n = sc.nextInt();

			int cost[][] = new int[n + 1][n + 1];

			for (int i = 1; i < n; ++i) {
				for (int j = i + 1; j <= n; ++j) {
					cost[i][j] = sc.nextInt();
				}
			}

			int par[] = new int[n + 1];
			for (int i = 1; i <= n; ++i)
				par[i] = 1;

			int dp[] = new int[n + 1];
			for (int i = 1; i <= n; ++i)
				dp[i] = 1000000000;

			dp[1] = 0;

			for (int i = 2; i <= n; ++i) {
				for (int j = 1; j < i; ++j) {
					if (dp[i] > cost[j][i] + dp[j]) {
						dp[i] = cost[j][i] + dp[j];
						par[i] = j;

					}
				}
			}

			System.out.println("Optimal solution for each subproblem ( cost to reach 1...n) is : ");
			for (int i = 1; i <= n; ++i) {
				System.out.print(" " + dp[i]);
			}

			System.out.println(" ");

			System.out.println("Optimal cost to reach 1 to n is : " + dp[n]);

			printPath(par, n);
		}
		System.out.println("");

	}

	public static void printPath(int[] par, int u) {
		if (u == 1) {
			System.out.print("1");
			return;
		}

		printPath(par, par[u]);
		System.out.print(" -> " + u);

	}

	public static void printPath(int[] par, int u, int dp[]) {
		if (u == 1) {
			System.out.print("1");
			return;
		}

		printPath(par, par[u], dp);
		System.out.print(" -> " + u + " ($" + (dp[u] - dp[u - 1]) + ")");

	}
}
