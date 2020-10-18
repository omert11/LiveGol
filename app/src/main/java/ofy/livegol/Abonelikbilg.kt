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

interface datayolla {
    fun datayiyolla(data: String)

}
class Abonelikbilg : androidx.fragment.app.DialogFragment() {
    lateinit var datayolar: datayolla


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        datayolar = context as datayolla

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_abonelik, container, false)
        val wpagerpre=v.findViewById<androidx.viewpager.widget.ViewPager>(R.id.Prebilgrescev_a)
        val indicator=v.findViewById<InkPageIndicator>(R.id.sayfabelirteci_a)
        val kapat=v.findViewById<ImageView>(R.id.prebilkapat_a)
        kapat.setOnClickListener { dismiss() }
        val satinal1=v.findViewById<TextView>(R.id.satinal_3_a)
        satinal1.setOnClickListener { datayolar.datayiyolla("abonelik")
        dismiss()}
        resimleriata(wpagerpre,indicator)
        return v
    }



    private fun resimleriata(wpagerpre: androidx.viewpager.widget.ViewPager?, indicator: InkPageIndicator?) {
        val array=ArrayList<prebilgdata>()
        array.add(prebilgdata(R.drawable.nedenabonelik, "Neden Abonelik?", "Çünkü özel olarak hazırladığımız ve analizini yaptığımız kuponları sadece abone olan üyelerimizle paylaşıyoruz." +
                "Tek tek her maçı analiz ettiğimiz bu kuponları herkese vermek yerine sadece özel üyelerimize ayırdık."))
        array.add(prebilgdata(R.drawable.kacgun, "Abonelik Kaç Gün Sürer?", "Abonelik normal şartlarda 7 gün sürer.Ayın 1inde başlayan birinin abonelik süresi 8inde biter.Tabiki ara ara özel olarak hediyelerimiz olacaktır takipte kalmayı unutmayın."))
        array.add(prebilgdata(R.drawable.abonelikguvenlimi, "Abonelik Almam Güvenlimi?", "Abonelik alırken asla mağdur kalmayacaksınız.Ödemenizi Google Playden güvenli bir şekilde ister kredi kartı isterse telefon faturasına yansıtarak rahat bir şekilde yapabilirsiniz ödemeniz yapıldığı anda abonelik süreniz başlayacak ve 7 gün boyunca kullanabileceksiniz."))
        val adpt=presayfaadapter(array)
        wpagerpre!!.adapter=adpt
        indicator!!.setViewPager(wpagerpre)

    }
}
