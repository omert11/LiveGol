package ofy.livegol.recws

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tek_kupon.view.*
import ofy.livegol.R
import ofy.livegol.datas.Kupondata

class KasaKuponRec(val kuponlar: ArrayList<Kupondata>, val Contex: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<KasaKuponRec.Kasaviewholder>() {
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
        fun atama(position: Int) {
            val adpt = macrecw(kuponlar[position].maclar!!, Contex,false)
            macRec.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(Contex, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
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
        }
    }


}