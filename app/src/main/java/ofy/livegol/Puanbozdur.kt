package ofy.livegol


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pixelcan.inkpageindicator.InkPageIndicator
import khronos.Dates
import khronos.day
import khronos.plus
import ofy.livegol.datas.prebilgdata
import java.util.*


class Puanbozdur : androidx.fragment.app.DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_puanbozdur, container, false)
        val wpagerpre = v.findViewById<androidx.viewpager.widget.ViewPager>(R.id.Prebilgrescev)
        val indicator = v.findViewById<InkPageIndicator>(R.id.sayfabelirteci)
        val kapat = v.findViewById<ImageView>(R.id.prebilkapat)
        kapat.setOnClickListener { dismiss() }
        val satinal1 = v.findViewById<TextView>(R.id.satinal_1)
        val satinal2 = v.findViewById<TextView>(R.id.satinal_2)
        val satinal3 = v.findViewById<TextView>(R.id.satinal_3)
        satinal1.setOnClickListener { gunlukcanli() }
        satinal2.setOnClickListener { reklamkaldir() }
        satinal3.setOnClickListener { ozelmac() }
        resimleriata(wpagerpre, indicator)
        return v
    }

    private fun gunlukcanli() {
        if (puan >= 100) {
            kontroletveyaz()
        } else {
            Toast.makeText(activity, "Puanınız yetersizdir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun kontroletveyaz() {
        if (Kullanicidata.Abonelik!!){
            val kullanicizmn = Kullanicidata.abonelikzaman!!
            val degisecek = kullanicizmn + 1.day
            ref.child("abonelikzaman").setValue(degisecek)
        }else{
            ref.child("abonelik").setValue(true)
            ref.child("abonelikzaman").setValue(Zaman+1.day)

        }
        coindusur(100)
    }

    private fun reklamkaldir() {
        if (puan >= 30) {
            ref.child("reklamkalan").setValue("0")
            coindusur(30)
        } else {
            Toast.makeText(activity, "Puanınız yetersizdir", Toast.LENGTH_SHORT).show()
        }

    }

    private fun ozelmac() {
        if (puan >= 20) {
           maclarcekildikontrol(Kullanicidata.maclarim!!)
        } else {
            Toast.makeText(activity, "Puanınız yetersizdir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun maclarcekildikontrol(mackodu: String) {
        FirebaseDatabase.getInstance().getReference().child("Ozelmaclar").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val kuponkodu = p0.child("KuponKodu").getValue() as String
                            val kupondurumu = p0.child("Kupon").child("kupondurumu").getValue() as String
                            if (kuponkodu != mackodu && kupondurumu == "Başlamadı") {
                                ref.child("maclarim").setValue(kuponkodu)
                                coindusur(20)
                            } else if (kupondurumu != "Başlamadı") {
                                Toast.makeText(activity, "Şuan almaya çalıştığınız maç bitmiş veya başlamış.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(activity, "Zaten Suanki Maci Aldiniz.Daha Sonra Tekrar Deneyin.", Toast.LENGTH_SHORT).show()

                            }
                        } else {
                            Toast.makeText(activity, "Şuan Satın Alınacak Bir Maç Yok", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
        )
    }

    private fun coindusur(DusCoin: Int) {
        val degisecekcoin= (Kullanicidata.Coin!!.toInt()-DusCoin).toString()
       ref.child("coin").setValue(degisecekcoin).addOnCompleteListener {
           Toast.makeText(activity, "Tebrikler Satın Alım Başarılı", Toast.LENGTH_SHORT).show()
           dismiss()
       }
    }

    private fun resimleriata(wpagerpre: androidx.viewpager.widget.ViewPager?, indicator: InkPageIndicator?) {
        val array = ArrayList<prebilgdata>()
        array.add(prebilgdata(R.drawable.kazandiginiharca, "Şimdi biriktirdiğin puanları harca.", "Puanlarını en iyi şekilde değerlendir ve sana en uygun olanı seç"))
        val adpt = presayfaadapter(array)
        wpagerpre!!.adapter = adpt
        indicator!!.setViewPager(wpagerpre)

    }
}
