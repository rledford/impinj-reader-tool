package com.rledford.impinj.tools;

public class CommandHelper {
	public static final String ENABLE_NTP = "config network ntp enable";
	public static final String DISABLE_NTP = "config network ntp disable";
	public static final String ENABLE_NTP_DYNAMIC_SERVERS = "config network ntp dynamicservers enable";
	public static final String DISABLE_NTP_DYNAMIC_SERVERS = "config network ntp dynamicservers disable";
	public static final String ADD_NTP_IP = "config network ntp add {ip}";
	public static final String DEL_NTP_IP = "config network ntp del {ip}";
	public static final String DEL_ALL_NTP_SERVERS = "config network ntp delall";
	public static final String SHOW_NET_SUMMARY = "show network summary";
	public static final String SHOW_NET_NTP = "show network ntp";
	public static final String SHOW_NET_DHCP = "show network dhcp";
	public static final String SHOW_NET_FTP = "show network ftp";
	public static final String SHOW_NET_SFTP = "show network sftp";
	public static final String SET_STATIC_IP = "config network ip static {ip} {mask} {gateway} {bcast}";
	public static final String ENABLE_DHCP = "config network ip dhcp";
}
