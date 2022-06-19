


package com.example.covid_19_tracker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AbsListView
import android.widget.ListView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
@InternalCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity : AppCompatActivity() {
     private lateinit var  stateListAdapter: StateListAdapter




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = findViewById<ListView>(R.id.list1)


        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header, list, false))

        fetchResults()
        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        swipeToRefresh.setOnRefreshListener {
            fetchResults()
        }
        initWorker()


        list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (list.getChildAt(0) != null) {
                    swipeToRefresh.isEnabled = list.firstVisiblePosition == 0 && list.getChildAt(
                        0
                    ).top == 0
                }
            }
        })
    }



    @OptIn(DelicateCoroutinesApi::class)
    fun fetchResults() {
        GlobalScope.launch {
            kotlin.runCatching {
                val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
                if (response.isSuccessful) {
                    val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)

                    swipeToRefresh.isRefreshing = false
                    val data = Gson().fromJson(response.body?.string(), Response::class.java)
                    launch(Dispatchers.Main) {
                        bindCombinedData(data.statewise[0])
                        bindStateWiseData(data.statewise.subList(0, data.statewise.size))
                    }
                }
            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateListAdapter = StateListAdapter(subList)
        val list = findViewById<ListView>(R.id.list1)


        list.adapter = stateListAdapter
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun bindCombinedData(data: StatewiseItem) {
        val lastUpdatedTime = data.lastupdatedtime
        val lastUpdatedTv = findViewById<TextView>(R.id.lastUpdatedTv)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv.text = "Last Updated\n ${getTimeAgo(
            simpleDateFormat.parse(lastUpdatedTime.toString()) as Date
        )}"

        val confirmedTv = findViewById<TextView>(R.id.confirmedTv)
        confirmedTv.text = data.confirmed
        val activeTv = findViewById<TextView>(R.id.activeTv)
        activeTv.text = data.active
        val recoveredTv = findViewById<TextView>(R.id.recoveredTv)
        recoveredTv.text = data.recovered
        val deceasedTv = findViewById<TextView>(R.id.deceasedTv)
        deceasedTv.text = data.deaths

    }

    @InternalCoroutinesApi
    private fun initWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "JOB_TAG",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }


}

@SuppressLint("SimpleDateFormat")
fun getTimeAgo(past: Date): String {
    val now = Date()
    val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

    return when {
        seconds < 60 -> {
            "Few seconds ago"
        }
        minutes < 60 -> {
            "$minutes minutes ago"
        }
        hours < 24 -> {
            "$hours hour ${minutes % 60} min ago"
        }
        else -> {
            SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
        }
    }
}