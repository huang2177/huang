package cn.fanrunqi.materiallogin.socket;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Des:
 * Created by hs on 2018/7/24 0024 18:34
 */
public interface IWsManager {
    WebSocket getWebSocket();

    void startConnect();

    void stopConnect();

    boolean isWsConnected();

    int getCurrentStatus();

    void setCurrentStatus(int currentStatus);

    boolean sendMessage(String msg);

    boolean sendMessage(ByteString byteString);
}
