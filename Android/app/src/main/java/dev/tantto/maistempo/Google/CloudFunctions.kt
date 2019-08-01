package dev.tantto.maistempo.Google

import com.google.firebase.functions.FirebaseFunctions
import dev.tantto.maistempo.Chaves.Chaves

enum class Resultado(val valor:String){
    SUCESSO("sucesso"),
    ERRO("erro")
}

class CloudFunctions {

    companion object {

        fun salvarRanking(Id:String, Email:String, Valor:Double, Interface:FunctionsRanking){

            val Dados = hashMapOf(
                Pair("loja", Id),
                Pair("valor", Valor.toString()),
                Pair("pessoa", Email))

            FirebaseFunctions.getInstance().getHttpsCallable(Chaves.CHAVE_NOTAS_RANKING.valor).call(Dados).addOnCompleteListener {
                if(it.isSuccessful){
                    Interface.resultado(Resultado.SUCESSO)
                } else {
                    Interface.resultado(Resultado.ERRO)
                }
            }
        }

    }

}

interface FunctionsRanking {

    fun resultado(Valor:Resultado)

}