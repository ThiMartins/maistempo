package dev.tantto.maistempo.classes

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

class HttpDataHandler {

    fun recuperarHttpData(requestURL:String) : String {
        val url:URL
        var resposta = ""
        try {
            url = URL("https://maps.googleapis.com/maps/api/geocode/json?address=Via$requestURL&key=AIzaSyDD64ChVEYMLcV7lwnkaXhhcx_u_5Uu1Go")
            val conexao = url.openConnection() as HttpURLConnection
            conexao.requestMethod = "GET"
            conexao.readTimeout = 150000
            conexao.connectTimeout = 150000
            conexao.doInput = true
            conexao.doOutput = true
            conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val respostaCode = conexao.responseCode
            if(respostaCode == HttpURLConnection.HTTP_OK){

                val Linhas = BufferedReader(InputStreamReader(conexao.inputStream))
                resposta = Linhas.readText()
            }
        } catch (Erro:ProtocolException){
            Erro.printStackTrace()
        } catch (Erro:MalformedURLException){
            Erro.printStackTrace()
        } catch (Erro:IOException){
            Erro.printStackTrace()
        }

        return resposta
    }

}