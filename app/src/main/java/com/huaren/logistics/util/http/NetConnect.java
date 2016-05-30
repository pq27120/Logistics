package com.huaren.logistics.util.http;

import android.os.Handler;
import android.os.Message;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 网络连接
 */
public class NetConnect {
	/** 网络连接错误,或返回错误 */
	public final static int NET_ERROR = 999;
	public final static int DATA_ERROR = 998;
	/** 最大网络连接数 */
	public final static int NET_MAX_COUNT = 10;
	/** 连接超时:15秒 */
	public static int CONNNET_TIMEOUT = 15;

	private Handler handler;
	private Map<String, NetHttp> netHttpMap;

	/** 1:Get, 2:Post */
	public final static int TYPE_GET = 1, TYPE_POST = 2,TYPE_POST_FILE=3;

	public NetConnect() {
		netHttpMap = new Hashtable<String, NetHttp>();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					NetParam param = (NetParam) msg.obj;
					switch (msg.what) {
					case 1:
						netHttpMap.remove(param.urlKey);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	/** 新增网络连接 */
	public boolean addNet(NetParam netParam) {
		NetHttp netHttp = new NetHttp(netParam);
		netHttp.setParentHandler(handler);
		netHttpMap.put(netParam.urlKey, netHttp); // 加入连接集合
		netHttp.start(); // 开始连接获取数据
		return true;
	}

	/** 取消当前所有连接 */
	public void cancelAllConnect() {
		try {
			NetHttp netHttp = null;
			Set<String> keySet = netHttpMap.keySet();
			for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
				netHttp = netHttpMap.get(iterator.next());
				netHttp.cancelConnect();
			}
			netHttpMap.clear();
		} catch (Exception e) {
			netHttpMap.clear();
		}
	}
}
