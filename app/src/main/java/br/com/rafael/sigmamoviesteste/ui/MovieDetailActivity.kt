package br.com.rafael.sigmamoviesteste.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.rafael.sigmamoviesteste.R
import br.com.rafael.sigmamoviesteste.data.api.POSTER_BASE_URL
import br.com.rafael.sigmamoviesteste.data.api.TheMovieDbClient
import br.com.rafael.sigmamoviesteste.data.repository.NetworkState
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import br.com.rafael.sigmamoviesteste.movie_detail.MovieDetailRepository
import br.com.rafael.sigmamoviesteste.movie_detail.MovieDetailViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalhe_filme.*
import kotlinx.android.synthetic.main.activity_main.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_filme)

        val movieId = intent.getIntExtra("id", 1)

        val apiService = TheMovieDbClient.getClient()
        movieDetailRepository = MovieDetailRepository(apiService)

        viewModel = getViewModel(movieId, "pt-BR")

        viewModel.movieDetail.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            pgb_movie_detail_progress.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_movie_detail_error.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    private fun bindUI(it: MovieDetail){
        txt_movie_detail_title.text = it.title
        txt_movie_detail_tagline.text = it.tagline
        txt_movie_detail_release_date.text = it.releaseDate
        txt_movie_detail_vote.text = it.voteAverage.toString()
        txt_movie_detail_overview.text = it.overview

        val moviePosterUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(img_movie_detail_poster)
    }

    private fun getViewModel(movieId:Int, language:String): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailViewModel(movieDetailRepository, movieId, language) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}
