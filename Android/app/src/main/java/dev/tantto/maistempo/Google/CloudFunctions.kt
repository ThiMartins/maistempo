package dev.tantto.maistempo.google

import com.google.firebase.functions.FirebaseFunctions
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.classes.Dados

enum class Resultado(val valor:String){
    SUCESSO("sucesso"),
    ERRO("erro")
}

class CloudFunctions {

    companion object {

        fun adicionarNotaFila(Id:String, TipoFila:String, Valor:Double, Horario:String, anInterface:FunctionsInterface){

            val Dados = hashMapOf(
                Pair("id", Id),
                Pair("valor", Valor.toString()),
                Pair("tipoFila", TipoFila),
                Pair("horario", Horario)
            )

            FirebaseFunctions.getInstance().getHttpsCallable(Chave.CHAVE_ADICIONAR_FILA_NOTA.valor).call(Dados).addOnCompleteListener {
                if(it.isSuccessful){
                    anInterface.resultado(Resultado.SUCESSO)
                } else {
                    anInterface.resultado(Resultado.ERRO)
                }
            }
        }

        fun adicionarNovoVoto(Id: String){
            val Dados = hashMapOf(Pair("id", Id))
            FirebaseFunctions.getInstance().getHttpsCallable(Chave.CHAVE_ADD_RANKING.valor).call(Dados)
        }

        fun adicionarNotaLoja(Email:String, Nota:Double, Loja:String){

            val Dados = hashMapOf(
                Pair("email", Email),
                Pair("valor", Nota),
                Pair("loja", Loja)
            )

            FirebaseFunctions.getInstance().getHttpsCallable(Chave.CHAVE_NOTA_LOJA.valor).call(Dados)
        }

    }

}

interface FunctionsInterface {

    fun resultado(Valor:Resultado)

}