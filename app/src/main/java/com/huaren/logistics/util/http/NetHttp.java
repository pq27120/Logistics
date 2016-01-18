package com.huaren.logistics.util.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONObject;

/**
 * HTTP连接处理
 */
public class NetHttp extends Thread {
	private final int WIFIAndCMNET = 1;
	private final int CMWAP = 2;

	private HttpURLConnection httpconnection;
	private ByteArrayInputStream resultStream;
	private String content; // 获取到的原始数据
	private boolean isCancel = false; // 是否取消连接
	private String proxyHost; // 代理主机
	private int proxyPort; // 代理端口

	/** 参数与返回数据 */
	private NetParam netParam;
	private Handler parentHandler;
	private String dataMsg;

	private static final String BOUNDARY = UUID.randomUUID().toString();// 边界标识、随机生成、数据分割线
	private static final String PREFIX = "--"; // 前缀
	private static final String LINE_END = "\r\n"; // 一行的结束标识
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	public NetHttp(NetParam netParam) {
		this.netParam = netParam;
	}

	// 线程执行
	public void run() {
		try {
			getNetData();
		} catch (Exception e) {
			CommonTool.showLog(e.getMessage());
			e.printStackTrace();
			notifyUI(NetConnect.NET_ERROR, null);
		}
	}

	/** 连接网络获取数据 */
	public void getNetData() {
		// 判断网络环境
		netParam.setNetType(getNetTyle());
		boolean connectStatue = false;
		if (netParam.getChanneltype() == NetConnect.TYPE_GET) {// get
			connectStatue = get();
		} else if (netParam.getChanneltype() == NetConnect.TYPE_POST) {// post
			JSONObject jsonObject=new JSONObject(netParam.params);
			connectStatue = post(jsonObject);
		}else if (netParam.getChanneltype() == NetConnect.TYPE_POST_FILE) {// post
			JSONObject jsonObject=new JSONObject(netParam.params);
			connectStatue = postFiles( jsonObject,netParam.files);
		}
		if (connectStatue == true) {
			if (getNetContent()) {
				if (isCancel)
					return;
				// 取得数据
				notifyUI(netParam.getResult(), content);
				return;
			}
		}
		notifyUI(NetConnect.NET_ERROR, null);
	}

	/** 通知 响应界面UI与连接集合 */
	public void notifyUI(int resultState, String dataMsg) {
		CommonTool.showLog("dataMsg=" + dataMsg);
		if (isCancel)
			return;
		Message msg = new Message();
		msg.what = resultState;
		if (StringTool.isNotNull(dataMsg)) {
			msg.obj = dataMsg;
		}
		// 通知界面响应请求
		netParam.getNetHandler().sendMessage(msg);
		// 通知缓存清除该请求
		if (parentHandler != null) {
			msg = new Message();
			msg.what = 1;
			msg.obj = netParam;
			parentHandler.sendMessage(msg);
		}
	}

	// get方式
	private boolean get() {
		boolean getFlag = false;
		URL getUrl = null;
		try {
			if (netParam.getNetType() == 1) { // 直连
				getUrl = new URL(netParam.getUrl());
				httpconnection = (HttpURLConnection) getUrl.openConnection();
			} else {
				getUrl = new URL(getAgentURL(netParam.getUrl()));
				httpconnection = (HttpURLConnection) getUrl.openConnection();
				httpconnection.setRequestProperty("X-Online-Host", getDomain(netParam.getUrl()));
			}
			CommonTool.showLog("接口地址=" + getUrl);
			addHeaderVersion();
			httpconnection.setUseCaches(false);
			httpconnection.setRequestMethod("GET");
			httpconnection.setRequestProperty("Charset", "utf-8"); // 设置编码
			httpconnection.setConnectTimeout(NetConnect.CONNNET_TIMEOUT * 1000);
			httpconnection.connect();
			getFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getFlag;
	}

	/** 连接后获取内容 */
	private boolean getNetContent() {

		boolean getNetDataFlag = false;
		StringBuffer conbuffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(httpconnection.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				conbuffer.append(line);
			}
			content = conbuffer.toString();
			content = replaceHtmls(content);
			getNetDataFlag = true;
		} catch (Exception e) {
			CommonTool.showLog(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {

			}
			closeConnection();
		}
		return getNetDataFlag;
	}

	// 去掉某些html字符
	private String replaceHtmls(String content) {
		if (content.indexOf("<br/>") > 0) {
			content = content.replaceAll("<br/>", "\n");
		}
		if (content.indexOf("<br />") > 0) {
			content = content.replaceAll("<br />", "\n");
		}
		content = content.replaceAll("</?[p|P]/?>", "");
		return content;
	}

	// 获取代理地址
	private String getAgentURL(String iURL) {
		int start = "http://".length() + 1;
		int end = iURL.indexOf("/", start) + 1;
		String agentURL = "http://" + proxyHost + ":" + proxyPort + "/" + iURL.substring(end);
		return agentURL;
	}

	// 获取域名
	private String getDomain(String iURL) {
		int start = "http://".length();
		int end = iURL.indexOf("/", start);
		String domain = iURL.substring(start, end);
		return domain;
	}

	// 根据网络类型获用Post方式取数据放入到connection
	private boolean post(JSONObject jsonObject) {
		boolean postFlag = false;
		URL postUrl = null;
		try {
			if (netParam.getNetType() == 1) {
				postUrl = new URL(netParam.getUrl());
				httpconnection = (HttpURLConnection) postUrl.openConnection();
			} else {
				postUrl = new URL(getAgentURL(netParam.getUrl()));
				httpconnection = (HttpURLConnection) postUrl.openConnection();
				httpconnection.setRequestProperty("X-Online-Host", getDomain(netParam.getUrl()));
			}
			CommonTool.showLog("接口地址=" + postUrl + "?params" + jsonObject.toString());
			httpconnection.setReadTimeout(NetConnect.CONNNET_TIMEOUT * 1000);
			httpconnection.setConnectTimeout(NetConnect.CONNNET_TIMEOUT * 1000);
			httpconnection.setDoInput(true); // 允许输入流
			httpconnection.setDoOutput(true); // 允许输出流
			httpconnection.setUseCaches(false); // 不允许使用缓存
			httpconnection.setRequestMethod("POST"); // 请求方式
			httpconnection.setRequestProperty("Charset", "utf-8"); // 设置编码
			httpconnection.setRequestProperty("connection", "keep-alive");
			httpconnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			//当文件不为空，把文件包装并且上传
			DataOutputStream dos = new DataOutputStream(httpconnection.getOutputStream());
			StringBuffer sb = new StringBuffer();
			//以下是用于上传参数
			if (jsonObject != null) {
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"params")
						.append("\"")
						.append(LINE_END).append(LINE_END);
				sb.append(jsonObject.toString()).append(LINE_END);
			}
			// 写入参数信息
			dos.write(sb.toString().getBytes());
			// 请求结束符
			byte[] after = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			// 写结束符，代表该HTTP组包完毕
			dos.write(after);
			// 发送出去
			dos.flush();
			// 关闭流
			dos.close();
			postFlag=true;
		} catch (Exception e) {
			CommonTool.showLog(e.getMessage());
			e.printStackTrace();
		}
		return postFlag;
	}
	public  boolean  postFiles(JSONObject jsonObject,
									  ArrayList<File> files) {
		boolean postFlag = false;
		URL postUrl = null;
		try {
			if (netParam.getNetType() == 1) {
				postUrl = new URL(netParam.getUrl());
				httpconnection = (HttpURLConnection) postUrl.openConnection();
			} else {
				postUrl = new URL(getAgentURL(netParam.getUrl()));
				httpconnection = (HttpURLConnection) postUrl.openConnection();
				httpconnection.setRequestProperty("X-Online-Host", getDomain(netParam.getUrl()));
			}
			CommonTool.showLog("接口地址=" + postUrl + "==" + jsonObject.toString()+"==file="+files);
			httpconnection.setReadTimeout(NetConnect.CONNNET_TIMEOUT * 1000);
			httpconnection.setConnectTimeout(NetConnect.CONNNET_TIMEOUT * 1000);
			httpconnection.setDoInput(true); // 允许输入流
			httpconnection.setDoOutput(true); // 允许输出流
			httpconnection.setUseCaches(false); // 不允许使用缓存
			httpconnection.setRequestMethod("POST"); // 请求方式
			httpconnection.setRequestProperty("Charset", "utf-8"); // 设置编码
			httpconnection.setRequestProperty("connection", "keep-alive");
			httpconnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			//当文件不为空，把文件包装并且上传
			DataOutputStream dos = new DataOutputStream(httpconnection.getOutputStream());

			StringBuffer sb = new StringBuffer();


			 //以下是用于上传参数
			if (jsonObject != null) {
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"params")
						.append("\"")
						.append(LINE_END).append(LINE_END);
				sb.append(jsonObject.toString()).append(LINE_END);
			}

			// 写入参数信息
			dos.write(sb.toString().getBytes());

			/**
			 * 构造要上传文件的前段参数内容，和普通参数一样，在这些设置后就可以紧跟文件内容了。 这里重点注意：
			 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的
			 * 比如:abc.png
			 */

			if (files != null) {
				for (File file : files) {
					sb = new StringBuffer();
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition:form-data; name=\"file"
							+ "\"; filename=\""
							+ file.getName() + "\"" + LINE_END);
					sb.append("Content-Type:image/jpeg" + LINE_END); // 这里配置的Content-type很重要的
					// ，用于服务器端辨别文件的类型的
					sb.append(LINE_END);
					// 写入文件前段参数信息
					dos.write(sb.toString().getBytes());
					// 写入文件数据
					InputStream is = new FileInputStream(file);
					byte[] bytes = new byte[1024];
					int len = 0;
					int count = 0;
					while ((len = is.read(bytes)) != -1) {
						count += len;
						dos.write(bytes, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());
				}
			}
			// 请求结束符
			byte[] after = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			// 写结束符，代表该HTTP组包完毕
			dos.write(after);
			// 发送出去
			dos.flush();
			// 关闭流
			dos.close();
			postFlag=true;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return postFlag;
	}
	/** 获取客户端的网络类型 */
	private int getNetTyle() {
		int type = WIFIAndCMNET;
		try {
			ConnectivityManager cm = (ConnectivityManager) netParam.getContext().getSystemService(
					Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null) {
				if (info.getType() == ConnectivityManager.TYPE_WIFI) { // Wifi
					type = WIFIAndCMNET;
				} else {
					proxyHost = android.net.Proxy.getDefaultHost();
					proxyPort = android.net.Proxy.getDefaultPort();
					if (proxyHost != null && proxyPort != -1) { // 获取运营商网络类型 wap
						type = CMWAP;
					} else {
						type = WIFIAndCMNET;
					}
				}
			}
		} catch (Exception e) {
		}
		return type;
	}

	public void setParentHandler(Handler parentHandler) {
		this.parentHandler = parentHandler;
	}

	private void closeConnection() {
		try {
			if (httpconnection != null) {
				httpconnection.disconnect();
				httpconnection = null;
			}
		} catch (Exception e) {
		}
	}

	private void closeBais() {
		try {
			if (resultStream != null) {
				resultStream.close();
				resultStream = null;
				content = null;
			}
		} catch (Exception e) {
		}
	}

	/** 取消连接 */
	public void cancelConnect() {
		isCancel = true;
		closeBais();
		closeConnection();
	}

	/**
	 * 添加请求头version
	 */
	public void addHeaderVersion() {
//		httpconnection.setRequestProperty("Authorization", "android<>" + Config.TOKEN);
	}
}