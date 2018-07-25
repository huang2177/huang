package cn.fanrunqi.materiallogin.socket;

import android.util.Log;

import java.io.IOException;

import okhttp3.Response;
import okio.ByteString;

/**
 * Des:
 * Created by hs on 2018/7/25 0025 11:13
 */
public class WsBaseStatusListener extends WsStatusListener {
    private long perTime;
    private WsManager mWsManager;

    public WsBaseStatusListener(WsManager wsManager) {
        this.mWsManager = wsManager;
    }

    @Override
    public void onOpen(Response response) {
        super.onOpen(response);
        try {
            mWsManager.sendMessage("hello");
            Log.e("onOpen", response.body().string());
        } catch (IOException e) {
        }
    }

    @Override
    public void onMessage(String text) {
        super.onMessage(text);
        Log.e("-----huang", (System.currentTimeMillis() - perTime) + "");
        perTime = System.currentTimeMillis();

        Log.e("onMessage", text);
    }

    @Override
    public void onMessage(ByteString bytes) {
        super.onMessage(bytes);
        Log.e("onMessage", bytes.toString());
    }

    @Override
    public void onReconnect() {
        super.onReconnect();
        Log.e("onReconnect", "重连。。。。");
    }

    @Override
    public void onClosing(int code, String reason) {
        super.onClosing(code, reason);
        Log.e("onClosing", "code:" + code + "reason:" + reason);
    }

    @Override
    public void onClosed(int code, String reason) {
        super.onClosed(code, reason);
        Log.e("onClosed", "code:" + code + "reason:" + reason);
    }

    @Override
    public void onFailure(Throwable t, Response response) {
        super.onFailure(t, response);
        Log.e("onFailure", "Throwable:" + t.getMessage());
    }
}
