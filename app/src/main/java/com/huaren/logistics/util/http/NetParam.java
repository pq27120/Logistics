package com.huaren.logistics.util.http;

import android.content.Context;
import android.os.Handler;
import com.huaren.logistics.common.Constant;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/** 网络连接参数对象 */
public class NetParam {
	private Context context;
	private String url;
	private Handler netHandler;
	private int result;
	private boolean cachesFlag = false;
	private int channeltype = 0; // get, post, img
	private int netType = 0; // wifi,其它
	public String urlKey;
	public int urlKeyType = 1; // 0: 截取url中'?'前部分; 1 取整个url
	public ArrayList<File> files;
	public  Map params;
	public NetParam() {
	}

	/** get方式 */
	public NetParam(Context ctt, String url, Handler handler, int result) {
		this.context = ctt;
		this.url = Constant.HTTP_URL + url;
		this.netHandler = handler;
		this.result = result;
		this.channeltype = NetConnect.TYPE_GET;
		setUrlKey();
	}

	/** post方式 */
	public NetParam(Context ctt, String url, Map params, Handler handler, int result) {
		this.context = ctt;
		this.url = Constant.HTTP_URL + url;
		this.params=params;
		this.netHandler = handler;
		this.result = result;
		this.channeltype = NetConnect.TYPE_POST;
		setUrlKey();
	}

	/** post方式 */
	public NetParam(Context ctt, String url,   Map params,ArrayList<File> files, Handler handler, int result) {
		this.context = ctt;
		this.url = Constant.HTTP_URL + url;
		this.netHandler = handler;
		this.result = result;
		this.files=files;
		this.channeltype = NetConnect.TYPE_POST_FILE;
		this.params=params;
		setUrlKey();
	}

	public void setUrlKey() {
		if (urlKeyType == 0) {
			int pos = url.indexOf("?");
			if (pos < 1) {
				pos = url.length();
			}
			urlKey = url.substring(0, pos);
		} else {
			urlKey = url;
		}
	}

	public int getChanneltype() {
		return channeltype;
	}

	public void setChanneltype(int channeltype) {
		this.channeltype = channeltype;
	}

	public int getNetType() {
		return netType;
	}

	public void setNetType(int netType) {
		this.netType = netType;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public Handler getNetHandler() {
		return netHandler;
	}

	public void setNetHandler(Handler netHandler) {
		this.netHandler = netHandler;
	}

	/** 连接成功的int标志 */
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public boolean isCachesFlag() {
		return cachesFlag;
	}

	public void setCachesFlag(boolean cachesFlag) {
		this.cachesFlag = cachesFlag;
	}
}
