package com.paintology.lite.trace.drawing.Activity.notifications.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paintology.lite.trace.drawing.Activity.notifications.models.CommunityPostNotification;

public abstract class BaseViewHolderCommunityNotification extends RecyclerView.ViewHolder {
    public BaseViewHolderCommunityNotification(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void onBindView(CommunityPostNotification object);
}
