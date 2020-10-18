package ofy.livegol

import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.teksatir_prebilgres.view.*
import ofy.livegol.datas.prebilgdata

class presayfaadapter ( var Resimlist:ArrayList<prebilgdata>): androidx.viewpager.widget.PagerAdapter() {
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return (p0==p1 as ConstraintLayout)
    }

    override fun getCount(): Int {
        return Resimlist.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inf= LayoutInflater.from(container.context)
        val view=inf.inflate(R.layout.teksatir_prebilgres,container,false)
        val res=view.prebilgirestek
        val baslik=view.BilgBaslik
        val aciklama=view.BilgAciklama
        baslik.setText(Resimlist[position].baslik)
        aciklama.setText(Resimlist[position].aciklama)
        res.setImageResource(Resimlist[position].res!!)
        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, obje: Any) {
        container.removeView(obje as ConstraintLayout)
    }
}