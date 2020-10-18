package ofy.livegol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_bizeyazin.*
import ofy.livegol.datas.mesajdata
import ofy.livegol.recws.MesajRec
import ofy.livegol.services.fcminterface
import ofy.livegol.services.fcmmodel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class bizeyazin : AppCompatActivity() {
    val baseurl = "https://fcm.googleapis.com/fcm/"
    val uid=FirebaseAuth.getInstance().currentUser!!.uid
    val mesajlarlis=ArrayList<mesajdata>()
    val tokenler=ArrayList<String>()
    var kullaniciadi=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bizeyazin)
        kullaniciadim()
        butoon()
        mesajlaricek()
    }


    private fun kullaniciadim() {
     kullaniciadi= Kullanicidata.Nick!!
    }

    private fun mesajlaricek() {
        FirebaseDatabase.getInstance().getReference().child("KullaniciMesaj").child(uid).addValueEventListener(
                object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            mesajlarlis.clear()
                            for (data in p0.children){
                                val mesaj=data.getValue(mesajdata::class.java)
                                mesajlarlis.add(mesaj!!)
                            }
                            recolustur()
                        }
                    }

                }
        )
    }

    private fun recolustur() {
        var adpt = MesajRec(mesajlarlis, this)
        KonusmaRec.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        KonusmaRec.adapter = adpt
        KonusmaRec.scrollToPosition(mesajlarlis.size - 1)
    }

    private fun butoon() {
        KonusmaMesajSend.setOnClickListener {

            mesajiyolla()

        }
    }

    private fun mesajiyolla() {
        if (KonusmaMesaj.text.isNotEmpty()){
            KonusmaMesajSend.speed=3f
            KonusmaMesajSend.playAnimation()
        val data= mesajdata(KonusmaMesaj.text.toString(), uid)
        FirebaseDatabase.getInstance().getReference().child("KullaniciMesaj").child(uid).addListenerForSingleValueEvent(
                object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val sayi=p0.childrenCount
                            p0.child(sayi.toString()).ref.setValue(data)
                        }else{
                            p0.child("0").ref.setValue(data)
                        }
                        admincek(data)
                        KonusmaMesaj.text.clear()
                    }

                } )
        }else{
            Toasty.warning(this,"Boş mesaj yollanamaz",Toast.LENGTH_SHORT).show()
        }
    }

    private fun admincek(data: mesajdata) {
       FirebaseDatabase.getInstance().getReference().child("Admins").addListenerForSingleValueEvent(
               object :ValueEventListener{
                   override fun onCancelled(p0: DatabaseError) {

                   }

                   override fun onDataChange(p0: DataSnapshot) {
                       if (p0.exists()){
                           tokenler.clear()
                           for (admin in p0.children){
                               val ad=admin.getValue() as String
                               tokenler.add(ad)
                           }
                           bildirimadmin("Bir Mesajiniz Var - Yonetici",kullaniciadi+" - "+data.mesaj!! )
                       }
                   }

               }
       )
    }

    private fun bildirimadmin(baslik: String, mesaj: String) {
        for (token in tokenler) {
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

                }
            })
        }

    }

}
