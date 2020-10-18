package ofy.livegol.recws

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tek_yorum.view.*
import ofy.livegol.Kullanicidata
import ofy.livegol.R
import ofy.livegol.datas.yorumdata

class yorum_rec(val kataloglar: ArrayList<yorumdata>, val Contex: Context,val itemClickListener: (View, Int, Int) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<yorum_rec.yorumviewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): yorumviewholder {
        val intt = LayoutInflater.from(p0.context)
        val teksohbet = intt.inflate(R.layout.tek_yorum, p0, false)
        val viewholder = yorumviewholder(teksohbet, p0)
        viewholder.onClick(itemClickListener)
        return viewholder

    }

    override fun getItemCount(): Int {
        return kataloglar.size
    }

    override fun onBindViewHolder(holder: yorumviewholder, position: Int) {
        holder.atama(position)
    }

    inner class yorumviewholder(Viev: View, var ViewGroup: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(Viev) {
        val tekyorum = Viev as LinearLayout
        val kullanicires=tekyorum.yorum_kullanicires
        val kullaniciisim=tekyorum.yorum_isim
        val kullaniciyorum=tekyorum.yorum_yorum
        fun atama(position: Int) {
            val yorum=kataloglar[position]
            if (Kullanicidata.Ayarlar!!.Veritasarufu!!){
                Picasso.get().load(Uri.parse(yorum.reslow_yorumcu)).into(kullanicires)

            }else{
                Picasso.get().load(Uri.parse(yorum.res_yorumcu)).into(kullanicires)

            }
            kullaniciyorum.setText(yorum.yorum_yorumcu)
            kullaniciisim.setText(yorum.nick_yorumcu)
            }
        }
    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnLongClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
            return@setOnLongClickListener true
        }
        return this
    }
    }

