package ofy.livegol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_hakkimizda.*

class Hakkimizda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hakkimizda)
        hakimizda_geribtn.setOnClickListener {
            onBackPressed()
        }}



}
