package dev.tantto.maistempo.adaptadores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class AdaptadorPager(Gerenciador:FragmentManager, private val Fragmentos:List<Fragment>) : FragmentStatePagerAdapter(Gerenciador) {

    override fun getItem(position: Int): Fragment {
        return Fragmentos[position]
    }

    override fun getCount(): Int {
        return Fragmentos.size
    }
}