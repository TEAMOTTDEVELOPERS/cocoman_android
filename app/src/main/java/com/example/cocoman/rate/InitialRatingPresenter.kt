package com.example.cocoman.rate

import android.util.Log
import com.example.cocoman.data.ContentRating
import com.example.cocoman.data.source.ContentsApi
import com.example.cocoman.data.source.UserApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class InitialRatingPresenter @Inject constructor(
    private val contentsApi: ContentsApi,
    private val userApi: UserApi
) :
    InitialRatingContract.Presenter {
    lateinit var view: InitialRatingContract.View

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun onDoneClick() {

    }

    override fun onContentsRated(contentsId: String, p1: Float) {
        TODO("Not yet implemented")
    }

    override fun onContentsUnrated(contentsId: String, p1: Float) {
        TODO("Not yet implemented")
    }

    override fun attach(view: InitialRatingContract.View) {
        this.view = view
        loadContentsData()
    }

    private fun loadContentsData() {
        // todo :: 로딩꺼야해...
        contentsApi.getRatingContents()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.startLoading() }
            .flatMapIterable { listData -> listData }
            .map { content ->
                ContentRating(
                    content.id,
                    content.title,
                    content.year,
                    content.genreList.toString(),
                    content.genreList.toString(),
                    0F
                )
            }
            .toList()
            .subscribe({ contentList ->
                view.updateContents(contentList)
            }, { fail ->
                Log.e("coconut", "왜 실패지 ㅠㅠ " + fail.message)
            }).apply { compositeDisposable.add(this) }
    }

    override fun detach() {
        compositeDisposable.clear()
    }


//    fun incrementRated(position: Int, score: Float){
//        rateCompleted+=1
//        shouldBeRated = 10-rateCompleted
//        contentList[position].score = score
//        Log.d("rate scor (INCREMENT)",""+contentList[position].score)
//        if(rateCompleted>=10){
//            rateCount.setText("10개 달성!")
//            progressbar.progress=10
//        }else{
//            rateCount.setText(""+shouldBeRated+"개 남음")
//            progressbar.progress=rateCompleted
//        }
//    }
//
//    fun decrementRated(position: Int){
//        rateCompleted-=1
//        shouldBeRated = 10-rateCompleted
//        contentList[position].score = 0.0F
//        if(rateCompleted>=10){
//            rateCount.setText("10개 달성!")
//            progressbar.progress=10
//        }else{
//            rateCount.setText(""+shouldBeRated+"개 남음")
//            progressbar.progress=rateCompleted
//        }
//    }
}