package dev.tantto.maistempo.modelos

import java.io.Serializable

class NotasUsuarios(
    val filaNormal:HashMap<String, List<Int>>,
    val filaRapida:HashMap<String, List<Int>>,
    val filaPreferencial:HashMap<String, List<Int>>,
    val notasRanking:HashMap<String, Double>
) : Serializable