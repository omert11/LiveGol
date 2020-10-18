package ofy.livegol

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.codof.matbacim.datas.Oydata
import com.codof.matbacim.datas.ayardata
import com.codof.matbacim.datas.kullanicidata
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import es.dmoral.toasty.Toasty
import khronos.Dates
import khronos.day
import khronos.minus
import khronos.toString
import kotlinx.android.synthetic.main.navigation_lay.*
import kotlinx.android.synthetic.main.siyahekran.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

var Stil = R.style.AppTheme
var reklamvar = true
lateinit var Uri1: Uri
lateinit var Uri2: Uri
lateinit var Zaman: Date
lateinit var Kullanicidata: kullanicidata
var Uid_kullanan = FirebaseAuth.getInstance().currentUser!!.uid
var ref = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(Uid_kullanan)

class Kayit : AppCompatActivity(), IPickResult {
    val surum = "3.0.0"
    val surumesk = "2.1.8"
    val appPackageName = "ofy.livegol"
    var uri_p: Uri
    override fun onPickResult(p0: PickResult?) {
        uri_p = p0!!.uri
        kayit_resim.setImageURI(uri_p)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.siyahekran)
        zamancek2()
    }

    init {
        Uid_kullanan = FirebaseAuth.getInstance().currentUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(Uid_kullanan)
        uri_p = Uri.parse("android.resource://ofy.livegol/drawable/personempty")

    }

    private fun nickvarmi() {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    try {
                        val deneme = p0.child("res").getValue() as String
                        Kullanicidata = p0.getValue(kullanicidata::class.java)!!
                        yuklemedurum.setText("Görünüm ayarları yapılıyor.")
                        guncellemekontrol()
                    } catch (e: Exception) {
                        var drs = false
                        try {
                            val Stil = p0.child("Ayarlar").child("Stil").getValue() as String
                            drs = true
                        } catch (e: Exception) {
                            drs = false
                        }
                        if (drs) {
                            val Coin = p0.child("Coin").getValue() as String
                            val Abonelik = p0.child("Abonelik").getValue() as Boolean
                            var reklamkaldir = false
                            var maclarim = ""
                            try {
                                maclarim = p0.child("Maclarim").getValue() as String
                            } catch (e: Exception) {
                                maclarim = ""
                            }
                            try {
                                reklamkaldir = p0.child("ReklamKaldir").getValue() as Boolean
                            } catch (e: Exception) {
                                reklamkaldir = false
                            }
                            val Token = p0.child("Token").getValue() as String
                            if (Abonelik) {
                                val Abonelikzmn = p0.child("AbonelikZmnYeni").getValue(Date::class.java)
                                googleilebilgicek()
                                animation(0)
                                buttonayar(Coin, Abonelik, Abonelikzmn!!, reklamkaldir, Token, maclarim)
                            } else {
                                googleilebilgicek()
                                animation(0)
                                buttonayar(Coin, Abonelik, Dates.today - 300.day, reklamkaldir, Token, maclarim)
                            }


                        } else {
                            val uyari = AlertDialog.Builder(this@Kayit)
                            uyari.setTitle("Live Tahmin")
                            uyari.setMessage("Hesabınız Sonsuza Kadar Engellenmiştir Eğer Mağdur Olduğunuzu Düşünüyorsanız Bizimle İletişim Kurun.")
                            uyari.setNegativeButton("Anladım", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {

                                }

                            })
                            uyari.show()
                        }
                    }
                } else {
                    googleilebilgicek()
                    animation(0)
                    buttonayar("0", false, Dates.today - 300.day, false, "", "")
                }
            }
        })
    }


    private fun guncellemekontrol() {
        yuklemedurum.setText("Güncellemeler konrol ediliyor.")
        FirebaseDatabase.getInstance().getReference().child("Surum").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val surumdata = p0.getValue() as String
                            if (surum == surumdata || surumesk == surumdata) {
                                tahminyaptimikontrol()
                            } else {
                                guncellemegerekli()
                            }
                        }
                    }

                }
        )
    }

    private fun guncellemegerekli() {
        guncellemegrk.visibility = View.VISIBLE
        yuklemedurum.setText("Güncelleme Yapınız")
        val uyari = AlertDialog.Builder(this)
        uyari.setTitle("Live Gol")
        uyari.setMessage("Güncelleme gerekli GooglePlaye Gitmek İstermisiniz?")
        uyari.setPositiveButton("Evet", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }

            }

        })
        uyari.setNegativeButton("Hayır", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })
        uyari.show()

    }

    private fun zamancek2() {
        FirebaseDatabase.getInstance().getReference().child("YeniZaman").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            Zaman = p0.getValue(Dates.today::class.java)!!
                            nickvarmi()
                        }

                    }

                }
        )
    }

    private fun tahminyaptimikontrol() {
        yuklemedurum.setText("Tahmin Kontrolü Yapılıyor.")
        val gun = Zaman.toString("dd")
        val guntah = Kullanicidata.Oydurum!!.Gun
        if (guntah == gun) {
            rescek()
        } else {
            ref.child("oydurum/durum").ref.setValue(false)
            ref.child("oydurum/gun").ref.setValue(gun)
            rescek()
        }

    }

    private fun rescek() {
        FirebaseStorage.getInstance().getReference().child("Mac1.png").downloadUrl.addOnCompleteListener {
            if (it.isComplete) {
                Uri1 = it.result!!
                FirebaseStorage.getInstance().getReference().child("Mac2.png").downloadUrl.addOnCompleteListener {
                    if (it.isComplete) {
                        Uri2 = it.result!!
                        abonelikdurumukontrol()
                    } else {
                        abonelikdurumukontrol()
                    }
                }
            } else {
                abonelikdurumukontrol()
            }
        }


    }

    private fun abonelikdurumukontrol() {
        yuklemedurum.setText("Abonelik Kontrolü Yapılıyor.")
        puan = Kullanicidata.Coin!!.toInt()

        val durum = Kullanicidata.Abonelik
        if (durum!!) {
            val kullanicizaman = Kullanicidata.abonelikzaman
            zamanhesapla(kullanicizaman!!)

        } else {
            reklamdurumknt()
        }

    }


    private fun zamanhesapla(kullanicizmn: Date) {
        if (kullanicizmn > Zaman) {
            reklamvar = false
            anamenuyolla()
        } else {
            abonelikbitir()
        }
    }

    private fun abonelikbitir() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(Uid_kullanan)
                .child("abonelik").setValue(false)
        reklamkaldirdurum()
    }

    private fun reklamkaldirdurum() {

        if (Kullanicidata.reklamkaldir!!) {
            reklamvar = false
            anamenuyolla()
        } else {
            reklamdurumknt()
        }
    }

    private fun reklamdurumknt() {
        if (Kullanicidata.ReklamGun!!.toInt() != Zaman.toString("dd").toInt()) {
            reklamvar = true
            ref.child("reklamGun").setValue(Zaman.toString("dd"))
            ref.child("reklamkalan").setValue("10")
            anamenuyolla()
        } else {
            if (Kullanicidata.Reklamkalan!!.toInt() == 0) {
                reklamvar = false
                anamenuyolla()
            } else {
                reklamvar = true
                anamenuyolla()
            }
        }

    }

    private fun anamenuyolla() {
        val intt = Intent(this@Kayit, Anamenu::class.java)
        startActivity(intt)
        finish()
    }

    private fun buttonayar(Coin: String, Abonelik: Boolean, abonelikzamn: Date, reklamkaldirvarmi: Boolean, tokn: String, maclarim: String) {
        kaydetbtn.setOnClickListener {
            if (nick_et.text.isNotEmpty()) {
                kaydetbtnyapilack(Coin, Abonelik, abonelikzamn, reklamkaldirvarmi, tokn, maclarim)
            } else {
                Toasty.warning(this@Kayit, "Bir Nick Giriniz", Toast.LENGTH_SHORT).show()
            }
        }
        kayit_resim.setOnClickListener {
            resimcekayarlari()
        }
    }

    private fun resimcekayarlari() {
        val Setup = PickSetup()
        Setup.setWidth(900).setHeight(600)
        Setup.backgroundColor = resources.getColor(R.color.colorAccent)
        Setup.setTitle("Profilinizde Görünecek Bir Fotoğraf Seçin")
        Setup.setTitleColor(resources.getColor(R.color.colorPrimary))
                .setProgressText("İşleniyor")
                .setProgressTextColor(resources.getColor(R.color.colorPrimary))
                .setCancelText("İptal")
                .setCancelTextColor(resources.getColor(R.color.yesil))
                .setButtonTextColor(resources.getColor(R.color.colorPrimary))
                .setCameraButtonText("Şimdi Çek")
                .setGalleryButtonText("Galeriden Seç")
                .cameraIcon = R.drawable.camera
        Setup.galleryIcon = R.drawable.gallery

        PickImageDialog.build(Setup).show(this)

    }

    private fun googleilebilgicek() {
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personPhoto = acct.photoUrl
            val newuri =
                    Uri.parse(personPhoto.toString().substring(0, personPhoto.toString().length - 15) + "s500-c/photo.jpg")
            nick_et.setText(personName)
            Log.e("EEnw", newuri.toString())
            Log.e("EEold", personPhoto.toString())
            Picasso.get().load(newuri).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    val newuri2 =
                            Uri.parse(personPhoto.toString().substring(0, personPhoto.toString().length - 5) + "s500-c")
                    Picasso.get().load(newuri2).into(object : Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                        }

                        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {

                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            uri_p = newuri2
                            kayit_resim.setImageBitmap(bitmap)
                        }
                    })
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    uri_p = newuri
                    kayit_resim.setImageBitmap(bitmap)
                }

            })
        }
    }

    fun kaydetbtnyapilack(Coin: String, Abonelik: Boolean, abonelikzamn: Date, reklamkaldirvarmi: Boolean, tokn: String, maclarim: String) {
        val nick = nick_et.text.toString()
        val dogumtarihi = Dates.of(
                kayit_yil.selectedItem.toString().toInt(),
                kayit_ay.selectedItem.toString().toInt(),
                kayit_gun.selectedItem.toString().toInt()
        )
        animation(1)
        val options =  BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile( File (uri_p.getPath()).getAbsolutePath(), options)
        val imageHeight = options . outHeight.toFloat()
        val imageWidth = options.outWidth.toFloat()
        val yeniyuk=((imageHeight/imageWidth)*500f).toInt()
        Picasso.get().load(uri_p).resize(500,yeniyuk).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                Snackbar.make(
                        Kayitve_bekleme,
                        "Resim Yüklenemedi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                        Snackbar.LENGTH_LONG
                ).show()
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                yuklemedurum.setText("Resim Kaydediliyor Bekleyiniz.")
                val baos = ByteArrayOutputStream()
                val baoslow = ByteArrayOutputStream()
                val bitmaplow = bitmap
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                bitmaplow!!.compress(Bitmap.CompressFormat.JPEG, 40, baoslow)
                var databyt = baos.toByteArray()
                val databytlow = baoslow.toByteArray()
                Log.e("Databoyut", "Buyuk:" + databyt.size.toString())
                Log.e("Databoyut", "Kucuk:" + databytlow.size.toString())
                if (databyt.size > 2000000) {
                    baos.reset()
                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                    databyt = baos.toByteArray()
                    Log.e("Databoyut", "Buyuk1:" + databyt.size.toString())
                    if (databyt.size > 2000000) {
                        baos.reset()
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                        databyt = baos.toByteArray()
                        Log.e("Databoyut", "Buyuk2:" + databyt.size.toString())
                        if (databyt.size > 2000000) {
                            Snackbar.make(
                                    yuklemedurum,
                                    "Resim dosyası cok büyük başka bir dosya seçip tekrar deneyin.",
                                    Snackbar.LENGTH_LONG
                            ).show()
                        } else {
                            resyukle(databyt, databytlow)

                        }
                    } else {
                        resyukle(databyt, databytlow)

                    }
                } else {
                    resyukle(databyt, databytlow)
                }

            }

            private fun resyukle(databyt: ByteArray, databytlow: ByteArray) {
                FirebaseStorage.getInstance().reference.child("KullaniciDatasi").child(Uid_kullanan).child("resimlerim/profil.jpg")
                        .putBytes(databyt).addOnCompleteListener {
                            if (it.isComplete) {
                                yuklemedurum.setText("Bilgiler Kaydediliyor Bekleyiniz.")
                                it.result!!.storage.downloadUrl.addOnCompleteListener {
                                    if (it.isComplete) {
                                        val urikaydedilecek = it.result
                                        FirebaseStorage.getInstance().reference.child("KullaniciDatasi").child(Uid_kullanan).child("resimlerim/profillow.jpg").putBytes(databytlow).addOnCompleteListener {
                                            if (it.isComplete) {
                                                it.result!!.storage.downloadUrl.addOnCompleteListener {
                                                    if (it.isComplete) {
                                                        val urikaydedileceklow = it.result
                                                        val data = kullanicidata(urikaydedilecek.toString(), urikaydedileceklow.toString(), nick, Coin, dogumtarihi, Abonelik, ayardata("LS", true, true), false, Oydata(Zaman.toString("dd"), false), Zaman.toString("dd"), "10", tokn, abonelikzamn, reklamkaldirvarmi, maclarim, Zaman-1.day, Zaman)
                                                        ref.setValue(data).addOnCompleteListener {
                                                            if (it.isComplete) {
                                                                zamancek2()
                                                            } else {
                                                                Snackbar.make(
                                                                        yuklemedurum,
                                                                        "Kayıt Olunamadı İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                                        Snackbar.LENGTH_LONG
                                                                ).show()
                                                            }
                                                        }
                                                    } else {
                                                        Snackbar.make(
                                                                yuklemedurum,
                                                                "Resim Url Alinamadi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                                Snackbar.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            } else {
                                                Snackbar.make(
                                                        yuklemedurum,
                                                        "Resim Url Alinamadi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                        Snackbar.LENGTH_LONG
                                                ).show()
                                            }
                                        }

                                    } else {
                                        Snackbar.make(
                                                yuklemedurum,
                                                "Resim Url Alinamadi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                Snackbar.LENGTH_LONG
                                        ).show()

                                    }
                                }

                            } else {
                                Snackbar.make(
                                        yuklemedurum,
                                        "Resim Yüklenemedi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                        Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
            }
        })

    }

    fun animation(Dr: Int) {
        val paramslogo = kayit_logo.layoutParams as ViewGroup.MarginLayoutParams
        val paramskayitsekme = kayit_sekme.layoutParams as ViewGroup.MarginLayoutParams
        when (Dr) {
            0 -> {
                val animkayitsekme = ValueAnimator.ofInt(1000, 25)
                animkayitsekme.addUpdateListener {
                    kayit_sekme.visibility = View.VISIBLE
                    val int = it.getAnimatedValue() as Int
                    paramskayitsekme.topMargin = int
                    kayit_sekme.layoutParams = paramskayitsekme
                }
                animkayitsekme.setDuration(500)
                val animlogo = ValueAnimator.ofInt(350, 0)
                animlogo.addUpdateListener {
                    val int = it.getAnimatedValue() as Int
                    paramslogo.topMargin = int
                    kayit_logo.layoutParams = paramslogo
                    if (int < 50) {
                        Yaz.visibility = View.GONE
                        yuklemedurum.visibility = View.GONE
                        animkayitsekme.start()
                    }
                }
                animlogo.setDuration(500)
                animlogo.start()
            }
            1 -> {

                val animlogo = ValueAnimator.ofInt(0, 350)
                animlogo.addUpdateListener {
                    Yaz.visibility = View.VISIBLE
                    yuklemedurum.visibility = View.VISIBLE
                    val int = it.getAnimatedValue() as Int
                    paramslogo.topMargin = int
                    kayit_logo.layoutParams = paramslogo
                }
                animlogo.setDuration(500)
                val animkayitsekme = ValueAnimator.ofInt(25, 1000)
                animkayitsekme.addUpdateListener {
                    val int = it.getAnimatedValue() as Int
                    paramskayitsekme.topMargin = int
                    kayit_sekme.layoutParams = paramskayitsekme
                    if (int > 950) {
                        kayit_sekme.visibility = View.GONE
                        animlogo.start()
                    }
                }
                animkayitsekme.setDuration(500)
                animkayitsekme.start()
            }
        }
    }

}
