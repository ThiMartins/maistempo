package dev.tantto.maistempo

import android.graphics.Bitmap

class ListaBitmap {

    companion object {

        private var Lista = mutableListOf<Bitmap>()

        fun adicionar(Image:Bitmap){
            Lista.add(Image)
        }

        fun recuperar(Index:Int) : Bitmap?{
            return Lista[Index]
        }

        fun tamanho() : Int{
            return Lista.size
        }

    }

}