package com.example.websocket;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.websocket.bean.JoinUser;
import com.example.websocket.bean.LeaveBean;
import com.example.websocket.bean.MqttMessageResponse;
import com.example.websocket.bean.PKType;
import com.example.websocket.bean.ResponeBean;
import com.example.websocket.bean.SendGiftData;
import com.example.websocket.bean.SendRealData;
import com.example.websocket.im.JWebSocketClient;
import com.example.websocket.observable.PkObservable;
import com.example.websocket.observable.TimeOutObservable;
import com.jkcq.util.ktx.ToastUtil;

import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WsManager {
    public JWebSocketClient client;
    private static WsManager mInstance;
    protected static Handler mHandler;
    protected Context context;

    volatile int connectCount = 0;

    static ParsePkInfo parse;

    private String mUserId = "";
    private String mRoomId = "";
    private String mToken = "";

    private final String TAG = this.getClass().getSimpleName();

    private WsManager() {
    }

    public static WsManager getInstance() {
        if (mInstance == null) {
            synchronized (WsManager.class) {
                if (mInstance == null) {
                    parse = new ParsePkInfo();
                    mInstance = new WsManager();
                    mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {

                                case 0x01:
                                    mInstance.errorreconnectWs();
                                    break;

                            }
                        }
                    };
                }
            }
        }
        return mInstance;
    }


    public boolean isConn() {
        if (client != null) {
            if (client.isClosed()) {
                return false;
            } else {
                return true;
            }
        } else {
            //如果client已为空，重新初始化连接
            return false;
        }
    }


    public void disConnect() {
        // mHandler.removeCallbacks(heartBeatRunnable);

        Log.e("disConnect", "disConnect" + client);
        mUserId = "";
        mToken = "";
        mRoomId = "";

        Util.pkEnd = true;
        if (client != null && client.isOpen()) {
            try {
                client.closeBlocking();
                client = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void innerdisConnect() {
        // mHandler.removeCallbacks(heartBeatRunnable);

        Log.e("disConnect", "innerdisConnect" + client);

        if (client != null && client.isOpen()) {
            try {
                client.closeBlocking();
                // client = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void connetSocket(String userId, String roomId, String token) {
        if (ToastUtil.isFastSockt(500)) {
            return;
        }
        mUserId = userId;
        mRoomId = roomId;
        mToken = token;

        if (client != null && client.isOpen()) {
            try {
                //  mHandler.removeCallbacks(heartBeatRunnable);
                client.closeBlocking();
                client = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            URI uri = URI.create(Util.ws + roomId + "/" + userId + "?token=" + token);
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态" + (Util.ws + roomId + "/" + userId));
            client = new JWebSocketClient(uri) {
                @Override
                public void onError(Exception ex) {
                    super.onError(ex);
                    Log.e("JWebSocketClientService", "onError：" + ex.toString());

                    mHandler.sendEmptyMessageDelayed(0x01, (connectCount + 1) * 2000);
                    /*if (NetUtil.INSTANCE.isNetworkConnected(context)) {


                    } else {
                       // TimeOutObservable.getInstance().sendTimeOut();
                        //Util.pkEnd = true;
                        // mHandler.removeCallbacks(heartBeatRunnable);
                        //就是发生了错误不需要再进行
                    }*/
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    Log.e("JWebSocketClientService", "onClose：" + reason + ",code=" + code);

                  /*  switch (code) {
                        case 1:
                            Util.pkEnd = true;
                            reconnectWs();
                            break;
                        case 2:
                            Util.pkEnd = true;
                            reconnectWs();
                            break;
                        case 3://手动断开连接

                            break;
                        case 4:
                            Util.pkEnd = true;
                            reconnectWs();
                            break;

                        case 5://网络断开连接
                            TimeOutObservable.getInstance().sendTimeOut();
                            Util.pkEnd = true;
                            break;
                    }
*/
                }

                @Override
                public void onMessage(String message) {
                    isConn = true;
                    Log.e("JWebSocketClientService", "收到的消息：messageId=" + message);
                    try {
                        JSONObject object = new JSONObject(message);
                        int type = (int) object.opt("type");
                        String messageId = (String) object.optString("messageId");
                        String pkId = (String) object.optString("pkId");
                        Log.e("JWebSocketClientService", "收到的消息：messageId=" + messageId + ",pkId=" + pkId + "------" + message);
                        if (!TextUtils.isEmpty(messageId)) {
                            responce(messageId, pkId, type);
                        }
                        if (type == PKType.SOCKET_CON.getValue()) {
                            isConn = true;
                        }
                        if (type == PKType.JOIN.getValue()) {
                            JoinUser joinUser = parse.parJoinUser(message);
                            PKUserCacheManager.INSTANCE.addValue(joinUser.getUserId(), joinUser.getNickName(), joinUser.getAvatar());
                            PKUserCacheManager.INSTANCE.addPKUsers(joinUser);
                            PkObservable.getInstance().sendJoinPk(joinUser);
                            Log.e("JWebSocketClientService", "收到的消息：" + joinUser);
                        } else if (type == PKType.LEAVE.getValue()) {
                            LeaveBean joinUser = parse.parLeave(message);
                            PKUserCacheManager.INSTANCE.removeUers(joinUser.getUserId());
                            PkObservable.getInstance().sendLeavePk(joinUser);

                        } else if (type == PKType.LOGOUT.getValue()) {
                            PkObservable.getInstance().sendPkState(type);
                        } else if (type == PKType.START.getValue()) {
                            PkObservable.getInstance().sendPkState(type);

                        } else if (type == PKType.DESTROY.getValue()) {
                            PkObservable.getInstance().sendPkState(type);

                        } else if (type == PKType.END.getValue()) {
                            PkObservable.getInstance().sendPkState(type);

                        } else if (type == PKType.RANK.getValue()) {
                            MqttMessageResponse rankList = parse.parList(message);
                            PkObservable.getInstance().sendRank(rankList);

                        } else if (type == PKType.REJOIN.getValue()) {

                        } else if (type == PKType.RECEIVE_GIFT.getValue()) {
                            SendGiftData joinUser = parse.parGift(message);
                            Log.e("JWebSocketClientService", "收到的消息：RECEIVE_GIFT" + joinUser);
                            PkObservable.getInstance().sendGiftBean(joinUser);

                        } else if (type == PKType.PK_END_CONTDOWN.getValue()) {
                            PkObservable.getInstance().sendPkState(type);
                        } else if (type == PKType.PK_END_ONE.getValue()) {
                            PkObservable.getInstance().sendPkState(type);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*Intent intent = new Intent();
                    intent.setAction("com.xch.servicecallback.content");
                    intent.putExtra("message", message);
                    sendBroadcast(intent);

                    checkLockAndShowNotification(message);*/
                }

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    super.onOpen(handshakedata);
                    sendMessage();
                    TimeOutObservable.getInstance().sendConnTime();
                    Util.pkEnd = false;
                    connectCount = 0;
                    isConn = true;
                    Log.e("JWebSocketClientService", "websocket连接成功");
                }
            }

            ;

            connect();

        } catch (
                Exception e) {
            e.printStackTrace();
        }
        // mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }


    /**
     * {"type":2001,"pkId":"1","userId":1,"distance":1,"durationMillis":1,"pkStatus":1}
     */
    public void sendRealData(String pkId, String userId, int distance, String pkStatus) {
        SendRealData sendRealData = new SendRealData();
        sendRealData.setType(PKType.REPORT_DATA.getValue());
        sendRealData.setPkId(pkId);
        sendRealData.setUserId(userId);
        sendRealData.setDistance("" + distance);
        sendRealData.setPkStatus(pkStatus);
        sendValue(parse.sendRealDataBeanToString(sendRealData));

    }


    //发送心跳包
    public void sendHeartData() {
        sendValue("{\"type\": 101}");
    }

    /**
     * {"type":2002,"pkId":"1","giftCode":"demoData","fromUserId":1,"toUserId":2}
     *
     * @return
     */
    public void sendGiftData(String pkId, String giftCode, String fromUserId, String toUserId) {
        SendGiftData sendRealData = new SendGiftData();
        sendRealData.setType(PKType.GIVE_GIFT.getValue());
        sendRealData.setPkId(pkId);
        sendRealData.setGiftCode(giftCode);
        sendRealData.setFromUserId(fromUserId);
        sendRealData.setToUserId(toUserId);
        Log.e("JWebSocketClientService", "sendGiftData=" + sendRealData);
        sendValue(parse.sendGiftbeanToStr(sendRealData));

    }

    //
    volatile boolean isConn = false;


    public void cheacConn() {
        Log.e("cheacConn", "cheacConn------" + "Util.pkEnd=" + Util.pkEnd + "connectCount=" + connectCount);

        if (Util.pkEnd) {
            return;
        }
        if (connectCount == 4) {
            TimeOutObservable.getInstance().sendTimeOut();
            Util.pkEnd = true;
        }
        if (!isConn) {
            // Util.pkEnd = true;
            innerdisConnect();
            reconnectWs();
        }
        isConn = false;

        if (client != null) {
            if (!client.isOpen()) {
                if (client.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                    try {
                        reconnectWs();
                    } catch (IllegalStateException e) {
                    }
                } else if (client.getReadyState().equals(ReadyState.CLOSING) || client.getReadyState().equals(ReadyState.CLOSED)) {
                    reconnectWs();
                }
            } else {
                sendHeartData();
            }
        } else {
            //如果client已为空，重新初始化连接
            client = null;
            connetSocket(mUserId, mRoomId, mToken);
        }
    }


    public void responce(String messageId, String pkId, int type) {
        ResponeBean sendRealData = new ResponeBean();
        sendRealData.setType(type);
        sendRealData.setPkId(pkId);
        sendRealData.setMessageId(messageId);
        sendValue(parse.sendResponse(sendRealData));

    }

    public void sendValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        addQueuryData(value);

    }

    String value;
    boolean isStart = false;

    private void sendMessage() {
        if (isStart) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!Util.pkEnd) {
                            isStart = true;
                            //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                            if (client != null && client.isOpen()) {
                                if (getQueuryLenth() > 0) {
                                    value = pollQueuryData();
                                    Log.e("JWebSocketClientService", " sendMessage sendValue=" + value + "isOpen=" + client.isOpen());
                                    if (!TextUtils.isEmpty(value)) {
                                        client.send(value);
                                    }

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    protected BlockingQueue<String> cmds = new LinkedBlockingQueue<>();


    public int getQueuryLenth() {
        return cmds.size();
    }

    public void clearQueuryData() {
        cmds.clear();
    }

    public void addQueuryData(String data) {
        cmds.offer(data);
    }

    public String pollQueuryData() {

        return cmds.poll();
    }


    /**
     * 开启重连
     */
    public void reconnectWs() {

        // mHandler.removeCallbacks(heartBeatRunnable);
        if (ToastUtil.isFastSockt(1000)) {
            return;
        }
        connectCount++;
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连" + client);
                    if (client != null) {
                        client.reconnectBlocking();
                    } else {
                        if (!TextUtils.isEmpty(mUserId)) {
                            //  connetSocket(mUserId, mRoomId, mToken);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 开启重连
     */
    private void errorreconnectWs() {
        cheacConn();
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
