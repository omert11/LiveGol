package ofy.livegol


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pixelcan.inkpageindicator.InkPageIndicator
import ofy.livegol.datas.prebilgdata


class Puansatinal : androidx.fragment.app.DialogFragment() {
    lateinit var datayolar: datayolla

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        datayolar = context as datayolla

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_puansatinal, container, false)
        val wpagerpre=v.findViewById<androidx.viewpager.widget.ViewPager>(R.id.Prebilgrescev_p)
        val indicator=v.findViewById<InkPageIndicator>(R.id.sayfabelirteci_p)
        val kapat=v.findViewById<ImageView>(R.id.prebilkapat_p)
        kapat.setOnClickListener { dismiss() }
        val satinal1=v.findViewById<TextView>(R.id.satinal_1_p)
        val satinal2=v.findViewById<TextView>(R.id.satinal_2_p)
        satinal1.setOnClickListener { datayolar.datayiyolla("1500puan")
        dismiss()}
        satinal2.setOnClickListener { datayolar.datayiyolla("700puan")
        dismiss()}
        resimleriata(wpagerpre,indicator)
        return v
    }

    private fun resimleriata(wpagerpre: androidx.viewpager.widget.ViewPager?, indicator: InkPageIndicator?) {
        val array=ArrayList<prebilgdata>()
        array.add(prebilgdata(R.drawable.coinsatinal, "Neden Puan Satın Almalıyım", "Dilediğin gibi harcaya bileceğin puanları sen istediğin zaman harca diye koyduk"))
        array.add(prebilgdata(R.drawable.coinsatinal, "Gunlük Özel Maç", "Sadece tek sefere mahsus yüksek ihtimalli özel bir maç satın al.Bunun bedeli 20 kupon olarak belirlenmiştir.Detay için 'Puan Bozdur' Bölümüne Bakabilirsin"))
        array.add(prebilgdata(R.drawable.coinsatinal, "Reklamları 1 Günlüğüne Kaldır", "Reklamlardan sıkıldın mı? Sadece 50 kupona 1 gün reklamlardan kurtul."))
        array.add(prebilgdata(R.drawable.coinsatinal, "Neden Puan Satın Almalıyım", "Dilediğin gibi günlük olarak canlı maçları aç.Bunun bedeli 100 kupon olarak belirlenmiştir"))
        val adpt=presayfaadapter(array)
        wpagerpre!!.adapter=adpt
        indicator!!.setViewPager(wpagerpre)

    }
}
