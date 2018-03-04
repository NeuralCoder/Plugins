package com.neuralcoder.utilities.socketio;

import java.util.concurrent.CopyOnWriteArrayList;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import hudson.init.InitMilestone;
import hudson.init.Initializer;

public class SocketIOService {
	
	private static SocketIOServer server;
	
	private static CopyOnWriteArrayList<SocketIOClient> clients = new CopyOnWriteArrayList<SocketIOClient>();
	
	@Initializer(before = InitMilestone.COMPLETED)
	public static void init() {
		
		reset(8082);
		
	}
	
	private static void reset(int port) {
		
		System.out.println("stopping web server");
		
		if (server != null) {
			
			for (SocketIOClient con : clients) {
				
				con.disconnect();
				
			}
			
			clients.clear();
			
			server.stop();
			
		}
		
		System.out.println("start websocket server at " + port);
		
		Configuration config = new Configuration();
		config.setPort(port);
		
		server = new SocketIOServer(config);
		
		server.start();
		
		server.addConnectListener(new ConnectListener()
		{
			
			@Override
			public void onConnect(SocketIOClient client) {
				
				clients.add(client);
				
			}
			
		});
		
		server.addDisconnectListener(new DisconnectListener()
		{
			
			@Override
			public void onDisconnect(SocketIOClient client) {
				
				clients.remove(client);
				
			}
			
		});
		
	}
	
	public static void send(String message) {
		
		for (SocketIOClient client : clients) {
			
			client.sendEvent("message", message);
			
		}
	}
	
}
