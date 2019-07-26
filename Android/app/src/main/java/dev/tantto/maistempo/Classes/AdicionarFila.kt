package dev.tantto.maistempo.Classes

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.widget.Button
import dev.tantto.maistempo.R

class AdicionarFila {

    private var CaixaAlerta:AlertDialog? = null
    private var CaixaAlertaBuilder:AlertDialog.Builder? = null

    fun criarTela(Contexto:Context) : AdicionarFila{
        CaixaAlertaBuilder = AlertDialog.Builder(Contexto)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CaixaAlertaBuilder?.setView(R.layout.adicionar_fila)
            CaixaAlertaBuilder?.setTitle("Teste")
            CaixaAlerta = CaixaAlertaBuilder?.create()
            CaixaAlerta?.window?.setBackgroundDrawable(Contexto.getDrawable(R.drawable.bordas_dialog))
        }
        return this
    }

    fun showTela(){
        CaixaAlerta?.show()
    }

    fun fecharTela(){
        CaixaAlerta?.dismiss()
    }

    fun botaoEnviar() : Button?{
        return CaixaAlerta?.findViewById<Button>(R.id.SalvarFila)
    }

    fun botaoCancelar() : Button?{
        return CaixaAlerta?.findViewById<Button>(R.id.CancelarFila)
    }

}