package cc.pp.sina.bozhus.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Nettool {
	
	/**
	 * 获取服务器局域网ip
	 */
	public static String getServerLocalIp() {

		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			String ip = "";
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = ips.nextElement().getHostAddress();
					if (ip.contains("192.168")) {
						return ip;
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取我的本机IP
	 */
	public static String getMyip() {

		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * 获取其它服务器IP
	 */
	public static String getMyServer(String url) {

		try {
			return InetAddress.getByName(url).getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

}

