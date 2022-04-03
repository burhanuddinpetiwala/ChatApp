package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatapp.databinding.ItemContainerSentMessageBinding;
import com.example.chatapp.models.chatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<chatMessage> chatMessages;
    private final Bitmap receivedProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<chatMessage> chatMessages, Bitmap receivedProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receivedProfileImage = receivedProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new sentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }
        else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((sentMessageViewHolder) holder).setData(chatMessages.get(position));
        }
        else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receivedProfileImage);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class sentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        sentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(chatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(chatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }
}
