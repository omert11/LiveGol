package ofy.livegol


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Puanver : androidx.fragment.app.DialogFragment() {
    val appPackageName="ofy.livegol"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_puanver, container, false)
        val iptalbtn=v.findViewById<TextView>(R.id.iptal_btn)
        val puanverbtn=v.findViewById<TextView>(R.id.puan_btn)
        iptalbtn.setOnClickListener { dismiss() }
        puanverbtn.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
            ref.child("degerlendirmedurum").setValue(true)
        }
        return v
    }


}
