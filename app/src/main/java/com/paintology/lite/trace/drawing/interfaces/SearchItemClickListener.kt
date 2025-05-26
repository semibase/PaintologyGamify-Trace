package com.paintology.lite.trace.drawing.interfaces

import com.paintology.lite.trace.drawing.Enums.SearchResultType

interface SearchItemClickListener {
    fun selectItem(pos: Int, type: SearchResultType)
}