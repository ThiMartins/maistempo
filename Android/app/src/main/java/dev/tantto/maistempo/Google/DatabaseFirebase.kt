package dev.tantto.maistempo.Google

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.ListaFavoritos
import dev.tantto.maistempo.Modelos.NotasLojas
import dev.tantto.maistempo.Modelos.Lojas
import dev.tantto.maistempo.Modelos.Perfil

enum class TipoPontos(val valor:String){
    PONTOS_FILA("pontosFila"),
    PONTOS_AVALIACAO("pontosLocais"),
    PONTOS_TOTAIS("pontosTotais"),
    PONTOS_CADASTRO("pontosCadastro")
}

enum class Respostas(valor: String){
    SUCESSO("sucesso"),
    ERRO("erro")
}

class DatabaseFirebaseSalvar {

    companion object {

        fun salvarDados(Dados:Perfil){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Dados.email).set(Dados)
        }

        fun adicionarPontos(Email: String, Pontos:Int, Tipo:TipoPontos){
            val Documento = FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).get()
            Documento.addOnSuccessListener {
                val valorRecuperado = it.get(Tipo.valor).toString().toLong()
                val fila = it.get(TipoPontos.PONTOS_FILA.valor).toString().toLong()
                val avaliacao = it.get(TipoPontos.PONTOS_AVALIACAO.valor).toString().toLong()
                val cadastro = it.get(TipoPontos.PONTOS_CADASTRO.valor).toString().toLong()
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Tipo.valor, Pontos + valorRecuperado)
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(TipoPontos.PONTOS_TOTAIS.valor, fila + avaliacao + cadastro + 1)
            }

        }

        fun adicionarNotaFila(Nome:String, Valor:Int){
            val Documento = FirebaseFirestore.getInstance().collection(Chaves.CHAVE_LOJA.valor).document(Nome).get()
            Documento.addOnSuccessListener {

            }
        }

        @Suppress("UNCHECKED_CAST")
        fun adicionarFavorito(Email: String, LojaRecebida:String){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val Recuperado = it.result?.get(Chaves.CHAVE_FAVORITOS.valor)
                    if(Recuperado != null){
                        val lojas = Recuperado as MutableList<String>
                        lojas.add(LojaRecebida)
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_FAVORITOS.valor, lojas)

                    } else {
                        val lista = mutableListOf(LojaRecebida)
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_FAVORITOS.valor, lista)
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun removerFavorito(Email: String, LojaRecebida:String){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val Resultado = it.result?.get(Chaves.CHAVE_FAVORITOS.valor) as MutableList<String>
                    if(Resultado.contains(LojaRecebida)){
                        Resultado.remove(LojaRecebida)
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_FAVORITOS.valor, Resultado)
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun salvarNotaRaking(Email: String, Id:String, Valor:Float){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_NOTAS_USUARIOS.valor).document(Id).get().addOnCompleteListener {
                if(it.isSuccessful){
                    if(!it.result?.contains(Chaves.CHAVE_NOTAS_RANKING.valor)!!){
                        val Iniciar = NotasLojas(hashMapOf(Pair(Email, Valor)))
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_NOTAS_USUARIOS.valor).document(Id).set(Iniciar)
                    } else if(it.result?.exists()!!){
                        val Resultado = it.result?.get(Chaves.CHAVE_NOTAS_RANKING.valor) as HashMap<String, Float>
                        Resultado[Email] = Valor
                        FirebaseFirestore.getInstance().collection(Chaves.CHAVE_NOTAS_USUARIOS.valor).document(Id).update(Chaves.CHAVE_NOTAS_RANKING.valor, Resultado)
                    }
                }
            }
        }

        fun mudarRaio(Email: String, Valor: Int){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_RAIO.valor, Valor)
        }

        fun mudarNomeComImagem(Email: String, Nome: String = "", Caminho:Uri = Uri.EMPTY, Resposta: DatabaseMudanca){
            if(Nome.isNotEmpty() && Caminho == Uri.EMPTY){
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_TITULO.valor, Nome).addOnCompleteListener {
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
                FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).update(Chaves.CHAVE_TITULO.valor, Nome).addOnCompleteListener { it ->
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
        }

        fun deletarConta(Email: String){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).delete()
            CloudStorageFirebase.deletarImagem(Email)
        }
    }

}

class DatabaseFirebaseRecuperar {

    companion object {

        @Suppress("unchecked_cast")
        fun recuperarLojasLocais(Cidade:String, Interface:DatabaseLocaisInterface){

            /*FirebaseFirestore.getInstance().collection(Chaves.CHAVE_LOJA.valor).add(Lojas(
                "+Tempo",
                listOf("Aberto: 08:00/17:00", "Fechado"),
                "https://firebasestorage.googleapis.com/v0/b/maistempo-desenvolvendo.appspot.com/o/iconesLojas%2FMaistempoCircle.png?alt=media&token=b9cf13f2-fc47-4382-8bb9-96b4eb029780",
                0.5,
                9.7,
                "Avenida General Osório",
                listOf(100, 80, 50, 30, 70, 5, 95),
                listOf(20, 40, 75, 50, 60, 15, 25),
                listOf(65, 20, 35, 80, 17, 50, 60),
                "Sorocaba",
                "15 99999-9999",
                listOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00"),
                10,
                1,
                4.5F
            ))*/

            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_LOJA.valor).whereEqualTo(Chaves.CHAVE_CIDADE.valor, Cidade).addSnapshotListener { querySnapshot, _ ->
                if(querySnapshot?.documents?.isNotEmpty()!!){
                    val ListaFinal = mutableListOf<Lojas>()
                    for(Item in querySnapshot){
                        ListaFinal.add(Lojas(
                            id = Item.id,
                            titulo =  Item["titulo"].toString(),
                            status = Item["status"] as List<String>,
                            imagem = Item["imagem"].toString(),
                            latitude = Item["latitude"].toString().toDouble(),
                            longitude = Item["longitude"].toString().toDouble(),
                            local = Item["local"].toString(),
                            filaNormal = Item["filaNormal"] as List<Int>,
                            cidade = Item["cidade"].toString(),
                            telefone = Item["telefone"].toString(),
                            horarios = Item["horarios"] as List<String>,
                            quantidadeAvaliacoesFila = Item["quantidadeAvaliacoesFila"].toString().toInt(),
                            quantidadeAvaliacoesRating = Item["quantidadeAvaliacoesRating"].toString().toInt(),
                            filaPreferencial = Item["filaPreferencial"] as List<Int>,
                            filaRapida = Item["filaRapida"] as List<Int>,
                            mediaRating = Item["mediaRating"].toString().toFloat()
                        ))
                    }
                    Interface.dadosRecebidos(ListaFinal)
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun recuperaDadosPessoa(Email:String, Interface:DatabasePessoaInterface){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).document(Email).addSnapshotListener { documentSnapshot, _ ->
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
                        favoritos = documentSnapshot["lojasFavoritas"] as MutableList<String>
                    )
                    ListaFavoritos.Lista = documentSnapshot["lojasFavoritas"] as MutableList<String>
                    Interface.pessoaRecebida(Item)
                }
            }
        }

        fun recuperarTopRanking(Interface:DatabaseRakingInterface){
            FirebaseFirestore.getInstance().collection(Chaves.CHAVE_USUARIO.valor).orderBy(TipoPontos.PONTOS_TOTAIS.valor, Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener {
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

        fun recuperarNotaRanking(Email:String, Interface:DatabaseNotaRaking){
            //Recuperar o especifico
        }
    }
}

interface DatabaseNotaRaking{

    fun Nota(Nota:String?)

}

interface DatabaseLocaisInterface{

    fun dadosRecebidos(Lista:MutableList<Lojas>)

}

interface DatabasePessoaInterface{

    fun pessoaRecebida(Pessoa:Perfil)

}

interface DatabaseRakingInterface{

    fun topRanking(Lista:List<Perfil>)

}

interface DatabaseMudanca{

    fun resposta(Resposta:Respostas)

}