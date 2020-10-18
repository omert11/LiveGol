package ofy.livegol.recws

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tek_canlimesaj.view.*
import ofy.livegol.Kullanicidata
import ofy.livegol.R
import ofy.livegol.adapterposition
import ofy.livegol.datas.canlimesajdata

class CanliMesaj_Rec (val mesajlar: ArrayList<canlimesajdata>, val Contex: Context,val itemClickListener: (View, Int, Int) -> Unit):RecyclerView.Adapter<CanliMesaj_Rec.CanliMesaj_Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanliMesaj_Viewholder {
        val intt = LayoutInflater.from(parent.context)
        val teksohbet = intt.inflate(R.layout.tek_canlimesaj, parent, false)
        val viewholder = CanliMesaj_Viewholder(teksohbet, parent)
        viewholder.onClick(itemClickListener)
        return viewholder
    }

    override fun getItemCount(): Int {
        return mesajlar.size
    }

    override fun onBindViewHolder(holder: CanliMesaj_Viewholder, position: Int) {
        holder.atama(position)
    }

    inner class CanliMesaj_Viewholder(val Viev: View, var ViewGroup: ViewGroup):RecyclerView.ViewHolder(Viev) {
        val teksatirmesaj=Viev as LinearLayout
        val konusmabalonu_giden=teksatirmesaj.konusmabalonu_giden
        val konusmabalonu_gelen=teksatirmesaj.konusmabalonu_gelen
        val mesajgiden=teksatirmesaj.Gidenmesaj_c
        val resgiden=teksatirmesaj.Gidenres_c
        val nickgiden=teksatirmesaj.Gidennick_c
        val mesajgelen=teksatirmesaj.Gelenmesaj_c
        val resgelen=teksatirmesaj.Gelenres_c
        val nickgelen=teksatirmesaj.Gelennick_c
        fun atama(pos:Int){
            adapterposition=pos
            Log.e("Pos",pos.toString())
            val mesaj=mesajlar[pos]
            if (!mesaj.engellimi_mesaj!!){
            if (mesaj.res_mesaj== Kullanicidata.Res){
                konusmabalonu_gelen.visibility=View.GONE
                konusmabalonu_giden.visibility=View.VISIBLE
                if (mesaj.hesaptipi!=null){
                    if (mesaj.hesaptipi==0){
                        nickgiden.setText(mesaj.nick_mesaj+"- ADMIN")
                        nickgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgiden.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_admin)
                    }else if (mesaj.hesaptipi==2) {
                        nickgiden.setText(mesaj.nick_mesaj+"- YAZILIM SORUMLUSU")
                        nickgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgiden.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_yazilimci)
                    }
                    else
                    {
                        nickgiden.setText(mesaj.nick_mesaj+"- ABONE")
                        nickgiden.setTextColor(Contex.resources.getColor(R.color.b2))
                        mesajgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgiden.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_abone)
                    }
                }else{
                    nickgiden.setText(mesaj.nick_mesaj)
                    mesajgiden.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_m)
                    nickgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                    mesajgiden.setTextColor(Contex.resources.getColor(R.color.colorAccent))


                }
                mesajgiden.setText(mesaj.mesaj_mesaj)
                if (Kullanicidata.Ayarlar!!.Veritasarufu!!){
                    Picasso.get().load(mesaj.reslow_mesaj).into(resgiden)
                }else{
                    Picasso.get().load(mesaj.res_mesaj).into(resgiden)
                }

            }else{
                konusmabalonu_gelen.visibility=View.VISIBLE
                konusmabalonu_giden.visibility=View.GONE
                mesajgelen.setText(mesaj.mesaj_mesaj)
                if (Kullanicidata.Ayarlar!!.Veritasarufu!!){
                    Picasso.get().load(mesaj.reslow_mesaj).into(resgelen)
                }else{
                    Picasso.get().load(mesaj.res_mesaj).into(resgelen)
                }
                if (mesaj.hesaptipi!=null){
                    if (mesaj.hesaptipi==0){
                        nickgelen.setText(mesaj.nick_mesaj+"- ADMIN")
                        nickgelen.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgelen.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgelen.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_admin)
                    }else if(mesaj.hesaptipi==2){
                        nickgelen.setText(mesaj.nick_mesaj+"- YAZILIM SORUMLUSU")
                        nickgelen.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgelen.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgelen.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_yazilimci)
                    }
                    else{
                        nickgelen.setText(mesaj.nick_mesaj+"- ABONE")
                        nickgelen.setTextColor(Contex.resources.getColor(R.color.b2))
                        mesajgelen.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                        mesajgelen.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_abone)
                    }
                }else{
                    nickgelen.setText(mesaj.nick_mesaj)
                    nickgelen.setTextColor(Contex.resources.getColor(R.color.colorAccent))
                    mesajgelen.setTextColor(Contex.resources.getColor(R.color.colorPrimaryDark4))
                    mesajgelen.background=Contex.resources.getDrawable(R.drawable.konusmabalonu_normal)

                }
            }}
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