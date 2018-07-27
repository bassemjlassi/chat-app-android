package com.formation.chatapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.formation.chatapplication.R;
import com.formation.chatapplication.model.MessageUser;

import java.util.List;

/**
 * Created by MacBook on 26/07/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<MessageUser> mList;
    private Context context;

    public MessageAdapter(List<MessageUser> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_receive, parent, false);
       // return new MessageViewHolder(itemView);


        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new MessageViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_receive, parent, false);
            return new MessageViewHolder(itemView);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

          MessageUser messageUser = mList.get(position);


        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((MessageViewHolder) holder).message.setText(messageUser.getMessage().toString());
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((MessageViewHolder) holder).username.setText(messageUser.getUsername().toString());
                ((MessageViewHolder) holder).message.setText(messageUser.getMessage().toString());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageUser message = (MessageUser) mList.get(position);

        if (message.getUsername().equals("Bassem")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public void add(MessageUser messageUser) {

        mList.add(messageUser);
        notifyDataSetChanged();

    }

}
