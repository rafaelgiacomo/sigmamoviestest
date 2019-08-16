package br.com.rafael.sigmamoviesteste.detalhe_filme

import androidx.lifecycle.LiveData
import br.com.rafael.sigmamoviesteste.data.api.TheMovieDbInterface
import br.com.rafael.sigmamoviesteste.data.repository.MovieDetailDataSource
import br.com.rafael.sigmamoviesteste.data.repository.NetworkState
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiService : TheMovieDbInterface) {

    lateinit var movieDetailDataSource: MovieDetailDataSource

    fun fetchMovieDetail(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetail> {
        movieDetailDataSource = MovieDetailDataSource(apiService, compositeDisposable)
        movieDetailDataSource.fetchMovieDetail(movieId)

        return movieDetailDataSource.downloadMovieDetail
    }

    fun fetchMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailDataSource.networkState
    }

}