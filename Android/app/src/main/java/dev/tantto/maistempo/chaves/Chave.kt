package dev.tantto.maistempo.chaves

enum class Chave(val valor:String) {

    CHAVE_LOJA("lojas"),
    CHAVE_USUARIO("usuarios"),
    CHAVE_CIDADE("cidade"),
    CHAVE_USERNAME("username"),
    CHAVE_TITULO("titulo"),
    CHAVE_SENHA("senha"),
    CHAVE_TELA_PRINCIPAL("TELAPRINCIPAL"),
    CHAVE_RAIO("raio"),
    CHAVE_FAVORITOS("lojasFavoritas"),
    CHAVE_NOTAS_USUARIOS("notasUsuarios"),
    CHAVE_POSICAO_LISTA("listaBitmap"),
    CHAVE_ADICIONAR_FILA_NOTA("adicionarFila"),
    CHAVE_ADD_RANKING("adicionarAvalicaoPonto"),
    CHAVE_ADM("adm"),
    CHAVE_ADM_CIDADES("cidades"),
    CHAVE_ACESSO("Acesso"),
    CHAVE_FECHAR("Fechar"),
    CHAVE_MINHA_LOCALIZCAO("localizacao"),
    CHAVE_NOTA_LOJA("adicionarNotaLocal")

}

enum class Requisicoes(val valor: Int){

    REQUISICAO_CAMERA(0),
    REQUISICAO_LEITURA_STORAGE(1),
    REQUISICAO_ESCRITA_STORAGE(2),
    REQUISICAO_FINE_ACCESS(3),
    REQUISICAO_COARSE_ACCESS(3),

}