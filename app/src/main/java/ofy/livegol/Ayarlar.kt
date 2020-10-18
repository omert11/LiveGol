package ofy.livegol

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.codof.matbacim.datas.kullanicidata
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
import kotlinx.android.synthetic.main.activity_ayarlar.*
import java.io.ByteArrayOutputStream
import java.io.File

class Ayarlar : AppCompatActivity(),IPickResult {
    var yuklemedurumukilit=false
    var resimcekildimi=false
    var uri:Uri=Uri.parse("android.resource://ofy.livegol/drawable/personempty")
    override fun onPickResult(p0: PickResult) {
        uri=p0.uri
        a_resim.setImageURI(p0.uri)
        resimcekildimi=true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Stil)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayarlar)
        setSupportActionBar(toolbar3)
        supportActionBar!!.title="Ayarlar"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.back))
        Listenerlar()
        DurumagoreCheckle()
        Nickdegisayarlari()
        Picasso.get().load(Kullanicidata.Reslow).into(a_resim)
        a_resim.setOnClickListener { resimcekiciayarlari() }
    }

    private fun resimcekiciayarlari() {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home->{
                onBackPressed()
            }

        }

        return super.onOptionsItemSelected(item)
    }


    private fun Nickdegisayarlari() {
        nick_ett.setText(Kullanicidata.Nick!!)
        a_kaydetbtn.setOnClickListener {
            if (nick_ett.text.isNotEmpty()){
                if (resimcekildimi){
                    yuklemedurumukilit=true
                    val options =  BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile( File (uri.getPath()).getAbsolutePath(), options)
                    val imageHeight = options . outHeight.toFloat()
                    val imageWidth = options.outWidth.toFloat()
                    val yeniyuk=((imageHeight/imageWidth)*500f).toInt()
                    Picasso.get().load(uri).resize(500,yeniyuk).into(object : Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        }

                        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                            Snackbar.make(
                                    ayarlar_lay,
                                    "Resim Yüklenemedi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                    Snackbar.LENGTH_LONG
                            ).show()
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            Toasty.info(this@Ayarlar,"Resim Kaydediliyor Bekleyiniz.").show()
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
                                                ayarlar_lay,
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
                                            Toasty.info(this@Ayarlar,"Bilgiler Kaydediliyor Bekleyiniz.").show()
                                            it.result!!.storage.downloadUrl.addOnCompleteListener {
                                                if (it.isComplete) {
                                                    val urikaydedilecek = it.result
                                                    FirebaseStorage.getInstance().reference.child("KullaniciDatasi").child(Uid_kullanan).child("resimlerim/profillow.jpg").putBytes(databytlow).addOnCompleteListener {
                                                        if (it.isComplete) {
                                                            it.result!!.storage.downloadUrl.addOnCompleteListener {
                                                                if (it.isComplete) {
                                                                    val urikaydedileceklow = it.result
                                                                    ref.child("res").setValue(urikaydedilecek)
                                                                    ref.child("reslow").setValue(urikaydedileceklow)
                                                                    Toasty.success(this@Ayarlar,"Yükleme tamamlandı").show()
                                                                    val kaytnick=nick_ett.text.toString()
                                                                    ref.child("nick").setValue(kaytnick)
                                                                    yuklemedurumukilit=false
                                                                } else {
                                                                    Snackbar.make(
                                                                            ayarlar_lay,
                                                                            "Resim Url Alinamadi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                                            Snackbar.LENGTH_LONG
                                                                    ).show()
                                                                }
                                                            }
                                                        } else {
                                                            Snackbar.make(
                                                                    ayarlar_lay,
                                                                    "Resim Url Alinamadi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                                    Snackbar.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }

                                                } else {
                                                    Snackbar.make(
                                                            ayarlar_lay,
                                                            "Resim Url Alinamadi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                            Snackbar.LENGTH_LONG
                                                    ).show()

                                                }
                                            }

                                        } else {
                                            Snackbar.make(
                                                    ayarlar_lay,
                                                    "Resim Yüklenemedi İnternet Bağlantınızı Kontrol Edin Veya Bizle İletişime Geçin",
                                                    Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                        }
                    })
                }else{
                    val kaytnick=nick_ett.text.toString()
                    ref.child("nick").setValue(kaytnick)
                    Toasty.success(this@Ayarlar,"Nick Değişti",Toast.LENGTH_SHORT).show()

                }

            }else{
                Toasty.warning(this@Ayarlar,"Bir Nick Girin",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun DurumagoreCheckle() {
       bildirimata(Kullanicidata.Ayarlar!!.Bildirim!!, Kullanicidata.Ayarlar!!.Veritasarufu!!)
    }
    private fun bildirimata(durumbildirim: Boolean,durumveritasarufu: Boolean) {
        ay_bildirim.isChecked = durumbildirim
        ay_Veritasarufu.isChecked=durumveritasarufu
    }

    override fun onBackPressed() {
        if (!yuklemedurumukilit){
        super.onBackPressed()}
    }
    private fun Listenerlar() {
        ay_bildirim.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ref.child("ayarlar/bildirim").setValue(true)
            } else {
                ref.child("ayarlar/bildirim").setValue(false)
            }
        }
        ay_Veritasarufu.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ref.child("ayarlar/veritasarufu").setValue(true)
            } else {
                ref.child("ayarlar/veritasarufu").setValue(false)
            }
        }
    }
}
