package dev.tantto.maistempo.Modelos

class NotasUsuarios(
    var filaNormal:HashMap<String, List<Int>>,
    var filaRapida:HashMap<String, List<Int>>,
    var filaPreferencial:HashMap<String, List<Int>>,
    var notasRanking:HashMap<String, Double>
)