package dev.tantto.barra_progresso;

public enum IndicadoresTipos {

    Quadrado(0), Oval(1), Circular(2), QuadradoArrendodado(3);

    public final int valor;

    IndicadoresTipos(int Tipo) {
        valor = Tipo;
    }
}