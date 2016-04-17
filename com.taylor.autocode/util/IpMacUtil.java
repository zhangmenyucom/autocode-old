package com.taylor.api.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class IpMacUtil {

	private static final Logger LOGGER = Logger.getLogger(IpMacUtil.class);

	private static DatagramSocket ds = null;

	/**
	 * @notes:获取客户端真实IP
	 * 
	 * @param request
	 * @author taylor 2014-7-28 上午10:58:06
	 */
	public static String getRealIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		// ipAddress = request.getRemoteAddr();
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if ("127.0.0.1".equals(ipAddress)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					LOGGER.error("《《《《《调用IpMacUtil中的getRealIpAddr方法异常！》》》》》", e);
				} catch (Exception e) {
					LOGGER.error("《《《《《《《《调用IpMacUtil中的getRealIpAddr方法异常》》》》》》》》", e);
				}
				if(inet != null){
					ipAddress = inet.getHostAddress();
				}
			}
		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) { // "***.***.***.***".length()
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}
		return ipAddress;
	}

	/**
	 * 获取当前操作系统名称. return 操作系统名称 例如:windows xp,linux 等.
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	/**
	 * 获取unix网卡的mac地址. 非windows的系统默认调用本方法获取. 如果有特殊系统请继续扩充新的取mac地址方法.
	 * 
	 * @return mac地址
	 */
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			// linux下的命令，一般取eth0作为本地主网卡
			process = Runtime.getRuntime().exec("ifconfig eth0");
			// 显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				// 寻找标示字符串[hwaddr]
				index = line.toLowerCase().indexOf("hwaddr");
				if (index >= 0) {// 找到了
					// 取出mac地址并去除2边空格
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (IOException e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getUnixMACAddress方法异常》》》》》》》》", e);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e1) {
				LOGGER.error("《《《《《《《《调用BufferedReader中的getUnixMACAddress方法异常》》》》》》》》", e1);
			}
			process = null;
		}
		return mac;
	}

	/**
	 * 获取widnows网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			// windows下的命令，显示信息中包含有mac地址信息
			process = Runtime.getRuntime().exec("ipconfig /all");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				// 寻找标示字符串[physical
				index = line.toLowerCase().indexOf("physical address");

				if (index >= 0) {// 找到了
					index = line.indexOf(":");// 寻找":"的位置
					if (index >= 0) {
						// 取出mac地址并去除2边空格
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getWindowsMACAddress方法异常》》》》》》》》", e);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e1) {
				LOGGER.error("《《《《《《《《调用BufferedReader中的getWindowsMACAddress方法异常》》》》》》》》", e1);
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * windows 7 专用 获取MAC地址
	 * 
	 * @throws Exception
	 */
	public static String getMACAddress(){

		// 获取本地IP对象
		InetAddress ia;
		try {
			ia = InetAddress.getLocalHost();
			// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

			// 下面代码是把mac地址拼装成String
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				// mac[i] & 0xFF 是为了把byte转化为正整数
				String s = Integer.toHexString(mac[i] & 0xFF);
				sb.append(s.length() == 1 ? 0 + s : s);
			}
			// 把字符串所有小写字母改为大写成为正规的mac地址并返回
			return sb.toString().toUpperCase();
		} catch (UnknownHostException e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getMACAddress方法异常》》》》》》》》", e);
		} catch (SocketException e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getMACAddress方法异常》》》》》》》》", e);
		} catch (Exception e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getMACAddress方法异常》》》》》》》》", e);
		}
		return null;
	}

	/**
	 * @notes:获取服务器端MAC
	 * 
	 * @author taylor 2014-7-28 下午4:46:00
	 */
	public String getServerMac() {
		String mac = "";
		try {
			String os = getOSName();
			if (os.equals("windows 7")) {
				mac = getMACAddress();
			} else if (os.startsWith("windows")) {
				// 本地是windows
				mac = getWindowsMACAddress();
			} else {
				// 本地是非windows系统 一般就是unix
				mac = getUnixMACAddress();
			}
		} catch (Exception e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getServerMac方法异常》》》》》》》》", e);
		}
		return mac;
	}

	// 发送数据包
	protected static final DatagramPacket send(String ipAddress, final byte[] bytes){
		DatagramPacket dp = null;
		try {
			dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ipAddress), 137);
			if (null == ds || ds.isClosed()) {
				ds = new DatagramSocket();
			}
			ds.send(dp);
			return dp;
		} catch (UnknownHostException e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的DatagramPacket方法异常》》》》》》》》", e);
		} catch (IOException e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的DatagramPacket方法异常》》》》》》》》", e);
		} catch (Exception e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getMACAddress方法异常》》》》》》》》", e);
		}
		return dp;
	}

	// 接收数据包
	protected static final DatagramPacket receive() {
		byte[] buffer = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
		try {
			ds.setSoTimeout(3000);
			ds.receive(dp);
		} catch (SocketTimeoutException ex) {
			LOGGER.error("接收数据超时...,不能获取客户端MAC地址", ex);
		} catch (SocketException e1) {
			LOGGER.error("发生Sorcket异常..." + e1.getMessage(), e1);
		} catch (IOException e2) {
			LOGGER.error("发生IO异常..." + e2.getMessage(), e2);
		}
		return dp;
	}

	// 询问包结构:
	// Transaction ID 两字节（16位） 0x00 0x00
	// Flags 两字节（16位） 0x00 0x10
	// Questions 两字节（16位） 0x00 0x01
	// AnswerRRs 两字节（16位） 0x00 0x00
	// AuthorityRRs 两字节（16位） 0x00 0x00
	// AdditionalRRs 两字节（16位） 0x00 0x00
	// Type:NBSTAT 两字节 0x00 0x21
	// Class:INET 两字节（16位）0x00 0x01
	protected static byte[] getQueryCmd(){
		byte[] t_ns = new byte[50];
		t_ns[0] = 0x00;
		t_ns[1] = 0x00;
		t_ns[2] = 0x00;
		t_ns[3] = 0x10;
		t_ns[4] = 0x00;
		t_ns[5] = 0x01;
		t_ns[6] = 0x00;
		t_ns[7] = 0x00;
		t_ns[8] = 0x00;
		t_ns[9] = 0x00;
		t_ns[10] = 0x00;
		t_ns[11] = 0x00;
		t_ns[12] = 0x20;
		t_ns[13] = 0x43;
		t_ns[14] = 0x4B;

		for (int i = 15; i < 45; i++) {
			t_ns[i] = 0x41;
		}

		t_ns[45] = 0x00;
		t_ns[46] = 0x00;
		t_ns[47] = 0x21;
		t_ns[48] = 0x00;
		t_ns[49] = 0x01;
		return t_ns;
	}

	// 表1 “UDP－NetBIOS－NS”应答包的结构及主要字段一览表
	// 序号 字段名 长度
	// 1 Transaction ID 两字节（16位）
	// 2 Flags 两字节（16位）
	// 3 Questions 两字节（16位）
	// 4 AnswerRRs 两字节（16位）
	// 5 AuthorityRRs 两字节（16位）
	// 6 AdditionalRRs 两字节（16位）
	// 7 Name<Workstation/Redirector> 34字节（272位）
	// 8 Type:NBSTAT 两字节（16位）
	// 9 Class:INET 两字节（16位）
	// 10 Time To Live 四字节（32位）
	// 11 Length 两字节（16位）
	// 12 Number of name 一个字节（8位）
	// NetBIOS Name Info 18×Number Of Name字节
	// Unit ID 6字节（48位
	protected static final String getMacAddr(byte[] brevdata){
		// 获取计算机名
		int i = brevdata[56] * 18 + 56;
		String sAddr = "";
		StringBuilder sb = new StringBuilder(17);
		// 先从第56字节位置，读出Number Of Names（NetBIOS名字的个数，其中每个NetBIOS Names Info部分占18个字节）
		// 然后可计算出“Unit ID”字段的位置＝56＋Number Of Names×18，最后从该位置起连续读取6个字节，就是目的主机的MAC地址。
		for (int j = 1; j < 7; j++) {
			sAddr = Integer.toHexString(0xFF & brevdata[i + j]);
			if (sAddr.length() < 2) {
				sb.append(0);
			}
			sb.append(sAddr.toUpperCase());
			if (j < 6)
				sb.append('-');
		}
		return sb.toString();
	}

	public static final void close() {
		try {
			ds.close();
		} catch (Exception ex) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的close方法异常》》》》》》》》", ex);
		}
	}

	/**
	 * 获取远程主机的mac地址
	 * 
	 * @throws Exception
	 */
	public static final String getRemoteMacAddr(String ip) {
		try {

			byte[] bqcmd = getQueryCmd();
			send(ip, bqcmd);
			DatagramPacket dp = receive();
			String smac = "";
			smac = getMacAddr(dp.getData());
			close();
			return smac;
		} catch (Exception e) {
			LOGGER.error("《《《《《《《《调用BufferedReader中的getRemoteMacAddr方法异常》》》》》》》》", e);
		}
		return "";
	}
}
