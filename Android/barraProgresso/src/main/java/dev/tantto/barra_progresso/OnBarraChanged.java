package dev.tantto.barra_progresso;

public interface OnBarraChanged {

    void setOnClickListener(BarraProgresso v);

    void setOnBarraChangeListener(BarraProgresso v, float value);

    void setOnFinishListener(BarraProgresso v);

}