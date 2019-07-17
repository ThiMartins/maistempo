package dev.tantto.maistempo.Modelos

import java.io.Serializable

data class Perfil(
    var Titulo:String = "User",
    var Imagem:String = "",
    var Email:String = "email",
    var Senha:String = "",
    var Raio:Int = 1,
    var PontosCadastro:Int = 0,
    var PontosFila:Int = 0,
    var PontosLocais:Int = 0,
    var PontosTotais:Int = 0,
    var Nascimento:String = ""
) : Serializable