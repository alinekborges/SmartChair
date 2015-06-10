package com.alieeen.smartchair.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alieeen.smartchair.R;
import com.alieeen.smartchair.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alinekborges on 10/06/15.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private ArrayList<Message> items;
    private Activity context;

    public MessagesAdapter(Activity context, ArrayList<Message> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message item = items.get(position);

        if(!item.getReceived().isEmpty()) {
            holder.layoutReceived.setVisibility(View.VISIBLE);
            holder.layoutSent.setVisibility(View.INVISIBLE);
            holder.txtReceived.setText(item.getReceived());
        } else if (!item.getSent().isEmpty()) {
            holder.layoutSent.setVisibility(View.VISIBLE);
            holder.layoutReceived.setVisibility(View.INVISIBLE);
            holder.txtSent.setText(item.getSent());
        }

        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSent;
        public TextView txtReceived;
        public View layoutReceived;
        public View layoutSent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSent = (TextView) itemView.findViewById(R.id.txt_message_chair);
            txtReceived = (TextView) itemView.findViewById(R.id.txt_message_arduino);
            layoutReceived = itemView.findViewById(R.id.layout_message_arduino);
            layoutSent = itemView.findViewById(R.id.layout_message_chair);
        }
    }
}

