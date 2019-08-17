package br.com.rafael.sigmamoviesteste.data.vo

import com.google.gson.annotations.SerializedName

data class MovieList(
    val dates: Dates,
    val page: Int,
    val results: List<MovieDetail>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)