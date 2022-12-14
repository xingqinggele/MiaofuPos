package com.example.miaofupos.socket;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homemessage.HomeMessageActivity;
import com.example.miaofupos.net.Urls;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;

import static com.example.miaofupos.net.Urls.socketUrls;

public class JWebSocketClientService extends Service {
    public JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;
    private String id;

    //灰色保活
    public class GrayInnerService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, getNotification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    PowerManager.WakeLock wakeLock;//锁屏唤醒

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    //用于Activity和service通讯
    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("mp_web", "mp_web", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1, getNotification());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent innerIntent = new Intent(this, GrayInnerService.class);
        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, getNotification());
        } else if (Build.VERSION.SDK_INT > 18 && Build.VERSION.SDK_INT < 25) {
            //Android4.3 - Android7.0，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, getNotification());
        } else {
            //Android7.0以上app启动后通知栏会出现一条"正在运行"的通知
            startForeground(GRAY_SERVICE_ID, getNotification());
        }
        acquireWakeLock();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        closeConnect();
        super.onDestroy();
    }

    public JWebSocketClientService() {
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()).setSmallIcon(R.mipmap.aa);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            NotificationChannel notificationChannel = new NotificationChannel("mp_msg", "mp_msg_name", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.setShowBadge(false);//是否显示角标
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId("mp_msg");
            builder.setContentTitle("\"秒付通\"正在运行");
        }
        Notification notification = builder.build(); // 获取构建好的Notification
        return notification;
    }


    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        if (TextUtils.isEmpty(id)) {
            Log.e("initSocketClient()", "无效id");
            return;
        }
        URI uri = URI.create(socketUrls + id);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);
                Intent intent = new Intent();
                intent.setAction("com.xch.servicecallback.content");
                intent.putExtra("message", message);
                sendBroadcast(intent);
                checkLockAndShowNotification(message);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }

        };
        connect();
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

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (null != client) {
            Log.e("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg);
        }
    }

    /**
     * 绑定连接
     *
     * @param id
     */
    public void startWebSocket(String id) {
        this.id = id;
        //初始化websocket
        if (client != null) {
            closeConnect();
        }
        initSocketClient();
        isStop = false;
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测

    }

    /**
     * 解除连接
     */
    public void stopWebSocket() {
        isStop = true;
        id = null;
        closeConnect();
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

//    -----------------------------------消息通知--------------------------------------------------------

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param content
     */
    private void checkLockAndShowNotification(String content) {
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//锁屏
            //获取电源管理器对象
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();  //点亮屏幕
                wl.release();  //任务结束后释放
            }
        }
        sendNotification(content);
    }

    /**
     * 发送通知
     *
     * @param content
     */
    private void sendNotification(String content) {
        try {
            JSONObject result = new JSONObject(content);
            String title = "";
            String msgContent = "";
            if (TextUtils.isEmpty(result.getString("msgContent"))) {
                msgContent = "";
            } else {
                msgContent = result.getString("msgContent");
            }
            if (TextUtils.isEmpty(result.getString("msgType"))) {
                title = "未知";
            } else {
                if (result.getString("msgType").equals("1")) {
                    title = "入库";
                } else if (result.getString("msgType").equals("2")) {
                    title = "机具激活";
                } else if (result.getString("msgType").equals("3")) {
                    title = "预约提现成功";
                } else if (result.getString("msgType").equals("4")) {
                    title = "提现成功";
                } else if (result.getString("msgType").equals("5")) {
                    title = "提现失败";
                } else if (result.getString("msgType").equals("6")) {
                    title = "兑换申请";
                } else if (result.getString("msgType").equals("7")) {
                    title = "兑换";
                } else if (result.getString("msgType").equals("8")) {
                    title = "返积分";
                } else if (result.getString("msgType").equals("9")){
                    title = "报件通知";
                }else {
                    title = "未知";
                }
            }
            String curAppTaskPackgeName = null;
            String curAppTaskClassName = null;
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> appTask = am.getRunningTasks(Integer.MAX_VALUE);
            if (appTask.size() > 0) {
                curAppTaskPackgeName = appTask.get(0).topActivity.getPackageName();
                curAppTaskClassName = appTask.get(0).topActivity.getClassName();
            }
            Log.d("TAG", "curAppTaskPackgeName = " + curAppTaskPackgeName + "  curAppTaskClassName = " + curAppTaskClassName);
            Intent intent = new Intent();
            intent.setClass(this, HomeMessageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setSmallIcon(R.mipmap.aa)
                    .setContentTitle(title)
                    .setContentText(msgContent)
                    .setContentIntent(pendingIntent);

            //设置Notification的ChannelID,否则不能正常显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("mp_web");
            }
            Notification notification = builder.build();
            notifyManager.notify(1, notification);//id要保证唯一
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isStop = false;

    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStop)
                return;
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                client = null;
                initSocketClient();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
