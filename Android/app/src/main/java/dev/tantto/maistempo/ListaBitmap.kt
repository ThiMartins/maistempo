package dev.tantto.maistempo

import android.graphics.Bitmap

class ListaBitmap {

    companion object {

        private var Lista = hashMapOf<String, Bitmap>()

        fun adicionar(Imagem:HashMap<String, Bitmap>){
            Lista.putAll(Imagem)
        }

        fun recuperar(Index:String) : Bitmap?{
            return Lista["$Index.jpg"]
        }

        fun tamanho() : Int{
            return Lista.size
        }

        fun refazer(Lista:HashMap<String, Bitmap>){
            this.Lista = Lista
        }

    }

}