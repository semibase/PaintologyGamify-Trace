package com.paintology.lite.trace.drawing.Youtube.ui.menu;

import android.view.View;

public interface YouTubePlayerMenu {
    void show(View anchorView);

    void dismiss();

    void addItem(MenuItem menuItem);

    void removeItem(int itemIndex);

    int getItemCount();
}
