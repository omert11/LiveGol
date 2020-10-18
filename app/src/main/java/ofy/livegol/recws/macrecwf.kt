package ofy.livegol.recws

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tek_mac.view.*
import ofy.livegol.R
import ofy.livegol.datas.macdata

class macrecwf(val maclar: ArrayList<macdata>, val Conte: Context, var Durum:Boolean) : androidx.recyclerview.widget.RecyclerView.Adapter<macrecwf.macviewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): macviewholder {
        val intt = LayoutInflater.from(p0.context)
        val teksohbet = intt.inflate(R.layout.tek_mac, p0, false)
        val viewholder = macviewholder(teksohbet, p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return maclar.size
    }

    override fun onBindViewHolder(holder: macviewholder, position: Int) {
        holder.atama(position)
    }

    inner class macviewholder(val Viev: View, var ViewGroup: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(Viev) {
        val teksatirmac = Viev as LinearLayout
        val macsaat = teksatirmac.Macsaat
        val macisim1 = teksatirmac.macisim1
        val macisim2 = teksatirmac.macisim2
        val macoran = teksatirmac.macoran
        val mactahmin = teksatirmac.mactahmin
        val macresim = teksatirmac.macresim
        val macsaatiyaz = teksatirmac.textView_macsaati
        fun atama(position: Int) {
            macisim1.text = maclar[position].macismi1
            macisim2.text = maclar[position].macismi2
            macoran.text = maclar[position].macoran
            macsaatiyaz.text="Dakika"
            if (Durum){
                mactahmin.text=maclar[position].mactahmin
            }else{
            mactahmin.text = "Tahmini Görmek İçin Abone Olunuz"}
            mactahmin.textSize=10f
            macsaat.text = maclar[position].macsaati
            val Contex=Conte.resources
            when (maclar[position].maculke) {
                "Sampiyonlar Ligi" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.championsleague))
                }
                "Uefa" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.uefaeuropaleague))
                }
                "Euro Lig" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.euroleague))
                }
                "Turkiye" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.turkiye))
                }
                "Almanya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.almanya))
                }
                "Amerika" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.amerika))
                }
                "Arjantin" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.arjantin))
                }
                "Arnavutluk" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.arnavutluk))
                }
                "Avusturalya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.avusturalya))
                }
                "Avusturya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.avusturya))
                }
                "Azerbaycan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.azerbaycan))
                }
                "Belarus" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.belarus))
                }
                "Belcika" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.belcika))
                }
                "Brezilya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.brezilya))
                }
                "Cek Cum" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.cek))
                }
                "Cezayir" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.cezayir))
                }
                "Cin Cum" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.cin))
                }
                "Danimarka" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.danimarka))
                }
                "Endonezya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.endonezya))
                }
                "Estonya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.estonya))
                }
                "Fas" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.fas))
                }
                "Finlandiya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.finlandiya))
                }
                "Fransa" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.fransa))
                }
                "Galler" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.galler))
                }
                "Hindistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.hindistan))
                }
                "Hirvatistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.hirvatistan))
                }
                "Hollanda" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.hollanda))
                }
                "Honkong" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.honkong))
                }
                "Ingiltere" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.ingiltere))
                }
                "Iran" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.iran))
                }
                "Iskocya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.iskocya))
                }
                "Ispanya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.ispanya))
                }
                "Israil" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.israil))
                }
                "Isvicre" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.isvicre))
                }
                "Italya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.italya))
                }
                "Izlanda" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.izlanda))
                }
                "Japonya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.japonya))
                }
                "Katar" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.katar))
                }
                "Kazakistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.kazakistan))
                }
                "Kibris" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.kibris))
                }
                "Kolombiya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.kolombiya))
                }
                "Kore" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.kore))
                }
                "Kuzeyirlanda" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.kuzeyirlanda))
                }
                "Litvanya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.litvanya))
                }
                "Macaristan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.macaristan))
                }
                "Makedonya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.makedonya))
                }
                "Meksika" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.meksika))
                }
                "Norvec" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.norvec))
                }
                "Ozbekistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.ozbekistan))
                }
                "Paraguay" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.paraguay))
                }
                "Peru" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.peru))
                }
                "Polonya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.polonya))
                }
                "Portekiz" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.portekiz))
                }
                "Romanya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.romanya))
                }
                "Rusya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.rusya))
                }
                "Sili" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.sili))
                }
                "Sirbistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.sirbistan))
                }
                "Slovakya" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.slovakya))
                }
                "Arabistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.suudiarabistan))
                }
                "Tunus" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.tunus))
                }
                "Ukrayna" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.ukrayna))
                }
                "Uruguay" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.uruguay))
                }
                "Venezuella" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.venezuella))
                }
                "Yunanistan" -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.yunanistan))
                }
                else -> {
                    macresim.setImageDrawable(Contex.getDrawable(R.drawable.digerulke))
                }


            }
        }
    }


}