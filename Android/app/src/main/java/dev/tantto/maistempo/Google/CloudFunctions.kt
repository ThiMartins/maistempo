package dev.tantto.maistempo.google

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.functions.FirebaseFunctions
import dev.tantto.maistempo.chaves.Chave
import java.lang.Exception
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

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

        fun adicionarNotaLoja(Email:String, Nota:Double, Loja:String){

            val Dados = hashMapOf(
                Pair("email", Email),
                Pair("valor", Nota),
                Pair("loja", Loja)
            )

            FirebaseFunctions.getInstance().getHttpsCallable(Chave.CHAVE_NOTA_LOJA.valor).call(Dados)
        }

        fun atualizarLista(Contexto:Context? = null) {
            val Dia = Calendar.getInstance().time
            var Dataformatada = DateFormat.getInstance().format(Dia)
            Dataformatada = Dataformatada.replace('/', ' ')
            Dataformatada = Dataformatada.replace(':', '-')
            val dados = hashMapOf(Pair("doc", Dataformatada))
            try {
                FirebaseApp.initializeApp(Contexto!!)
                FirebaseFunctions.getInstance().getHttpsCallable("atualizarLista").call(dados)
            } catch (Erro:Exception){
                Erro.printStackTrace()
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun recuperarRaking(Id: String, Interface:ListaRanking){
            FirebaseFunctions.getInstance().getHttpsCallable("recuperarRaking").call(hashMapOf(Pair("id", Id))).addOnCompleteListener {
                if(it.isSuccessful){
                    val valores = it.result?.data as HashMap<String, Double>
                    Interface.resultado(valores)
                }
            }
        }

    }

    interface ListaRanking{

        fun resultado(lista:HashMap<String, Double>?)

    }

}

interface FunctionsInterface {

    fun resultado(Valor:Resultado)

}