package com.formation.chatapplication;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by MacBook on 26/07/2018.
 */

public class ChatApplication extends Application {

    private static ChatApplication mInstance = null;
    public static ChatApplication getInstance() {
        return mInstance;
    }

    private Socket mSocket;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public Socket getSocket() {

        if(mSocket==null) {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return mSocket;
    }
}
