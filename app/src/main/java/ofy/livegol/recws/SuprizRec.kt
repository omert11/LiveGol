package ofy.livegol.recws

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.tek_kupon.view.*
import ofy.livegol.Kullanicidata
import ofy.livegol.R
import ofy.livegol.Uid_kullanan
import ofy.livegol.datas.Kupondata
import ofy.livegol.datas.yorumdata

class SuprizRec(val kuponlar: ArrayList<Kupondata>, val Contex: Context, val itemClickListener: (Int,String) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<SuprizRec.Kasaviewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Kasaviewholder {
        val intt = LayoutInflater.from(p0.context)
        val teksohbet = intt.inflate(R.layout.tek_kupon, p0, false)
        val viewholder = Kasaviewholder(teksohbet, p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return kuponlar.size
    }

    override fun onBindViewHolder(holder: Kasaviewholder, position: Int) {
        holder.atama(position)
    }

    inner class Kasaviewholder(val Viev: View, var ViewGroup: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(Viev) {
        val teksatirkupon = Viev as LinearLayout
        val kuponismi = teksatirkupon.Kuponismi
        val kupondurum = teksatirkupon.KuponDurum
        val kupondurumres = teksatirkupon.durum_resim
        val macRec = teksatirkupon.MaclarRec
        val begenbuton = teksatirkupon.kupon_begenbtn
        val yorumyapbtn = teksatirkupon.kupon_yorumyapbtn
        val toplambegenme = teksatirkupon.kupon_begenmesayisi
        val yorumcu1 = teksatirkupon.kupon_yorumcu_1
        val yorumcu1yorum = teksatirkupon.kupon_yorumcu_1_yorum
        val yorumcu2 = teksatirkupon.kupon_yorumcu_2
        val yorumcu2yorum = teksatirkupon.kupon_yorumcu_2_yorum
        val toplamyorum = teksatirkupon.kupon_yorum_tamami
        val yorumtamminigor = teksatirkupon.kupon_tamaminigorbtn
        val yorumet=teksatirkupon.kupon_yorumet
        val yorumsendbtn=teksatirkupon.kupon_Send
        val kuponyorumlay=teksatirkupon.kupon_yorum_lay
        val yorumlist = ArrayList<yorumdata>()
        var begendimmi = false
        var yorumsayisi = "0"
        var POs=0
        fun atama(position: Int) {
            val adpt = macrecw(kuponlar[position].maclar!!, Contex,false)
            macRec.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(Contex, RecyclerView.VERTICAL, false)
            macRec.adapter = adpt
            kuponismi.text = kuponlar[position].Kuponismi
            kupondurum.text = kuponlar[position].Kupondurumu
            when (kuponlar[position].Kupondurumu) {
                "Maç Devam Ediyor" -> {
                    kupondurumres.visibility = View.VISIBLE
                    kupondurumres.setImageDrawable(Contex.resources.getDrawable(R.drawable.devam))
                }
                "Kazandi" -> {
                    kupondurumres.visibility = View.VISIBLE
                    kupondurumres.setImageDrawable(Contex.resources.getDrawable(R.drawable.kazandi))
                }
                "Kaybetti" -> {
                    kupondurumres.visibility = View.VISIBLE
                    kupondurumres.setImageDrawable(Contex.resources.getDrawable(R.drawable.kaybetti))
                }
                else -> {
                    kupondurumres.visibility = View.VISIBLE
                    kupondurumres.setImageDrawable(Contex.resources.getDrawable(R.drawable.devam))
                }
            }
            FirebaseDatabase.getInstance().reference.child("Yorumlar/Supriz/" + position.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.child("Yorum").ref.addValueEventListener(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            yorumlist.clear()
                            if (p0.exists()){
                                yorumtamminigor.visibility = View.VISIBLE
                                toplamyorum.visibility = View.VISIBLE
                                yorumcu1.visibility = View.VISIBLE
                                yorumcu1yorum.visibility = View.VISIBLE
                                for (yorumlar in p0.children) {
                                    val data = yorumlar.getValue(yorumdata::class.java)
                                    yorumlist.add(data!!)
                                }
                                toplamyorum.text = yorumlist.size.toString() + " yorum var "
                                yorumcu1.text = yorumlist[0].nick_yorumcu
                                yorumcu1yorum.text = yorumlist[0].yorum_yorumcu
                                if (yorumlist.size > 1) {
                                    yorumcu2yorum.visibility = View.VISIBLE
                                    yorumcu2.visibility = View.VISIBLE
                                    yorumcu2.text = yorumlist[1].nick_yorumcu
                                    yorumcu2yorum.text = yorumlist[1].yorum_yorumcu
                                }else{
                                    yorumcu2yorum.visibility = View.GONE
                                    yorumcu2.visibility = View.GONE
                                }
                                yorumsayisi=yorumlist.size.toString()
                            }else{
                                yorumtamminigor.visibility = View.GONE
                                toplamyorum.visibility = View.GONE
                                yorumcu2yorum.visibility = View.GONE
                                yorumcu2.visibility = View.GONE
                                yorumcu1.visibility = View.GONE
                                yorumcu1yorum.visibility = View.GONE
                                yorumsayisi="0"
                            }
                        }

                    })
                    p0.child("Begenme").ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                toplambegenme.visibility = View.VISIBLE
                                toplambegenme.text = p0.childrenCount.toString() + " beğenme"
                                if (p0.child(Uid_kullanan).exists()) {
                                    begenbuton.setImageDrawable(Contex.resources.getDrawable(R.drawable.begenme_kupondolu))
                                    begendimmi = true
                                } else {
                                    begenbuton.setImageDrawable(Contex.resources.getDrawable(R.drawable.begenme_kupon))
                                    begendimmi = false
                                }
                            } else {
                                begenbuton.setImageDrawable(Contex.resources.getDrawable(R.drawable.begenme_kupon))
                                begendimmi = false
                                toplambegenme.visibility = View.GONE
                            }
                        }

                    })


                }

            })
            begenbuton.setOnClickListener { begenbutonyapilacak(position) }
            yorumyapbtn.setOnClickListener { yorumyapbtnyapilacak(position) }
            yorumtamminigor.onClick(itemClickListener)
        }
        private fun yorumyapbtnyapilacak(position: Int) {
            anim(0)
            yorumsendbtn.setOnClickListener {
                yorumyolla(position)
            }
        }
        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        private fun yorumyolla(position:Int) {
            var yorumsayim=0
            for (yor in yorumlist){
                if (yor.res_yorumcu== Kullanicidata.Res!!){
                    yorumsayim=yorumsayim+1
                }
            }
            if (yorumet.text.isEmpty()){
                Toasty.warning(Contex,"Boş yorum yollanamaz").show()
            }else if (yorumsayim>=3){
                Toasty.warning(Contex,"3 ten fazla yorum yollanamaz").show()

            }else{
                yorumet.hideKeyboard()
                FirebaseDatabase.getInstance().reference.child("Yorumlar/Supriz/" + position.toString()+"/Yorum/"+yorumsayisi).setValue(yorumdata(Kullanicidata.Res!!, Kullanicidata.Reslow!!, Kullanicidata.Nick!!,yorumet.text.toString())).addOnCompleteListener {
                    if (it.isComplete){
                        yorumsendbtn.speed=6f
                        yorumet.setText("")
                        yorumsendbtn.playAnimation()
                        yorumsendbtn.addAnimatorListener(object : Animator.AnimatorListener{
                            override fun onAnimationRepeat(animation: Animator?) {

                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                anim(1)
                            }

                            override fun onAnimationCancel(animation: Animator?) {
                            }

                            override fun onAnimationStart(animation: Animator?) {
                            }

                        })
                    }else{
                        Toasty.warning(Contex,"Yorum yollanamadi").show()

                    }

                }
            }
        }

        private fun anim(i: Int) {
            val params=kuponyorumlay.layoutParams
            when(i){
                0->{
                    val animlogo = ValueAnimator.ofInt(0, params.height)
                    animlogo.addUpdateListener {
                        kuponyorumlay.visibility=View.VISIBLE
                        val int = it.getAnimatedValue() as Int
                        params.height = int
                        kuponyorumlay.layoutParams = params
                    }
                    animlogo.setDuration(250)
                    animlogo.start()}
                1->{
                    val animlogo = ValueAnimator.ofInt(params.height,0 )
                    animlogo.addUpdateListener {
                        val int = it.getAnimatedValue() as Int
                        params.height = int
                        kuponyorumlay.layoutParams = params
                        if (int<2){
                            kuponyorumlay.visibility=View.GONE
                        }
                    }
                    animlogo.setDuration(250)
                    animlogo.start()
                }
            }
        }

        private fun begenbutonyapilacak(position: Int) {
            if (!begendimmi) {
                FirebaseDatabase.getInstance().reference.child("Yorumlar/Supriz/" + position.toString()).child("Begenme").child(Uid_kullanan).ref.setValue(Uid_kullanan)
            } else {
                FirebaseDatabase.getInstance().reference.child("Yorumlar/Supriz/" + position.toString()).child("Begenme").child(Uid_kullanan).ref.removeValue()
            }
        }
        fun <T : TextView> T.onClick(event:(Pos: (Int), Kuponismi:String) -> Unit): T {
            yorumtamminigor.setOnClickListener {
                event.invoke(POs,"Supriz")
            }
            return this
        }
    }


}