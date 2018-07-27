package com.formation.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.formation.chatapplication.adapter.MessageAdapter;
import com.formation.chatapplication.model.MessageUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;

    boolean isConnected = false;

    ImageView sendImageView;
    protected RecyclerView mRecyclerView;
    MessageAdapter messageAdapter;

    EditText messageEditText;

    TextView isTypingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView =findViewById(R.id.recycleview);
        messageEditText = findViewById(R.id.message_edit_text);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        messageAdapter = new MessageAdapter(new ArrayList<MessageUser>(),getApplicationContext());


        mRecyclerView.setAdapter(messageAdapter);
        mRecyclerView.setTag(messageAdapter);


        sendImageView = findViewById(R.id.send_image_view);
        isTypingTextView = findViewById(R.id.is_typing_text_view);


        mSocket = ChatApplication.getInstance().getSocket();

        //mSocket.on(Socket.EVENT_CONNECT,onConnect);


        mSocket.on("new message", onNewMessage);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);

        mSocket.connect();

        mSocket.emit("add user", "bassem");



        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(messageEditText.getText()))
                {
                    mSocket.emit("new message", messageEditText.getText().toString());
                }




                MessageUser messageUser = new MessageUser();
                messageUser.setMessage(messageEditText.getText().toString());

                messageEditText.setText("");
                messageUser.setUsername("Bassem");

                getCurrentAdapter().add(messageUser);
                scrollToBottom();
                mSocket.emit("stop typing");
            }
        });


        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSocket.emit("typing");


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(messageEditText.getText().toString()))
                    mSocket.emit("stop typing");
            }
        });

    }

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e("erreur", e.getMessage());
                        return;
                    }
                   Log.d("istyping",username);

                    isTypingTextView.setText(username + " est en train d'écrire");
                    isTypingTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e("erreur", e.getMessage());
                        return;
                    }
                    isTypingTextView.setVisibility(View.GONE);
                }
            });
        }
    };

//    private Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(!isConnected) {
//
//                            mSocket.emit("add user", "bassem");
//                        Toast.makeText(MainActivity.this.getApplicationContext(),
//                                "connected", Toast.LENGTH_LONG).show();
//                        isConnected = true;
//                    }
//                }
//            });
//        }
//    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.e("erreur", e.getMessage());
                        return;
                    }


                    MessageUser messageUser = new MessageUser();
                    messageUser.setMessage(message);
                    messageUser.setUsername(username);

                    getCurrentAdapter().add(messageUser);
                    scrollToBottom();


                }
            });
        }
    };


    private MessageAdapter getCurrentAdapter() {
        return (MessageAdapter) mRecyclerView.getTag();
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

    }
}
