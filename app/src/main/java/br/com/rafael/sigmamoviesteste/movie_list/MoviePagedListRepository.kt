package br.com.rafael.sigmamoviesteste.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.com.rafael.sigmamoviesteste.data.api.POST_PER_PAGE
import br.com.rafael.sigmamoviesteste.data.api.TheMovieDbInterface
import br.com.rafael.sigmamoviesteste.data.repository.MovieDataSource
import br.com.rafael.sigmamoviesteste.data.repository.MovieDataSourceFactory
import br.com.rafael.sigmamoviesteste.data.repository.NetworkState
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService: TheMovieDbInterface){

    lateinit var moviePagedList: LiveData<PagedList<MovieDetail>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<MovieDetail>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }

}