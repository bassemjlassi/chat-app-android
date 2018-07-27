package com.formation.chatapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.formation.chatapplication.R;

/**
 * Created by MacBook on 26/07/2018.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {


    public TextView username;
    public TextView message;

    public MessageViewHolder(View itemView) {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.username_textview);
        message = (TextView) itemView.findViewById(R.id.message_textview);

    }
}
