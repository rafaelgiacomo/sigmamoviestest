package br.com.rafael.sigmamoviesteste.data.api

import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDbInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int): Single<MovieDetail>

}