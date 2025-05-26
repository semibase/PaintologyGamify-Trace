package com.paintology.lite.trace.drawing.CameraPreview.manager.listener;

import com.paintology.lite.trace.drawing.CameraPreview.utils.Size;

import java.io.File;

/**
 * Created by Arpit Gandhi on 8/14/16.
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile);

    void onVideoRecordError();
}
