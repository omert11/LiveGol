package ofy.livegol

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import khronos.day
import khronos.plus
import kotlinx.android.synthetic.main.activity_kuponlar.*
import ofy.livegol.datas.Kupondata
import ofy.livegol.recws.*
import java.util.*
import kotlin.collections.ArrayList

class Kuponlar : AppCompatActivity(), datayolla, BillingProcessor.IBillingHandler {
    var katagori = 0
    val ozelmaclar = ArrayList<Kupondata>()
    val base64key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg8yuzIb5DLgSYT2B/u28tdbe14MGUsaSQw3D+CvjKwFqXqCrXaD0gphLwqvRR058MKQyY6BmL7NSfP/dqIUHnWxS5ZmS6/RqBFue5jptDev+q/0Ol9eflKu0PE+FVaLP8V47ukV3vemWtmuoERl6fRvL/JrS94aaggOyCVsj8SbDT70KzOW7Ib8nwu4H84YMSSy+mzh8iBM3yDBIGHOrqqOjq5hmJkbtu/+x1REk55cUStMUWObkLG+tT6sS21Y5Iqp17ldqOHNkJXJDNMWslT3EIyMM7L7uYBPYJuHqtmVaoPNhiGZt1lfysfS2TS+gpe9JE6DOzhGfgyXFt56zNQIDAQAB"
    lateinit var bp: BillingProcessor
    lateinit var odulreklam: InterstitialAd

    override fun onBillingInitialized() {

    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        val satinalimdata = details!!.purchaseInfo.purchaseData
        Toasty.success(this, "Satin Alma Basarili", Toast.LENGTH_SHORT).show()
        FirebaseDatabase.getInstance().getReference().child("KullanicilarSatinAlma").child(FirebaseAuth.getInstance().currentUser!!.uid).child(details!!.purchaseInfo.purchaseData.purchaseTime.time.toString())
                .setValue(satinalimdata)
        bp.consumePurchase(productId)
        if (satinalimdata.orderId.substring(0, 3) != "GPA") {
            Toasty.error(this, "Banned :):):)", Toasty.LENGTH_LONG).show()
            ref.child("ayarlar/stil").setValue(873)
        }
        Hesaplavekaydet()
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(this, "Satin Alma Basarisiz", Toast.LENGTH_SHORT).show()
    }


    override fun datayiyolla(data: String) {
        bp.purchase(this, data)
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

    private fun reklamizlendi() {
        bekletme_reklam.visibility = View.INVISIBLE
        val itemOnClick: (Int,String) -> Unit = { position,kuponismi ->
            val intt=Intent(this@Kuponlar,yorumlar::class.java)
            intt.putExtra("pos",position)
            intt.putExtra("kuponismi",kuponismi)
            startActivity(intt)
        }
        when (katagori) {
            0 -> {
                kuponlaridinle("Canli")
                kuponolustur(CanliRec(Canlikupon, this))
                kupnsayar.visibility = View.VISIBLE
            }
            1 -> {
                kuponolustur(BasketRec(Basketkupon, this,itemOnClick))
                kuponlaridinle("Basketbol")
                kupnsayar.visibility = View.VISIBLE
            }
            2 -> {
                kuponolustur(KasaKuponRec(Kasakupon, this))
                kuponlaridinle("KasaKup")
                kupnsayar.visibility = View.VISIBLE
            }
            3 -> {
                kuponolustur(BankoRec(Bankokupon, this,itemOnClick))
                kuponlaridinle("BankoKup")
                kupnsayar.visibility = View.VISIBLE
            }
            4 -> {
                kuponolustur(SuprizRec(Suprizkupon, this,itemOnClick))
                kuponlaridinle("Supriz")
                kupnsayar.visibility = View.VISIBLE
            }
            5 -> {
                ozelmacicinbak()
                kupnsayar.visibility = View.GONE
            }
        }

    }

    private fun kuponlaridinle(Kat: String) {
        FirebaseDatabase.getInstance().getReference().child("KuponlarV").child(Kat).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val toplam = p0.childrenCount.toString()
                    var kazanan = 0
                    var kaybeden = 0
                    var devameden = 0
                    for (kuponlar in p0.children) {
                        val durum = kuponlar.child("kupondurumu").getValue() as String
                        if (durum == "Kazandi") {
                            kazanan++
                        } else if (durum == "Kaybetti") {
                            kaybeden++
                        } else {
                            devameden++
                        }
                    }
                    kplr_devm.setText(devameden.toString())
                    kplr_kybdn.setText(kaybeden.toString())
                    kplr_kznn.setText(kazanan.toString())
                    kplr_tplm.setText(toplam)
                    progress_kzn.max=toplam.toInt()
                    progress_kyb.max=toplam.toInt()
                    progress_oyn.max=toplam.toInt()
                    progress_kzn.progress=kazanan
                    progress_kyb.progress=kaybeden
                    progress_oyn.progress=devameden
                }
            }
        })
    }

    private fun bpinitilize() {
        bp = BillingProcessor(this, base64key, this)
        bp.initialize()

    }

    private fun ozelmacicinbak() {
        maclarcekildikontrol(Kullanicidata.maclarim!!)
    }

    private fun maclarcekildikontrol(mackodu: String) {
        val itemOnClick: (Int,String) -> Unit = { position,kuponismi ->
            val intt=Intent(this@Kuponlar,yorumlar::class.java)
            intt.putExtra("pos",position)
            intt.putExtra("kuponismi",kuponismi)
            startActivity(intt)
        }
        FirebaseDatabase.getInstance().getReference().child("Ozelmaclar").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val kuponkodu = p0.child("KuponKodu").getValue() as String
                            if (kuponkodu == mackodu) {
                                val data = p0.child("Kupon").getValue(Kupondata::class.java)
                                ozelmaclar.add(data!!)
                                kuponolustur(OzelRec(ozelmaclar, this@Kuponlar,itemOnClick))
                            } else {
                                macsatinalinmadi()
                            }

                        } else {
                            macyok.visibility = View.VISIBLE
                        }
                    }

                }
        )
    }

    private fun macsatinalinmadi() {
        macyok.visibility = View.VISIBLE
        macvarmitext.setText("Maç Satın Almadınız.")
        val uyari = AlertDialog.Builder(this)
        uyari.setTitle("Maç Satın Alınmadı")
        uyari.setMessage("Maçı Görebilmen İçin Puanla Alman Gerekli")
        uyari.setPositiveButton("Tamam", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                onBackPressed()
            }

        })
        uyari.setNegativeButton("Maç Almak İstiyorum", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val fragment = Puanbozdur()
                fragment.show(supportFragmentManager, "puanboz")
            }

        })
        uyari.show()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuponlar)
        if (reklamvar) {
            reklamoynat()
            reklamlistener()
        }
        bpinitilize()
        katagori = intent.extras.get("katagori") as Int
        if (katagori != 0) {
            reklamizlendi()
        } else {
            abonelikdurumukontrol()
        }


    }

    private fun abonelikdurumukontrol() {
        try{
            if (Kullanicidata.Abonelik!!) {
                val kullanicizaman = Kullanicidata.abonelikzaman!!
                zamanhesapla(kullanicizaman)

            } else {
                kuponlarigosterfree()
            }
        }catch (e:Exception){
            Toasty.info(this,"Data alınamadı uygulamayı tekrar başlatmanız gerekebilir.Sorun Devam ederse bize ulaşın").show()
            onBackPressed()
        }


    }

    private fun zamanhesapla(kullanicizaman: Date) {
        if (Zaman < kullanicizaman) {
            abonelikvar()
        } else {
            abonelikbitirveuyariver()
        }
    }

    private fun abonelikvar() {
        reklamizlendi()
    }

    private fun abonelikbitirveuyariver() {
        ref.child("abonelik").setValue(false)
        kuponlarigosterfree()
    }


    private fun kuponlarigosterfree() {
        kuponolustur(CanliFree(Canlikupon, this))
        kuponlaridinle("Canli")
        kupnsayar.visibility = View.VISIBLE
        bekletme_reklam.visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun reklamlistener() {
        odulreklam.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (reklamvar) {
                    odulreklam.show()
                }
            }

            override fun onAdClosed() {
            }

            override fun onAdFailedToLoad(p0: Int) {
                Log.e("ReklamHata", p0.toString())

            }


            override fun onAdLeftApplication() {

            }
        }
    }

    private fun reklamoynat() {
        odulreklam = InterstitialAd(this)
        odulreklam.adUnitId = "ca-app-pub-2000424330412753/3434531525"
        odulreklam.loadAd(AdRequest.Builder().build())
    }


    private fun kuponolustur(Recadapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>) {

        Kuponlar_rec.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        Kuponlar_rec.adapter = Recadapter
        Kuponlar_rec.scrollToPosition(Recadapter.itemCount - 1)
        if (Recadapter.itemCount == 0) {
            macyok.visibility = View.VISIBLE
        }
    }
}
