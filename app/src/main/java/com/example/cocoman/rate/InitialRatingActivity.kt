package com.example.cocoman.rate

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocoman.BaseActivity
import com.example.cocoman.R
import com.example.cocoman.data.ContentRating
import com.scwang.wave.MultiWaveHeader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InitialRatingActivity : BaseActivity(), InitialRatingContract.View {
    private lateinit var adapter: ContentRatingAdaptor
    lateinit var rateCount: TextView
    lateinit var wave: MultiWaveHeader
    lateinit var userNickname: TextView
    lateinit var progressbar: ProgressBar
    lateinit var contentRecyclerView: RecyclerView

    @Inject
    lateinit var presenter: InitialRatingContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_rating_acitivity)
        initView()

        findViewById<Button>(R.id.ok_rating).setOnClickListener { presenter.onDoneClick() }
        adapter = ContentRatingAdaptor(
            this,
            object :
                onContentRatingStatusChangeListener {
                override fun onContentRated(position: Int, contentsId: String, p1: Float) {
                    presenter.onContentsRated(contentsId, p1)
                }

                override fun onContentUnrated(position: Int, contentsId: String, p1: Float) {
                    presenter.onContentsUnrated(contentsId, p1)
                }
            })
        contentRecyclerView.adapter = adapter
        contentRecyclerView.layoutManager = LinearLayoutManager(this@InitialRatingActivity)
    }

    private fun initView() {
        presenter.attach(this)
        contentRecyclerView = findViewById(R.id.content_recylerview)
        userNickname = findViewById(R.id.rating_userNickName)
        rateCount = findViewById(R.id.progress_bar_count)
        wave = findViewById(R.id.waveHeader)
        progressbar = findViewById(R.id.progress_bar_rating)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun updateContents(contentList: List<ContentRating>) {
        adapter.addContents(contentList)
    }
}