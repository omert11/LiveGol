package ofy.livegol.recws

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.tek_mesaj.view.*
import ofy.livegol.R
import ofy.livegol.datas.mesajdata

class MesajRec(val mesajlar: ArrayList<mesajdata>, val Contex: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<MesajRec.Kasaviewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Kasaviewholder {
        val intt = LayoutInflater.from(p0.context)
        val teksohbet = intt.inflate(R.layout.tek_mesaj, p0, false)
        val viewholder = Kasaviewholder(teksohbet, p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return mesajlar.size
    }

    override fun onBindViewHolder(holder: Kasaviewholder, position: Int) {
        holder.atama(position)
    }

    inner class Kasaviewholder(val Viev: View, var ViewGroup: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(Viev) {
        val tekmesaj=Viev as LinearLayout
        val gelenmesaj=tekmesaj.Gelenmesaj
        val gidenmesaj=tekmesaj.Gidenmesaj
        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        fun atama(position: Int) {
            if (mesajlar[position].uid==uid){
                gidenmesaj.visibility=View.VISIBLE
                gelenmesaj.visibility=View.INVISIBLE
                gidenmesaj.setText(mesajlar[position].mesaj)
            }else{
                gidenmesaj.visibility=View.INVISIBLE
                gelenmesaj.visibility=View.VISIBLE
                gelenmesaj.setText(mesajlar[position].mesaj)
            }
        }

    }


}