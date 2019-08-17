package br.com.rafael.sigmamoviesteste.data.api

import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import br.com.rafael.sigmamoviesteste.data.vo.MovieList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") id: Int,
        @Query("language") language: String): Single<MovieDetail>

    @GET("movie/{category}")
    fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("language") language: String): Single<MovieList>

}