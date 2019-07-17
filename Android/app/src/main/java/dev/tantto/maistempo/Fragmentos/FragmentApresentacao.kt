package dev.tantto.maistempo.Fragmentos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.tantto.maistempo.R

@SuppressLint
class FragmentApresentacao(private var Contexto:Context) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(Contexto).inflate(R.layout.fragment_apresentacao, container, false)
    }

}