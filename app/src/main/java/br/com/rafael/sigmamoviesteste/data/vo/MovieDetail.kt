package br.com.rafael.sigmamoviesteste.data.vo

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val budget: Int,
    val genres: List<Genre>,
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Long,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)