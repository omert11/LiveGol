package ofy.livegol

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_sen_tahminet.*
import java.text.DecimalFormat

class SenTahminet : AppCompatActivity() {
    var Oydurum = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sen_tahminet)
        Picasso.get().load(Uri2).into(Macamblem2)
        Picasso.get().load(Uri1).into(Macamblem1)
        macblokemi()
        geri_btn_tahmin.setOnClickListener {
            onBackPressed()
        }
    }

    private fun macblokemi() {
        FirebaseDatabase.getInstance().getReference().child("GununMaci").child("Bloke").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                 val bloke=p0.getValue() as Boolean
                    if (bloke){
                        Oydurum=true
                        gununtahininiveoranlaricek()
                        tahmin_yaptiniztx.setText("Maç Başlıdığından Oy Veremezsiniz")
                        tahmin_yaptiniztx.visibility = View.VISIBLE
                        oybtns_1.visibility=View.INVISIBLE
                    }else{
                        kullanicioyvemismi()
                    }
                }else{
                    macyok_constahmin.visibility=View.VISIBLE
                    oybtns_1.visibility=View.INVISIBLE
                }
            }

        })
    }

    private fun kullanicioyvemismi() {
        val durum= Kullanicidata.Oydurum!!.Durum!!
        if (durum) {
            tahminyollanmis()
            gununtahininiveoranlaricek()
        } else {
            buttonayarlari()
            gununtahininiveoranlaricek()
        }
    }

    private fun tahminyollanmis() {
        val tahmin = Kullanicidata.Oydurum!!.Tahmin!!
        oybtns_1.visibility = View.INVISIBLE
        tahmin_yaptiniztx.setText("Tahmininizi '" + tahmin + "' olarak yaptınız.")
        tahmin_yaptiniztx.visibility = View.VISIBLE
        Oydurum=true
        gununtahininiveoranlaricek()
    }

    private fun buttonayarlari() {
        Oyv_1.setOnClickListener {
            tahminet(FirebaseDatabase.getInstance().getReference("GununMaci").child("tahmin1"), "Ev Kazanır")
        }
        Oyv_2.setOnClickListener {
            tahminet(FirebaseDatabase.getInstance().getReference("GununMaci").child("tahmin2"), "Berabere")
        }
        Oyv_3.setOnClickListener {
            tahminet(FirebaseDatabase.getInstance().getReference("GununMaci").child("tahmin3"), "Deplasman Kazanır")
        }


    }

    private fun tahminet(artirref: DatabaseReference, Tahmin: String) {
        ref.child("oydurum/durum").setValue(true)
        ref.child("oydurum/tahmin").setValue(Tahmin)
        artirref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val suankitahminsayisi = p0.getValue() as String
                    val degisecektahmin = suankitahminsayisi.toFloat() + 1F
                    p0.ref.setValue(degisecektahmin.toString())
                    kullanicioyvemismi()
                    Toasty.success(this@SenTahminet, "Tahmininiz Yollandi.", Toast.LENGTH_SHORT).show()
                }
            }

        })
        when (Tahmin) {
            "Ev Kazanır" -> {
                bendevarimyaz("tahmin1")
            }
            "Berabere" -> {
                bendevarimyaz("tahmin2")
            }
            "Deplasman Kazanır" -> {
                bendevarimyaz("tahmin3")
            }
        }
    }

    private fun bendevarimyaz(uzanti: String) {
        FirebaseDatabase.getInstance().getReference().child("GununMaci").child("GununTahminleri").child(uzanti)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val yazsayi = p0.childrenCount.toString()
                            p0.child(yazsayi).ref.setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                        } else {
                            p0.child("0").ref.setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                        }
                        beklet_tahmin.visibility = View.INVISIBLE
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }

                })
    }

    private fun gununtahininiveoranlaricek() {
        FirebaseDatabase.getInstance().getReference().child("GununMaci").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val mac1 = p0.child("mac1").getValue() as String
                            val mac2 = p0.child("mac2").getValue() as String
                            val tahmin1 = p0.child("tahmin1").getValue() as String
                            val tahmin2 = p0.child("tahmin2").getValue() as String
                            val tahmin3 = p0.child("tahmin3").getValue() as String
                            val zaman = p0.child("Zaman").getValue() as String

                            thmn_ev_tx.setText(mac1 + " Kazanır")
                            thmn_dep_tx.setText(mac2 + " Kazanır")
                            sentahminet_zaman.setText(zaman)
                            oranlarihesaplaveoynat(tahmin1.toFloat(), tahmin2.toFloat(), tahmin3.toFloat())
                        } else {
                            macyok_constahmin.visibility = View.VISIBLE
                            oybtns_1.visibility=View.INVISIBLE

                        }
                    }

                }
        )
    }

    private fun oranlarihesaplaveoynat(tahmin1: Float, tahmin2: Float, tahmin3: Float) {
        if (Oydurum) {
            oranci_1.visibility = View.VISIBLE
            oranci_2.visibility = View.VISIBLE
            val toplam = tahmin1 + tahmin2 + tahmin3
            val oran1 = tahmin1 / toplam
            val oran2 = tahmin2 / toplam
            val oran3 = tahmin3 / toplam
            tahmin_1.setMaxProgress(oran1)
            tahmin_2.setMaxProgress(oran2)
            tahmin_3.setMaxProgress(oran3)
            tahmin_1.playAnimation()
            tahmin_2.playAnimation()
            tahmin_3.playAnimation()
            val format = DecimalFormat("#")
            val oran1tx = format.format(oran1 * 100)
            val oran2tx = format.format(oran2 * 100)
            val oran3tx = format.format(oran3 * 100)

            if (toplam == 0F) {
                oran_1.setText("%0")
                oran_2.setText("%0")
                oran_3.setText("%0")
            } else {
                oran_1.setText("%" + oran1tx)
                oran_2.setText("%" + oran2tx)
                oran_3.setText("%" + oran3tx)
            }
        }
    }
}
