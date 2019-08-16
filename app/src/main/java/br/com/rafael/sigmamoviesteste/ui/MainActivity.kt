package br.com.rafael.sigmamoviesteste.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.rafael.sigmamoviesteste.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener{
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("id", 299534)
            this.startActivity(intent)
        }
    }
}
