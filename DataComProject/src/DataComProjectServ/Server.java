package DataComProjectServ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<connectionHandler> connections;
	private ServerSocket server;
	private boolean done;
	// thread pool
	private ExecutorService pool;

	public Server() {
		connections = new ArrayList<>();
		done = false;
	}

	// override the run function of the runnable class
	@Override
	public void run() {
		try {
			server = new ServerSocket(8888);
			pool = Executors.newCachedThreadPool();

			while (!done) {
				Socket client = server.accept();
				connectionHandler handler = new connectionHandler(client);
				connections.add(handler);
				// runs the run function
				pool.execute(handler);
			}
		} catch (Exception ex) {
			shutdown();
		}
	}

	// broadcasts the message
	public void Broadcast(String message) {
		for (connectionHandler ch : connections) {
			// ensure the
			if (ch != null) {
				ch.sendMessage(message);
			}
		}
	}

	public void shutdown() {
		try {
			done = true;
			pool.shutdown();
			if (!server.isClosed()) {
				server.close();
			}
			for (connectionHandler ch : connections) {
				ch.shutdown();
			}
		} catch (IOException ex) {
			// Ignore it
		}
	}

	class connectionHandler implements Runnable {
		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private String username;

		public connectionHandler(Socket S) {
			this.client = S;
		}

		@Override
		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				// prompt the user for a name they would like to use for chat
				out.println("Please enter a name: ");
				// set the user name variable to the input
				username = in.readLine();
				System.out.println(username + ": connected");
				// confirms that the user name has connected to the chat
				Broadcast(username + " has Joined the chat!");
				// placeholder for messages
				String message;

				while ((message = in.readLine()) != null) {
					if (message.startsWith("/user")) {
						String[] messageSplit = message.split(" ", 2);
						if (messageSplit.length == 2) {
							Broadcast(username + " has been renamed to: " + messageSplit[1]);
							username = messageSplit[1];
						} else {
							out.println("no username provided!");
						}
					} else if (message.startsWith("/Quit")) {
						Broadcast(username + " has left the server!");
						shutdown();
					} else {
						Broadcast(username + ": " + message);
					}
				}
			} catch (IOException e) {
				shutdown();
			}
		}

		public void sendMessage(String message) {
			out.println(message);
		}

		public void shutdown() {
			try {
				in.close();
				out.close();
				if (!client.isClosed()) {
					client.close();
				}
			} catch (IOException d) {
				// ignore
			}
		}
	}

	public static void main(String[] args) {
		Server s = new Server();
		s.run();
	}

}