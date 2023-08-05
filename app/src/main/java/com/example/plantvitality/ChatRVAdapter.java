package com.example.plantvitality;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


//public class ChatRVAdapter extends RecyclerView.Adapter<ChatRVAdapter.MyViewHolder> {
//
//    private ArrayList<ChatsModal> chatsModalArrayList;
//    private Context context;
//
//    public ChatRVAdapter(ArrayList<ChatsModal> chatsModalArrayList, Context context) {
//        this.chatsModalArrayList = chatsModalArrayList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
//        MyViewHolder myViewHolder = new MyViewHolder(chatView);
//        return myViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        ChatsModal message = chatsModalArrayList.get(position);
//        if(message.getSentBy().equals(ChatsModal.SENT_BY_ME)){
//            holder.leftChatView.setVisibility(View.GONE);
//            holder.rightChatView.setVisibility(View.VISIBLE);
//            holder.rightTextView.setText(message.getMessage());
//        }else{
//            holder.rightChatView.setVisibility(View.GONE);
//            holder.leftChatView.setVisibility(View.VISIBLE);
//            holder.leftTextView.setText(message.getMessage());
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        switch (chatsModalArrayList.get(position).getSentBy()){
//            case "me":
//                return 0;
//            case "bot":
//                return 1;
//            default:
//                return -1;
//        }
//    }
//
//    @Override
//    public static int getItemCount() {
//        return chatsModalArrayList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        LinearLayout leftChatView,rightChatView;
//        TextView leftTextView,rightTextView;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            leftChatView  = itemView.findViewById(R.id.left_chat_view);
//            rightChatView = itemView.findViewById(R.id.right_chat_view);
//            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
//            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
//        }
//    }
//
//
//}
//
public class ChatRVAdapter extends RecyclerView.Adapter {

    // variable for our array list and context.
    private ArrayList<ChatsModal> chatsModalArrayList;
    private Context context;

    // constructor class.
    public ChatRVAdapter(ArrayList<ChatsModal> chatsModalArrayList, Context context) {
        this.chatsModalArrayList = chatsModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item, parent, false);
                //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item, parent, false);
                //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // this method is use to set data to our layout file.
        ChatsModal modal = chatsModalArrayList.get(position);
        switch (modal.getSender()) {
            case "user":
//                ((UserViewHolder) holder).leftChatView.setVisibility(View.GONE);
//                ((UserViewHolder) holder).rightChatView.setVisibility(View.VISIBLE);
//                ((UserViewHolder) holder).rightTextView.setText(modal.getMessage());
                ((UserViewHolder) holder).userTV.setText(modal.getMessage());
                break;
            case "bot":
                ((BotViewHolder) holder).botMsgTV.setText(modal.getMessage());
//                ((BotViewHolder) holder).rightChatView.setVisibility(View.GONE);
//                ((BotViewHolder) holder).leftChatView.setVisibility(View.VISIBLE);
//                ((BotViewHolder) holder).leftTextView.setText(modal.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // below line of code is to set position.
        switch (chatsModalArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTV;
        LinearLayout leftChatView,rightChatView;
        TextView leftTextView,rightTextView;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
//            leftChatView  = itemView.findViewById(R.id.left_chat_view);
//            rightChatView = itemView.findViewById(R.id.right_chat_view);
//            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
//            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
            userTV = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botMsgTV;

        LinearLayout leftChatView,rightChatView;
        TextView leftTextView,rightTextView;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTV = itemView.findViewById(R.id.idTVBot);
//            leftChatView  = itemView.findViewById(R.id.left_chat_view);
//            rightChatView = itemView.findViewById(R.id.right_chat_view);
//            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
//            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
        }
    }


}