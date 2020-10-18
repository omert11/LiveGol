package ofy.livegol.recws

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tek_kuponcnl.view.*
import ofy.livegol.R
import ofy.livegol.datas.Canlidata

class CanliRec(val kuponlar: ArrayList<Canlidata>, val Contex: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<CanliRec.Kasaviewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Kasaviewholder {
        val intt = LayoutInflater.from(p0.context)
        val teksohbet = intt.inflate(R.layout.tek_kuponcnl, p0, false)
        val viewholder = Kasaviewholder(teksohbet, p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return kuponlar.size
    }

    override fun onBindViewHolder(holder: Kasaviewholder, position: Int) {
        holder.atama(position)
    }

    inner class Kasaviewholder(val Viev: View, var ViewGroup: ViewGroup) :RecyclerView.ViewHolder(Viev) {
        val teksatirkupon = Viev as LinearLayout
        val kuponismi = teksatirkupon.Kuponismi_c
        val kupondurum = teksatirkupon.KuponDurum_c
        val kupondurumres = teksatirkupon.durum_resim_c
        val macRec = teksatirkupon.MaclarRec_c
        val kupondalires=teksatirkupon.tek_kupondalires
        fun atama(position: Int) {
            val adpt = macrecw(kuponlar[position].maclar!!, Contex,true)
            macRec.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(Contex, RecyclerView.VERTICAL, false)
            macRec.adapter = adpt
            kuponismi.text = kuponlar[position].Kuponismi
            kupondurum.text = kuponlar[position].Kupondurumu
            if (kuponlar[position].Kupondali=="Futbol"){
                kupondalires.setImageDrawable(Contex.resources.getDrawable(R.drawable.football_ball))
            }else{
                kupondalires.setImageDrawable(Contex.resources.getDrawable(R.drawable.basketball2))

            }
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
                    kupondurum.setText("Canlı")
                    kupondurumres.visibility = View.VISIBLE
                    kupondurumres.setImageDrawable(Contex.resources.getDrawable(R.drawable.devam))
                }
            }
        }
    }


}