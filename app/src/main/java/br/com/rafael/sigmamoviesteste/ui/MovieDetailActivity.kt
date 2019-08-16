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
import br.com.rafael.sigmamoviesteste.detalhe_filme.MovieDetailRepository
import br.com.rafael.sigmamoviesteste.detalhe_filme.MovieDetailViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalhe_filme.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_filme)

        val movieId = intent.getIntExtra("id", 1)

        val apiService = TheMovieDbClient.getClient()
        movieDetailRepository = MovieDetailRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetail.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txtErro.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    private fun bindUI(it: MovieDetail){
        txtTitulo.text = it.title
        txtSubTitulo.text = it.tagline
        txtLancamento.text = it.releaseDate
        txtNota.text = it.voteAverage.toString()
        txtResumo.text = it.overview

        val moviePosterUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(img_poster)
    }

    private fun getViewModel(movieId:Int): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailViewModel(movieDetailRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}
