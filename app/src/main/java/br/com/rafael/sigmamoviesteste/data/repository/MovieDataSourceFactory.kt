package br.com.rafael.sigmamoviesteste.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import br.com.rafael.sigmamoviesteste.data.api.TheMovieDbInterface
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(private val apiService: TheMovieDbInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, MovieDetail>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MovieDetail> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}