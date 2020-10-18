package ofy.livegol

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_iletisim.*
import android.content.ActivityNotFoundException



class Iletisim : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iletisim)
        iletisim_geribtn.setOnClickListener {
            onBackPressed()
        }

    }


    private fun buttonayarlari() {
        email_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, "Livetahmin@hotmail.com")
            startActivity(Intent.createChooser(intent, ""))

        }
        instagram_btn.setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/livetahmin/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/livetahmin/")))
            }

        }
    }

    private fun barlarisakla() {
        val decorvw = this.window.decorView
        decorvw.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View
                .SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View
                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}
