package com.paintology.lite.trace.drawing.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.paintology.lite.trace.drawing.ads.koin.DIComponent
import com.example.claptofindphone.data.local.data_store.TopLevel
import com.paintology.lite.trace.drawing.Activity.gallery_activity.view_model.DrawingViewModel
import com.paintology.lite.trace.drawing.Activity.notifications.NotificationsViewModel
import com.paintology.lite.trace.drawing.Activity.shared_pref.SharedPref
import com.paintology.lite.trace.drawing.Activity.your_ranking.RankingViewModel
import com.paintology.lite.trace.drawing.Retrofit.ApiClient
import com.paintology.lite.trace.drawing.Retrofit.ApiInterface
import com.paintology.lite.trace.drawing.room.AppDatabase
import com.paintology.lite.trace.drawing.util.MyApplication

open class BaseActivity : AppCompatActivity() {

    var apiInterface: ApiInterface? = null

    val viewModel: DrawingViewModel by viewModels()
    val notificationsViewModel: NotificationsViewModel by viewModels()
    val rankingViewModel: RankingViewModel by viewModels()

    lateinit var sharedPref: SharedPref

    lateinit var appDatabase: AppDatabase

    val topLevel by lazy {
        TopLevel(this)
    }

    val diComponent = DIComponent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiInterface = ApiClient.getClientForRX().create(ApiInterface::class.java)

        sharedPref = SharedPref(this)

        appDatabase = MyApplication.getDb()

    }

}