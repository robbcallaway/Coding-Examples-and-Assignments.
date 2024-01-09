package Alg;

import java.util.Scanner;
import java.util.Stack;

public class Cost {
	 public static void main(String[] args) {
	        try (Scanner sc = new Scanner(System.in)) {
				int n = sc.nextInt(); // number of trading posts
				int[][] cost = new int[n+1][n+1]; // cost matrix
				int[] dp = new int[n+1]; // DP table to store solutions to subproblems
				int[] prev = new int[n+1]; // table to store the previous trading post for each optimal solution

				// Read input cost matrix
				for (int i = 1; i < n; i++) {
				    for (int j = i+1; j <= n; j++) {
				        cost[i][j] = sc.nextInt();
				    }
				}

				// DP base case
				dp[1] = 0;

				// DP recurrence relation
				for (int i = 2; i <= n; i++) {
				    dp[i] = Integer.MAX_VALUE;
				    for (int j = 1; j < i; j++) {
				        int newCost = dp[j] + cost[j][i];
				        if (newCost < dp[i]) {
				            dp[i] = newCost;
				            prev[i] = j;
				        }
				    }
				}

				// Output DP table
				System.out.println("DP table:");
				for (int i = 1; i <= n; i++) {
				    System.out.print(dp[i] + " ");
				}
				System.out.println();

				// Output optimal solution value
				System.out.println("Optimal solution value: " + dp[n]);

				// Trace DP table to find optimal solution
				System.out.println("Optimal solution:");
				int post = n;
				Stack<Integer> solution = new Stack<Integer>();
				while (post != 0) {
				    solution.push(post);
				    post = prev[post];
				}
				System.out.print("Rent canoe at post 1");
				while (!solution.isEmpty()) {
				    int dropOff = solution.pop();
				    System.out.print(", drop off at post " + dropOff + ", cost = " + cost[prev[dropOff]][dropOff]);
				}
			}
	        System.out.println();
	    }
	}

