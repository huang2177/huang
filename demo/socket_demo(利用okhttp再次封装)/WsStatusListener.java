package cn.fanrunqi.materiallogin.socket;

import okhttp3.Response;
import okio.ByteString;

/**
 * Des:
 * Created by hs on 2018/7/24 0024 18:37
 */
public abstract class WsStatusListener {
    public void onOpen(Response response) {
    }

    public void onMessage(String text) {
    }

    public void onMessage(ByteString bytes) {
    }

    public void onReconnect() {

    }

    public void onClosing(int code, String reason) {
    }


    public void onClosed(int code, String reason) {
    }

    public void onFailure(Throwable t, Response response) {
    }
}
