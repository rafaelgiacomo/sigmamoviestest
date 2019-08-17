package br.com.rafael.sigmamoviesteste.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.rafael.sigmamoviesteste.data.api.TheMovieDbInterface
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailDataSource(
    private val apiService: TheMovieDbInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadDetalheFilmeResponse = MutableLiveData<MovieDetail>()
    val downloadMovieDetail: LiveData<MovieDetail>
        get() = _downloadDetalheFilmeResponse

    fun fetchMovieDetail(movieId: Int, language: String) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetail(movieId, language)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadDetalheFilmeResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailDataSource", it.message)
                        }
                    )
            )
        } catch (ex: Exception) {
            Log.e("MovieDetailDataSource", ex.message)
        }
    }

}