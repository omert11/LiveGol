package ofy.livegol

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_yorumlar.*
import ofy.livegol.datas.yorumdata
import ofy.livegol.recws.yorum_rec

class yorumlar : AppCompatActivity() {
    var positioN = 0
    lateinit var kuponismi: String
    val yorumlist = ArrayList<yorumdata>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yorumlar)
        positioN = intent.extras.get("pos") as Int
        kuponismi = intent.extras.get("kuponismi") as String
        kuponlaricek()
        yorum_back.setOnClickListener { onBackPressed() }
    }

    private fun kuponlaricek() {
        FirebaseDatabase.getInstance().reference.child("Yorumlar/" + kuponismi + "/" + positioN + "/Yorum").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        yorumlist.clear()
                        if (p0.exists()) {
                            for (yorumlar in p0.children) {
                                val data = yorumlar.getValue(yorumdata::class.java)
                                yorumlist.add(data!!)
                            }
                            recolustur()
                        } else {
                            onBackPressed()
                        }

                    }

                    private fun recolustur() {
                        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
                            if (Kullanicidata.adminlik!=null){
                                if (Kullanicidata.adminlik==1|| Kullanicidata.adminlik==0){
                                    val uyari = AlertDialog.Builder(this@yorumlar)
                                    uyari.setTitle("Sayın Admin")
                                    uyari.setMessage("Kullanıcının yorumunu silmek istemisin")
                                    uyari.setNegativeButton("Hayır", object : DialogInterface.OnClickListener {
                                        override fun onClick(dialog: DialogInterface?, which: Int) {

                                        }

                                    })
                                    uyari.setPositiveButton("Evet",object:DialogInterface.OnClickListener{
                                        override fun onClick(dialog: DialogInterface?, which: Int) {
                                            yorumlist.removeAt(position)
                                            FirebaseDatabase.getInstance().reference.child("Yorumlar/" + kuponismi + "/" + positioN + "/Yorum").setValue(yorumlist).addOnCompleteListener {
                                                if (it.isComplete){
                                                    kuponlaricek()
                                                }
                                            }

                                        }

                                    })
                                    uyari.show()
                                }
                            }
                        }
                        val adpt = yorum_rec(yorumlist, this@yorumlar,itemOnClick)
                        yorumlar_recw.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                                this@yorumlar,
                                RecyclerView.VERTICAL,
                                false
                        )
                        yorumlar_recw.adapter = adpt
                    }

                }
        )
    }
}
