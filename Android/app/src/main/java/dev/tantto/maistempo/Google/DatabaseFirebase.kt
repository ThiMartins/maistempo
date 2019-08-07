package dev.tantto.maistempo.google

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import dev.tantto.maistempo.chaves.Chave
import dev.tantto.maistempo.ListaLocais
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil
import java.io.IOException

enum class TipoPontos(val valor:String){
    PONTOS_FILA("pontosFila"),
    PONTOS_AVALIACAO("pontosLocais"),
    PONTOS_TOTAIS("pontosTotais"),
    PONTOS_CADASTRO("pontosCadastro")
}

enum class Respostas{
    SUCESSO,
    ERRO
}

enum class TipoFila(val valor: String){
    FilaNormal("filaNormal"),
    FilaRapida("filaRapida"),
    FilaPrererencial("filaPreferencial")
}

class DatabaseFirebaseSalvar {

    companion object {

        fun salvarDados(Dados:Perfil){
            try {
                FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Dados.email).set(Dados)
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }

        fun adicionarPontos(Email: String, Pontos:Int, Tipo:TipoPontos){
            try {
                val Documento =
                    FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).get()
                Documento.addOnSuccessListener {
                    val valorRecuperado = it.get(Tipo.valor).toString().toLong()
                    val fila = it.get(TipoPontos.PONTOS_FILA.valor).toString().toLong()
                    val avaliacao = it.get(TipoPontos.PONTOS_AVALIACAO.valor).toString().toLong()
                    val cadastro = it.get(TipoPontos.PONTOS_CADASTRO.valor).toString().toLong()
                    FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email)
                        .update(Tipo.valor, Pontos + valorRecuperado)
                    FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email)
                        .update(TipoPontos.PONTOS_TOTAIS.valor, fila + avaliacao + cadastro + 1)
                }
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }

        }

        @Suppress("UNCHECKED_CAST")
        fun adicionarFavorito(Email: String, LojaRecebida:String){
            try {
                FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val Recuperado = it.result?.get(Chave.CHAVE_FAVORITOS.valor)
                        if(Recuperado != null){
                            val lojas = Recuperado as MutableList<String>
                            lojas.add(LojaRecebida)
                            FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).update(Chave.CHAVE_FAVORITOS.valor, lojas)

                        } else {
                            val lista = mutableListOf(LojaRecebida)
                            FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).update(Chave.CHAVE_FAVORITOS.valor, lista)
                        }
                    }
                }
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun removerFavorito(Email: String, LojaRecebida:String){
            try {
                FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val Resultado = it.result?.get(Chave.CHAVE_FAVORITOS.valor) as MutableList<String>
                        if(Resultado.contains(LojaRecebida)){
                            Resultado.remove(LojaRecebida)
                            FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).update(Chave.CHAVE_FAVORITOS.valor, Resultado)
                        }
                    }
                }
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }

        fun mudarRaio(Email: String, Valor: Int){
            try {
                FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).update(Chave.CHAVE_RAIO.valor, Valor)
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }

        fun mudarNomeComImagem(Email: String, Nome: String = "", Caminho:Uri = Uri.EMPTY, Resposta: DatabaseMudanca){
            try {
                if(Nome.isNotEmpty() && Caminho == Uri.EMPTY){
                    FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).update(Chave.CHAVE_TITULO.valor, Nome).addOnCompleteListener {
                        if(it.isSuccessful){
                            Resposta.resposta(Respostas.SUCESSO)
                        } else {
                            Resposta.resposta(Respostas.ERRO)
                        }
                    }
                } else if(Nome.isEmpty() && Caminho != Uri.EMPTY){
                    CloudStorageFirebase.mudarImagem(Caminho, Email).addOnCompleteListener {
                        if(it.isSuccessful){
                            Resposta.resposta(Respostas.SUCESSO)
                        } else {
                            Resposta.resposta(Respostas.ERRO)
                        }
                    }
                } else {
                    FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).update(Chave.CHAVE_TITULO.valor, Nome).addOnCompleteListener { it ->
                        if(it.isSuccessful){
                            Resposta.resposta(Respostas.SUCESSO)
                            CloudStorageFirebase.mudarImagem(Caminho, Email).addOnCompleteListener {
                                if(it.isSuccessful){
                                    Resposta.resposta(Respostas.SUCESSO)
                                } else {
                                    Resposta.resposta(Respostas.ERRO)
                                }
                            }
                        } else {
                            Resposta.resposta(Respostas.ERRO)
                        }
                    }
                }
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }

        fun deletarConta(Email: String){
            try {
                FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).delete()
                CloudStorageFirebase.deletarImagem(Email)
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }

        fun enviarRanking(Id:String, Email:String, Valor:Double){
            try {
                FirebaseFirestore.getInstance().collection(Chave.CHAVE_NOTAS_USUARIOS.valor).document(Id).update(Chave.CHAVE_NOTAS_RANKING.valor, hashMapOf(
                    Pair(Email, Valor)
                ))
            } catch (Erro:FirebaseFirestoreException){
                Erro.printStackTrace()
            }
        }
    }

}

class DatabaseFirebaseRecuperar {

    companion object {

        @Suppress("unchecked_cast")
        fun recuperarLojasLocais(Cidade:String, Interface:DatabaseLocaisInterface){
            FirebaseFirestore.getInstance().collection(Chave.CHAVE_LOJA.valor).whereEqualTo(Chave.CHAVE_CIDADE.valor, Cidade.toLowerCase()).addSnapshotListener { querySnapshot, firebaseException ->
                when {
                    querySnapshot?.documents?.isNotEmpty()!! -> {
                        val ListaFinal = mutableListOf<Lojas>()
                        for(Item in querySnapshot){
                            ListaFinal.add(Lojas(
                                id = Item.id,
                                titulo =  Item["titulo"].toString(),
                                latitude = Item["latitude"].toString().toDouble(),
                                longitude = Item["longitude"].toString().toDouble(),
                                local = Item["local"].toString(),
                                filaNormal = Item["filaNormal"] as HashMap<String, Double>,
                                cidade = Item["cidade"].toString().toLowerCase(),
                                telefone = Item["telefone"].toString(),
                                quantidadeAvaliacoesFila = Item["quantidadeAvaliacoesFila"].toString().toInt(),
                                quantidadeAvaliacoesRating = Item["quantidadeAvaliacoesRating"].toString().toInt(),
                                filaPreferencial = Item["filaPreferencial"] as HashMap<String, Double>,
                                filaRapida = Item["filaRapida"] as HashMap<String, Double>,
                                horarioInicio = Item["horarioInicio"].toString().toInt(),
                                horariofinal = Item["horarioFinal"].toString().toInt(),
                                mediaRanking = Item["mediaRanking"].toString().toDouble()
                            ))
                        }
                        Interface.dadosRecebidosLojas(ListaFinal, "")
                    }
                    firebaseException == null -> Interface.dadosRecebidosLojas(mutableListOf(), "")
                    else -> Interface.dadosRecebidosLojas(mutableListOf(), firebaseException.localizedMessage!!)
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun recuperaDadosPessoa(Email:String, Interface:DatabasePessoaInterface){
            FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).addSnapshotListener { documentSnapshot, _ ->
                if(documentSnapshot?.exists()!!){
                    val Item = Perfil(
                        titulo = documentSnapshot["titulo"].toString(),
                        email = documentSnapshot["email"].toString(),
                        raio = documentSnapshot["raio"].toString().toLong(),
                        pontosCadastro = documentSnapshot["pontosCadastro"].toString().toLong(),
                        pontosFila = documentSnapshot["pontosFila"].toString().toLong(),
                        pontosLocais = documentSnapshot["pontosLocais"].toString().toLong(),
                        pontosTotais = documentSnapshot["pontosTotais"].toString().toLong(),
                        nascimento = documentSnapshot["nascimento"].toString(),
                        lojasFavoritas = documentSnapshot["lojasFavoritas"] as MutableList<String>,
                        cidade = documentSnapshot["cidade"].toString().toLowerCase(),
                        acesso = documentSnapshot["acesso"].toString()
                    )
                    val ListaTemp = documentSnapshot["lojasFavoritas"] as MutableList<String>
                    ListaLocais.refazerFavoritos(ListaTemp)
                    Interface.pessoaRecebida(Item)
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun recuperarFavoritos(Email:String, Interface:FavoritosRecuperados){
            FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).document(Email).addSnapshotListener { documentSnapshot, _ ->
                if(documentSnapshot?.exists()!!){
                    val ListaTemp = documentSnapshot["lojasFavoritas"] as MutableList<String>
                    ListaLocais.refazerFavoritos(ListaTemp)
                    Interface.recuperadoFavoritos()
                }
            }
        }

        fun recuperarTopRanking(Interface:DatabaseRakingInterface){
            FirebaseFirestore.getInstance().collection(Chave.CHAVE_USUARIO.valor).orderBy(TipoPontos.PONTOS_TOTAIS.valor, Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val ListaFinal = mutableListOf<Perfil>()
                    for(Item in it.result?.documents!!){
                        ListaFinal.add(Perfil(
                            titulo =  Item["titulo"].toString(),
                            pontosTotais = Item["pontosTotais"].toString().toLong(),
                            email = Item["email"].toString()
                        ))
                    }

                    Interface.topRanking(ListaFinal)
                }
            }

        }
    }
}

interface FavoritosRecuperados{

    fun recuperadoFavoritos()
}

interface DatabaseLocaisInterface{

    fun dadosRecebidosLojas(Lista:MutableList<Lojas>, Erros:String)

}

interface DatabasePessoaInterface{

    fun pessoaRecebida(Pessoa:Perfil)

}

interface DatabaseRakingInterface{

    fun topRanking(Lista:MutableList<Perfil>)

}

interface DatabaseMudanca{

    fun resposta(Resposta:Respostas)

}