package com.paintology.lite.trace.drawing.search

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.paintology.lite.trace.drawing.Enums.SearchResultType
import com.paintology.lite.trace.drawing.Model.Userpost
import com.paintology.lite.trace.drawing.R
import com.paintology.lite.trace.drawing.interfaces.SearchItemClickListener

class UserPostAdapter internal constructor(
    val context: Context,
    private val tutorialList: List<Userpost>,
    private val viewPager2: ViewPager2,
    private val objInterface: SearchItemClickListener,
    private val type: SearchResultType
) : RecyclerView.Adapter<UserPostAdapter.SliderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_item, parent, false
            ),
            objInterface,
            type
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, i: Int) {
        val tutorial = tutorialList[i]
        if (tutorial != null) {

            if (!TextUtils.isEmpty(tutorial.postTitle)) {
                holder.tvCategory.text = tutorial.postTitle
            }

            if (!TextUtils.isEmpty(tutorial.postContentHTML)) {
                holder.tvTutorialDesc.text = HtmlCompat.fromHtml(
                    tutorial.postContentHTML!!,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            if (tutorial.thumbUrl != null && !tutorial.thumbUrl.equals(
                    "false",
                    ignoreCase = true
                )
            ) {
                val imageLink = tutorial.thumbUrl
                Log.d("imagesLinks", "onBindViewHolder: $imageLink")
                val options: RequestOptions =
                    RequestOptions() //                        .centerCrop()
                        .fitCenter()
                        .placeholder(R.drawable.thumbnaildefault)
                        .error(R.drawable.thumbnaildefault)
                Glide.with(context).load(imageLink).apply(options).into(holder.thumbnail)
            }
        }
    }

//    private val runnable = Runnable {
//        tutorialList.addAll(tutorialList)
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int {
        return tutorialList.size
    }

    inner class SliderViewHolder(
        itemView: View,
        objInterface: SearchItemClickListener,
        type: SearchResultType
    ) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.iv_tutorial_category)
        val ytImage: ImageView = itemView.findViewById(R.id.iv_yt)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        val tvTutorialDesc: TextView = itemView.findViewById(R.id.tv_category_desc)

        init {
            itemView.setOnClickListener {
                objInterface.selectItem(adapterPosition, type)
            }
        }
    }
}
