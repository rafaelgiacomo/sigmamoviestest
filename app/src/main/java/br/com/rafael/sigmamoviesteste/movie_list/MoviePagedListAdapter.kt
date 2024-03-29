package br.com.rafael.sigmamoviesteste.movie_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.rafael.sigmamoviesteste.R
import br.com.rafael.sigmamoviesteste.data.api.POSTER_BASE_URL
import br.com.rafael.sigmamoviesteste.data.repository.NetworkState
import br.com.rafael.sigmamoviesteste.data.vo.MovieDetail
import br.com.rafael.sigmamoviesteste.ui.MovieDetailActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalhe_filme.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MoviePagedListAdapter(public val context: Context) : PagedListAdapter<MovieDetail, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        }else if (hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieDetail>() {
        override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: MovieDetail?, context: Context) {
            itemView.txt_movie_list_item_title.text = movie?.title
            itemView.txt_movie_list_item_release_date.text = movie?.releaseDate

            val moviePosterUrl = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(itemView.img_movie_list_item_poster)

            itemView.setOnClickListener{
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }

    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING){
                itemView.pgb_movie_detail_progress.visibility = View.VISIBLE
            }else {
                itemView.pgb_movie_detail_progress.visibility = View.GONE
            }

        }

    }

}