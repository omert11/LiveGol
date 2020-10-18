package ofy.livegol

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.codof.matbacim.datas.kullanicidata
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import khronos.*
import kotlinx.android.synthetic.main.activity_anamenu.*
import kotlinx.android.synthetic.main.divider_anamenu.*
import kotlinx.android.synthetic.main.navigation_lay.*
import net.danlew.android.joda.JodaTimeAndroid
import ofy.livegol.R.id.*
import ofy.livegol.datas.Canlidata
import ofy.livegol.datas.Kupondata
import ofy.livegol.datas.canlimesajdata
import ofy.livegol.datas.mesajdata
import ofy.livegol.recws.CanliMesaj_Rec
import ofy.livegol.services.fcminterface
import ofy.livegol.services.fcmmodel
import org.apache.commons.net.ntp.NTPUDPClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

var Kasakupon = ArrayList<Kupondata>()
var Basketkupon = ArrayList<Kupondata>()
var Bankokupon = ArrayList<Kupondata>()
var Canlikupon = ArrayList<Canlidata>()
var Suprizkupon = ArrayList<Kupondata>()
var puan = 0
var adapterposition=0
var serverkey=""
class Anamenu : AppCompatActivity(), BillingProcessor.IBillingHandler, NavigationView.OnNavigationItemSelectedListener, datayolla, RewardedVideoAdListener {
    lateinit var odulreklam: RewardedVideoAd
    val base64key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg8yuzIb5DLgSYT2B/u28tdbe14MGUsaSQw3D+CvjKwFqXqCrXaD0gphLwqvRR058MKQyY6BmL7NSfP/dqIUHnWxS5ZmS6/RqBFue5jptDev+q/0Ol9eflKu0PE+FVaLP8V47ukV3vemWtmuoERl6fRvL/JrS94aaggOyCVsj8SbDT70KzOW7Ib8nwu4H84YMSSy+mzh8iBM3yDBIGHOrqqOjq5hmJkbtu/+x1REk55cUStMUWObkLG+tT6sS21Y5Iqp17ldqOHNkJXJDNMWslT3EIyMM7L7uYBPYJuHqtmVaoPNhiGZt1lfysfS2TS+gpe9JE6DOzhGfgyXFt56zNQIDAQAB"
    lateinit var bp: BillingProcessor
    var mesajacikmi=false
    val baseurl = "https://fcm.googleapis.com/fcm/"
    var bakmadanoncekimesajsayisi=0
    var mesajsayisicek=true
    var mesajlist=ArrayList<canlimesajdata>()
    var mesajtoplamsira=""
    var kontrolbildirimyollanacak=""
    var bildirimyollayacagim=false
    var resbildirimyollancak=""
    override fun onRewardedVideoAdClosed() {
    }

    override fun onRewardedVideoAdLeftApplication() {
    }

    override fun onRewardedVideoAdLoaded() {
        odulreklam.show()
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoCompleted() {
        reklamatiklandicoinekle()
        reklamdurumuata()
    }

    override fun onRewarded(p0: RewardItem?) {

    }

    override fun onRewardedVideoStarted() {
        Toasty.info(this@Anamenu, "Reklam bittiğinde 2 puan aktarılacaktır.", Toast.LENGTH_SHORT).show()

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.e("REKLAMHATA", p0.toString())
        Toasty.info(this@Anamenu, "Şuan Reklam Yok.", Toast.LENGTH_SHORT).show()


    }
    override fun datayiyolla(data: String) {
        bp.purchase(this, data)
    }

    private fun reklamdurumuata() {
        val degisecek = (Kullanicidata.Reklamkalan!!.toInt() - 1)
        if (degisecek == 0) {
            reklamvar = false
            ref.child("reklamkalan").setValue(degisecek.toString())
        } else {
            ref.child("reklamkalan").setValue(degisecek.toString())
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            nav_hizmetpolitikasi -> {
                val intt=Intent(this,kullanicisozlesmesivehizmetkosullari::class.java)
                startActivity(intt)
            }
            nav_hakkimizda -> {
                val intt = Intent(this@Anamenu, Hakkimizda::class.java)
                startActivity(intt)
            }
            nav_bizeyazin -> {
                val intt = Intent(this@Anamenu, bizeyazin::class.java)
                startActivity(intt)
            }
            nav_abonelik -> {
                val fragment = Abonelikbilg()
                fragment.show(supportFragmentManager, "abonelik")
            }
            nav_puanal -> {
                val fragment = Puansatinal()
                fragment.show(supportFragmentManager, "puansatinal")
            }
            nav_reklamkaldir -> {
                bp.purchase(this, "reklamkaldirma")
            }
            nav_cikis -> {
                FirebaseAuth.getInstance().signOut()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                val googleSignInClient = GoogleSignIn.getClient(this@Anamenu, gso)
                googleSignInClient.signOut().addOnCompleteListener {
                    val intt = Intent(this@Anamenu, Giris::class.java)
                    startActivity(intt)
                    finish()
                }
            }
            nav_iletisim -> {
                val intt = Intent(this@Anamenu, Iletisim::class.java)
                startActivity(intt)
            }
            nav_ayarlar -> {
                val intt = Intent(this@Anamenu, Ayarlar::class.java)
                startActivity(intt)
            }
            nav_puanboz -> {
                val fragment = Puanbozdur()
                fragment.show(supportFragmentManager, "Puanbozdur")
            }
        }
        drawerlay.closeDrawer(GravityCompat.START)
        return false
    }

    override fun onBillingInitialized() {

    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        val satinalimdata = details!!.purchaseInfo.purchaseData
        Toasty.success(this, "Satin Alma Basarili", Toast.LENGTH_SHORT).show()
        FirebaseDatabase.getInstance().getReference().child("KullanicilarSatinAlma").child(Uid_kullanan).child(details.purchaseInfo.purchaseData.purchaseTime.time.toString())
                .setValue(satinalimdata)
        bp.consumePurchase(productId)
        if (satinalimdata.orderId.substring(0, 3) != "GPA") {
            Toasty.error(this, "Ödeme İçin Teşekkürler Sonsuza Kadar Engellendin :)", Toasty.LENGTH_LONG).show()
            ref.child("ayarlar/stil").setValue(873)
            FirebaseAuth.getInstance().signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(this@Anamenu, gso)
            googleSignInClient.signOut().addOnCompleteListener {
                val intt = Intent(this@Anamenu, Giris::class.java)
                startActivity(intt)
                finish()
            }
        }else{
        when (productId) {
            "50puan" -> {
                puanyukle(50)
            }
            "700puan" -> {
                puanyukle(700)
            }
            "1500puan" -> {
                puanyukle(1500)
            }
            "abonelik" -> {
                Hesaplavekaydet()
            }
            "reklamkaldirma" -> {
                reklamlarikaldir()

            }
        }}

    }

    private fun Hesaplavekaydet() {
        val kaydedilecekdatef = Zaman + 7.day
        if (Kullanicidata.Abonelik!!) {
            ref.child("abonelikzaman").setValue(Kullanicidata.abonelikzaman!! + 7.day)
        } else {
            ref.child("abonelikzaman").setValue(kaydedilecekdatef)
            ref.child("abonelik").setValue(true)
        }
    }

    private fun puanyukle(Eklepuan: Int) {
        val Puank = Kullanicidata.Coin!!.toInt() + Eklepuan
        ref.child("coin").setValue(Puank).addOnCompleteListener {
            Toasty.success(this@Anamenu, "Puan Yuklendi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reklamlarikaldir() {
        ref.child("reklamkaldir").setValue(true)
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toasty.error(this, "Satin Alma Basarisiz", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Stil)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.divider_anamenu)
        datalistener()
        drawerayarlari()
        Kuponcek()
        tokenkaydet()
        buttonayarlari()
        bpinitilize()
        degerlendirmelistener()
        zamanlistener()
        abonelikvasapurchetemizle()
        mesajlistenerveayarlari()
        serverkeycek()
    }

    private fun mesajlistenerveayarlari() {
        canli_mesaj_yukarikaydirbtn.setOnClickListener {
            animationcanli(1)
        }
        canli_mesaj_mesajsend.setOnClickListener {
            if(canli_mesaj_mesajyaz.text.isEmpty()){
                Toasty.error(this,"Boş mesaj yollanamaz.")
            }else if (canli_mesaj_mesajyaz.text.toString().length>300){
                Toasty.error(this,"Mesaj 300 karakterden fazla olmamalı.")
            }else{
                val engellimi= Kullanicidata.mesajengelim!!>Zaman
                var datamesajcanligidecek=canlimesajdata(Kullanicidata.Res!!, Kullanicidata.Reslow!!, Kullanicidata.Nick!!,canli_mesaj_mesajyaz.text.toString(),engellimi)
                if (Kullanicidata.adminlik!=null){
                    if (Kullanicidata.adminlik==3){
                        datamesajcanligidecek=canlimesajdata(Kullanicidata.Res!!, Kullanicidata.Reslow!!, Kullanicidata.Nick!!,canli_mesaj_mesajyaz.text.toString(),engellimi,2)
                    }else{
                     datamesajcanligidecek=canlimesajdata(Kullanicidata.Res!!, Kullanicidata.Reslow!!, Kullanicidata.Nick!!,canli_mesaj_mesajyaz.text.toString(),engellimi,0)}
                    if (bildirimyollayacagim){
                        if (canli_mesaj_mesajyaz.text.toString().substring(0,(kontrolbildirimyollanacak.length))==kontrolbildirimyollanacak){
                                FirebaseDatabase.getInstance().reference.child("Kullanicilar").addListenerForSingleValueEvent(
                                        object :ValueEventListener{
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                for (uids in p0.children){
                                                    if (uids.child("res").exists()){
                                                        val res=uids.child("res").getValue( ) as String
                                                        if (res==resbildirimyollancak){
                                                            val token=uids.child("token").getValue() as String
                                                            bildirimyollarcevp("Admin mesajınızı yanıtladı || * "+canli_mesaj_mesajyaz.text.toString().substring(kontrolbildirimyollanacak.length,canli_mesaj_mesajyaz.text.length)+" *","Canli mesaj",token)
                                                            Log.e("Bildirim","kullanicibulundu"+token)

                                                        }
                                                    }
                                                }
                                            }

                                            private fun bildirimyollarcevp(mesaj:String,baslik:String,token: String) {
                                                val headerd = HashMap<String, String>()
                                                headerd.put("Content-Type", "application/json")
                                                headerd.put("Authorization", "key=" +serverkey )
                                                val data = fcmmodel.Data(mesaj, baslik)
                                                val bildirim = fcmmodel(data, token)
                                                val etrofit = Retrofit.Builder().baseUrl(baseurl)
                                                        .addConverterFactory(GsonConverterFactory.create()).build()
                                                val int = etrofit.create(fcminterface::class.java)
                                                val istek = int.bildirimgonder(headerd, bildirim)
                                                istek.enqueue(object : Callback<Response<fcmmodel>> {
                                                    override fun onFailure(call: Call<Response<fcmmodel>>?, t: Throwable?) {

                                                    }

                                                    override fun onResponse(call: Call<Response<fcmmodel>>?, response: Response<Response<fcmmodel>>?) {
                                                        Log.e("Bildirim","yollandi")

                                                    }
                                                })
                                            }

                                        }
                                )

                        }else{
                            Toasty.info(this,"Sayın admin mesaj yanıtlarken @ ten once birsey yazmayiniz ve nickten harf eksiltmeyiniz").show()
                        }
                    }else{
                        Log.e("Bildirim","bildirimy")
                    }

                }else if (Kullanicidata.Abonelik!!){
                            datamesajcanligidecek=canlimesajdata(Kullanicidata.Res!!, Kullanicidata.Reslow!!, Kullanicidata.Nick!!,canli_mesaj_mesajyaz.text.toString(),engellimi,1)
                        }
                FirebaseDatabase.getInstance().reference.child("canlimesaj").child(mesajtoplamsira).setValue(datamesajcanligidecek).addOnCompleteListener {
                    if (it.isComplete){
                        canli_mesaj_mesajsend.speed=3f
                        canli_mesaj_mesajsend.playAnimation()
                        canli_mesaj_mesajyaz.setText("")
                        if (engellimi){
                            Toasty.error(this,"Mesaj Engeliniz "+ Kullanicidata.mesajengelim!!.toString("MM/dd HH:mm") + " 'e kadar sürecektir anlayışınız için teşekkürler.",Toasty.LENGTH_LONG).show()
                        }
                    }else{
                        Toasty.error(this,"Mesaj Yollanamadı.")

                    }
                }
            }
        }
        FirebaseDatabase.getInstance().reference.child("canlimesaj").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mesajlist.clear()
                if(mesajsayisicek){
                    bakmadanoncekimesajsayisi=p0.childrenCount.toInt()
                    mesajsayisicek=false
                }
                mesajtoplamsira=p0.childrenCount.toString()
                val bildirimmesaj=p0.childrenCount.toInt()-bakmadanoncekimesajsayisi
                if (bildirimmesaj>0){
                    bildirim_mesajsayisi.visibility=View.VISIBLE
                    bildirim_mesajsayisi.setText(bildirimmesaj.toString())
                }else{
                    bildirim_mesajsayisi.visibility=View.GONE
                }
                for (mesajdats in p0.children){
                    mesajlist.add(mesajdats.getValue(canlimesajdata::class.java)!!)
                }
                if (mesajacikmi){
                    recolusturcanlimesaj()
                }
            }

        })
    }

    private fun recolusturcanlimesaj() {
        val durum= adapterposition==mesajlist.size
        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
            if (Kullanicidata.adminlik!=null){
                if (Kullanicidata.adminlik==1|| Kullanicidata.adminlik==0|| Kullanicidata.adminlik==3){
                    val uyari = AlertDialog.Builder(this@Anamenu)
                    uyari.setTitle("Sayın Admin")
                    uyari.setMessage("Kullanıcının mesajını silmek,yoksa 15 dk yasakmi koymak istersin?")
                    uyari.setNegativeButton("Mesaji Sil", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            FirebaseDatabase.getInstance().reference.child("canlimesaj").child(position.toString()).child("engellimi_mesaj").setValue(true)
                        }

                    })
                    uyari.setNeutralButton("Yanitla",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            canli_mesaj_mesajyaz.setText("@"+mesajlist[position].nick_mesaj)
                            bildirimyollayacagim=true
                            kontrolbildirimyollanacak=("@"+mesajlist[position].nick_mesaj)
                            resbildirimyollancak=mesajlist[position].res_mesaj!!
                        }
                    })

                    uyari.setPositiveButton("Yasakla",object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            FirebaseDatabase.getInstance().reference.child("canlimesaj").child(position.toString()).child("engellimi_mesaj").setValue(true)
                            Kullaniciya15dkyasak(mesajlist[position].reslow_mesaj)
                        }

                        private fun Kullaniciya15dkyasak(reslow_mesaj: String?) {
                            FirebaseDatabase.getInstance().reference.child("Kullanicilar").addListenerForSingleValueEvent(
                                    object :ValueEventListener{
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            for (uid in p0.children){
                                                if (uid.child("reslow").exists()){
                                                    val bakilacak=uid.child("reslow").getValue() as String
                                                    if (reslow_mesaj==bakilacak){
                                                        uid.child("mesajengelim").ref.setValue(Dates.today+15.minute)
                                                    }
                                                }
                                            }
                                        }

                                    }
                            )
                        }

                    })
                    uyari.show()
                }
            }else{
                canli_mesaj_mesajyaz.setText("@"+mesajlist[position].nick_mesaj)
            }
        }
        val adpt = CanliMesaj_Rec(mesajlist, this,itemOnClick)
        canli_mesaj_mesajrecw.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        )
        canli_mesaj_mesajrecw.adapter = adpt
        if (durum){
            canli_mesaj_mesajrecw.scrollToPosition(mesajlist.size-1)
            Log.e("durum","enaltta")
        }else{
            if (adapterposition<10){
                canli_mesaj_mesajrecw.smoothScrollToPosition(mesajlist.size-1)
                Log.e("durum","baslangic")

            }else{
                canli_mesaj_mesajrecw.scrollToPosition(adapterposition)
                Log.e("durum","mesajokunuyor")
            }
        }
    }

    private fun serverkeycek() {
        FirebaseDatabase.getInstance().getReference().child("ServerKey").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            serverkey = p0.getValue() as String
                        }
                    }

                }
        )
    }
    private fun datalistener() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    Kullanicidata = p0.getValue(kullanicidata::class.java)!!
                } catch (e: Exception) {
                    Toasty.error(this@Anamenu, "Data çekilemiyor yöneticiye ulaşın").show()
                    FirebaseAuth.getInstance().signOut()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(this@Anamenu, gso)
                    googleSignInClient.signOut().addOnCompleteListener {
                        val intt = Intent(this@Anamenu, Giris::class.java)
                        startActivity(intt)
                        finish()
                    }
                }
            }

        })
    }

    private fun abonelikvasapurchetemizle() {
        if (Kullanicidata.Abonelik!!) {
            bp.consumePurchase("abonelik")
            bp.consumePurchase("700puan")
        }
    }

    private fun zamanlistener() {
        FirebaseDatabase.getInstance().getReference().child("YeniZaman").addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Zaman = p0.getValue(Dates.today::class.java)!!
                        ref.child("songorulmem").setValue(Zaman)
                    }

                }
        )
    }


    private fun reklamoynat() {
        odulreklam = MobileAds.getRewardedVideoAdInstance(this)
        odulreklam.rewardedVideoAdListener = this
        odulreklam.loadAd("ca-app-pub-2000424330412753/5636032303", AdRequest.Builder().build())
    }


    private fun reklamatiklandicoinekle() {
        val degisecekpuan = (Kullanicidata.Coin!!.toInt() + 2).toString()
        ref.child("coin").setValue(degisecekpuan).addOnCompleteListener {
            Toasty.success(this@Anamenu, "Puan Yuklendi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawerayarlari() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
        menu_drawerbtn.setOnClickListener {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawerlay)
            drawerLayout.openDrawer(GravityCompat.START)
            profilbarayarlari()
        }
    }

    private fun infobtnayari() {
        val uyari = AlertDialog.Builder(this)
        uyari.setTitle("Puan Nedir?")
        uyari.setMessage("Puanlari tahmin yaparak veya reklama tıklayarak kazanabilirsin.Peki ne işe yarıyor? Puanlarla 1 gün,reklam kaldırabilir veya " +
                "canlı maçları görüntüleyebilirsin.Yada sadece puanla alınabilecek maclardan 1 tane satın alabilirsin.")

    }

    private fun profilbarayarlari() {
        try {

            if (Kullanicidata.Abonelik!!) {
                abonelik.setText("Abonesiniz")
                abonelik.setTextColor(resources.getColor(R.color.yesil))
                zamanhesapla(Kullanicidata.abonelikzaman!!)
            } else {
                abonelik.setText("Abone Değilsiniz")
                abonelik.setTextColor(resources.getColor(R.color.kirmizi))
            }
            nick_name.setText(Kullanicidata.Nick)
            coin_sayisi.setText(Kullanicidata.Coin)
            if (Kullanicidata.Ayarlar!!.Veritasarufu!!){
                Picasso.get().load(Uri.parse(Kullanicidata.Reslow)).into(drawer_profilres)
            }else{
            Picasso.get().load(Uri.parse(Kullanicidata.Res)).into(drawer_profilres)}
            Reklamkalan_yanmenu.setText(Kullanicidata.Reklamkalan)
            puan = Kullanicidata.Coin!!.toInt()
        } catch (e: Exception) {
            Toasty.error(this@Anamenu, e.localizedMessage + "--" + e.message + "Hata kodu").show()
        }
    }

    private fun zamanhesapla(zamankullanci: Date) {
        try {
            if (Zaman < zamankullanci) {
                abonelikkalan.setText(zamankullanci.toString("MM/dd HH:mm") + " 'e Kadar.")
                abonelikkalan.visibility = View.VISIBLE
            } else {
                abonelik.setText("Abone Değilsiniz")
                abonelikkalan.visibility = View.INVISIBLE

            }
        } catch (e: Exception) {
            Toasty.error(this@Anamenu, e.localizedMessage + "--" + e.message + "Hata kodu").show()
        }

    }


    private fun degerlendirmelistener() {
        val durum = Kullanicidata.Degerlendirmedurum!!
        if (durum) {
            val rand = Random().nextInt(99) + 1
            if (rand > 95) {
                ref.child("degerlendirmedurum").setValue(false)
            }
        } else {
            degerlendirmegoster()
        }
    }

    private fun degerlendirmegoster() {
        val rand = Random().nextInt(99) + 1
        if (rand > 80) {
            val degerlendirmefrag = Puanver()
            degerlendirmefrag.show(supportFragmentManager, "Puan")
        }
    }

    private fun bpinitilize() {
        bp = BillingProcessor(this, base64key, this)
        bp.initialize()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        if (bp != null) {
            bp.release()
        }
        super.onDestroy()
    }

    private fun buttonayarlari() {
        ans_reklamizlepuankzn.setOnClickListener {
            if (Kullanicidata.Reklamkalan != "0") {
                reklamoynat()
            } else {
                Toasty.error(this, "Günlük Sınıra Ulaştınız").show()
            }
        }
        anasayfadirekaboneol_btn.setOnClickListener {
            val fragmet = Abonelikbilg()
            fragmet.show(supportFragmentManager, "sad")
        }
        ans_canlisohbetbtn.setOnClickListener {
            animationcanli(0)
        }
        menu_canli.setOnClickListener {
            val intt = Intent(this@Anamenu, Kuponlar::class.java)
            intt.putExtra("katagori", 0)
            startActivity(intt)
        }
        menu_basketbol.setOnClickListener {
            val intt = Intent(this@Anamenu, Kuponlar::class.java)
            intt.putExtra("katagori", 1)
            startActivity(intt)
        }
        menu_kasa.setOnClickListener {
            val intt = Intent(this@Anamenu, Kuponlar::class.java)
            intt.putExtra("katagori", 2)
            startActivity(intt)
        }
        menu_banko.setOnClickListener {
            val intt = Intent(this@Anamenu, Kuponlar::class.java)
            intt.putExtra("katagori", 3)
            startActivity(intt)
        }
        menu__supriz.setOnClickListener {
            val intt = Intent(this@Anamenu, Kuponlar::class.java)
            intt.putExtra("katagori", 4)
            startActivity(intt)
        }
        menu_sendetahminet.setOnClickListener {
            val intt = Intent(this@Anamenu, SenTahminet::class.java)
            startActivity(intt)
        }
        menu_ozelmac.setOnClickListener {
            val intt = Intent(this@Anamenu, Kuponlar::class.java)
            intt.putExtra("katagori", 5)
            startActivity(intt)
        }
    }

    private fun animationcanli(anim: Int) {
        val params=canli_mesaj_mesajlay.layoutParams as ViewGroup.MarginLayoutParams
        val bottom=params.bottomMargin
        when(anim){
            0->{
                val animlogo = ValueAnimator.ofInt(2000, params.bottomMargin)
                animlogo.addUpdateListener {
                    val int = it.getAnimatedValue() as Int
                    params.bottomMargin = int
                    canli_mesaj_mesajlay.layoutParams = params
                    canlimesaj_tab.visibility=View.VISIBLE
                    if (int==bottom){
                        recolusturcanlimesaj()
                    }
                }
                animlogo.setDuration(750)
                animlogo.start()
                mesajacikmi=true

            }
            1->{
                val animlogo = ValueAnimator.ofInt(params.bottomMargin,2000)
                animlogo.addUpdateListener {
                    val int = it.getAnimatedValue() as Int
                    params.bottomMargin = int
                    canli_mesaj_mesajlay.layoutParams = params
                    if (int>1995){
                        canlimesaj_tab.visibility=View.GONE
                        params.bottomMargin=bottom
                        canli_mesaj_mesajlay.layoutParams=params

                    }
                }
                animlogo.setDuration(750)
                animlogo.start()
                mesajacikmi=false
                bildirim_mesajsayisi.visibility=View.GONE
                mesajsayisicek=true
            }
        }
    }


    private fun tokenkaydet() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("h", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        ref.child("token").setValue(token)
                    }
                    val msg =token
                    Log.d("j", msg)
                })
    }

    private fun Kuponcek() {
        FirebaseDatabase.getInstance().getReference().child("KuponlarV").addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Kasakupon.clear()
                        Basketkupon.clear()
                        Bankokupon.clear()
                        Canlikupon.clear()
                        Suprizkupon.clear()
                        val Kasa = p0.child("KasaKup")
                        val Bas = p0.child("Basketbol")
                        val bank = p0.child("BankoKup")
                        val canli = p0.child("Canli")
                        val supriz = p0.child("Supriz")
                        if (Kasa.exists()) {
                            for (Kupon in Kasa.children) {
                                val kupon = Kupon.getValue(Kupondata::class.java)
                                Kasakupon.add(kupon!!)
                            }
                        }
                        if (Bas.exists()) {
                            for (Kupon in Bas.children) {
                                val kupon = Kupon.getValue(Kupondata::class.java)
                                Basketkupon.add(kupon!!)
                            }
                        }
                        if (bank.exists()) {
                            for (Kupon in bank.children) {
                                val kupon = Kupon.getValue(Kupondata::class.java)
                                Bankokupon.add(kupon!!)
                            }
                        }
                        if (canli.exists()) {
                            for (Kupon in canli.children) {
                                val kupon = Kupon.getValue(Canlidata::class.java)
                                Canlikupon.add(kupon!!)
                            }
                        }
                        if (supriz.exists()) {
                            for (Kupon in supriz.children) {
                                val kupon = Kupon.getValue(Kupondata::class.java)
                                Suprizkupon.add(kupon!!)
                            }
                        }
                    }
                }
        )
    }


}
