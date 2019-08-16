package br.com.rafael.sigmamoviesteste.detalhe_filme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.rafael.sigmamoviesteste.data.repository.NetworkState
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel (private val movieDetailRepository: MovieDetailRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetail : LiveData<MovieDetail> by lazy {
        movieDetailRepository.fetchMovieDetail(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieDetailRepository.fetchMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}