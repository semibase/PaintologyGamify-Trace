package com.paintology.lite.trace.drawing.interfaces

import com.paintology.lite.trace.drawing.gallery.model_DownloadedTutorial

interface MyMoviesMenuItemClickListener {
    fun onEditClick(item: model_DownloadedTutorial?, position: Int)
    fun onDeleteClick(item: model_DownloadedTutorial?, position: Int)
    fun onShareClick(item: model_DownloadedTutorial?, position: Int)
    fun onPostClick(item: model_DownloadedTutorial?, position: Int)
}