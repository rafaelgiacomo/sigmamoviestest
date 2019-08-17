package br.com.rafael.sigmamoviesteste.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rafael.sigmamoviesteste.R
import br.com.rafael.sigmamoviesteste.data.api.TheMovieDbClient
import br.com.rafael.sigmamoviesteste.data.repository.NetworkState
import br.com.rafael.sigmamoviesteste.movie_list.MovieListViewModel
import br.com.rafael.sigmamoviesteste.movie_list.MoviePagedListAdapter
import br.com.rafael.sigmamoviesteste.movie_list.MoviePagedListRepository

class MovieListFragment : Fragment() {

    private lateinit var viewModel: MovieListViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = this.arguments
        val actualSection: Int = args!!.getInt(ARG_SECTION_NUMBER, SECTION_NOW_PLAYING)

        val apiService = TheMovieDbClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel(
            "pt-BR",
            if(actualSection == SECTION_NOW_PLAYING) "now_playing" else if (actualSection == SECTION_POPULAR) "popular" else "top_rated"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie_tabbed_list, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.rv_movie_list_fragment_movies_list)
        val progressBar: ProgressBar = root.findViewById(R.id.pgb_movie_list_fragment_progress)
        val txtError: TextView = root.findViewById(R.id.txt_movie_list_fragment_error)

        val movieAdapter = MoviePagedListAdapter(super.getContext()!!)
        val gridLayoutManager = GridLayoutManager(super.getContext()!!, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)

                return if(viewType == movieAdapter.MOVIE_VIEW_TYPE) 1 else 3
            }
        }

        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txtError.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })

        return root
    }

    private fun getViewModel(language: String, category:String): MovieListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieListViewModel(movieRepository, language, category) as T
            }
        })[MovieListViewModel::class.java]
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"
        private const val SECTION_NOW_PLAYING = 1
        private const val SECTION_POPULAR = 2
        private const val SECTION_RATED = 3

        @JvmStatic
        fun newInstance(sectionNumber: Int): MovieListFragment {
            return MovieListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}