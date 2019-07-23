package dev.tantto.maistempo.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.tantto.maistempo.R

class FragmentAvaliacao() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var V = inflater.inflate(R.layout.fragment_avaliacao_local, container, false)
        return V
    }

}