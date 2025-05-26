package com.paintology.lite.trace.drawing.CameraPreview.manager.listener;

import androidx.annotation.RestrictTo;

import com.paintology.lite.trace.drawing.CameraPreview.utils.Size;

/**
 * Created by Arpit Gandhi on 8/14/16.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface CameraOpenListener<CameraId, SurfaceListener> {
    void onCameraOpened(CameraId openedCameraId, Size previewSize, SurfaceListener surfaceListener);

    void onCameraOpenError();
}
