package com.rledford.impinj.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class CommandHandler {
	
	private int CONNECTION_TIMEOUT = 3000;//milliseconds
	private JSch jsch;
	
	public static class Params {
		public String host = "localhost";
		public String username = "root";
		public String password = "impinj";
		public int port = 22;
		
		public String command = "";
		public ThreadListener listener = null;
		
		public Params (String host, String username, String password, int port, String command, ThreadListener listener) {
			this.host = host;
			this.username = username;
			this.password = password;
			this.port = port;
			this.command = command;
			this.listener = listener;
		}
	}
	
	//Constructor
	public CommandHandler() {
		jsch = new JSch();
	}
	
	/*
	private void jcabiSSH(Params params) {
		params.listener.onMessage("* " + params.command + " *", true);
		try {
			final SSHByPassword ssh = new SSHByPassword(params.host, params.port, params.username, params.password);
			Plain shell = new Shell.Plain(ssh);
			String output = shell.exec(params.command).toString();
			params.listener.onMessage(output, false);
		} catch (UnknownHostException e){
			params.listener.onMessage(e.getMessage(), false);
		} catch (IOException io) {
			params.listener.onMessage(io.getMessage(), false);
		}
	}
	*/
	
	private void jschSSH(Params params) {
		params.listener.onMessage("* " + params.command + " *", true);
		Session session = null;
		ChannelExec channel = null;
		try {
			session = jsch.getSession(params.username, params.host, params.port);
			session.setPassword(params.password);
			Properties props = new Properties();
			props.put("StrictHostKeyChecking", "no");
			session.setConfig(props);
			session.connect(CONNECTION_TIMEOUT);
			channel = (ChannelExec) session.openChannel("exec");
			BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			System.out.println("commands: "+params.command);
			channel.setCommand(params.command);
			channel.connect(CONNECTION_TIMEOUT);
			
			String message = "";
			String chunk = null;
			while((chunk = in.readLine()) != null) {
				message += chunk+"\n";
			}
			
			channel.disconnect();
			session.disconnect();
			params.listener.onMessage(message, false);
			System.out.println(message);
		}
		catch (Exception e) {
			System.out.println("an error occured");
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
			if (params.listener != null) {
				params.listener.onMessage(e.getMessage().toString(), false);
			}
		}
	}
	
	public void send(Params params) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//jcabiSSH(params);
				jschSSH(params);
			}
		});
		thread.start();
	}
}
