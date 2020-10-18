package ofy.livegol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_giris.*

class Giris : AppCompatActivity() {
    lateinit var Kullanicikontrol: FirebaseAuth.AuthStateListener
    val auth = FirebaseAuth.getInstance()
    lateinit var googlegirisclent: GoogleSignInClient
    val RC_SIGN_IN = 2


    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(Kullanicikontrol)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)
        buttonayari()
        Kullanicisoz_grsbtn.setOnClickListener {
            val intt=Intent(this,kullanicisozlesmesivehizmetkosullari::class.java)
            startActivity(intt)
        }
        auth.setLanguageCode("tr")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        Kullanicikontrol = FirebaseAuth.AuthStateListener {
            if (it.currentUser != null) {
                val intet = Intent(this, Kayit::class.java)
                startActivity(intet)
                finish()
            }
        }
        googlegirisclent = GoogleSignIn.getClient(this, gso)
        googlegirs.setOnClickListener {
            val girisintent = googlegirisclent.signInIntent
            startActivityForResult(girisintent, RC_SIGN_IN)
        }
    }


    private fun buttonayari() {
        Girisbtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val girisintent = googlegirisclent.signInIntent
                    startActivityForResult(girisintent, RC_SIGN_IN)

                }
                MotionEvent.ACTION_DOWN -> {

                }
                MotionEvent.ACTION_CANCEL -> {

                }
            }
            return@setOnTouchListener true
        }
        YardimBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val intt=Intent(this@Giris,Yardim::class.java)
                    startActivity(intt)

                }
                MotionEvent.ACTION_DOWN -> {

                }
                MotionEvent.ACTION_CANCEL -> {

                }
            }
            return@setOnTouchListener true
        }
        IletisimBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val intt=Intent(this@Giris,Iletisim::class.java)
                    startActivity(intt)

                }
                MotionEvent.ACTION_DOWN -> {

                }
                MotionEvent.ACTION_CANCEL -> {

                }
            }
            return@setOnTouchListener true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                val result = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val hesap = result.getResult(ApiException::class.java)
                    fireGirisGoogleile(hesap!!)
                    Toasty.success(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show()

                } catch (e: ApiException) {
                    Toasty.error(this, e.message!!, Toast.LENGTH_SHORT).show()
                    Toasty.error(this, "Giriş Başarısız", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun fireGirisGoogleile(hesap: GoogleSignInAccount) {
        Log.e("giris", hesap.id)
        val credetilal = GoogleAuthProvider.getCredential(hesap.idToken, null)
        auth.signInWithCredential(credetilal)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                    }
                }
    }


}

