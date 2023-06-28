package com.cicerodev.whatsapp.model

import java.io.Serializable

/**
 * Created by jamiltondamasceno
 */
class Grupo : Serializable {
    var id: String? = null
    var nome: String? = null
    var foto: String? = null
    var membros: List<Usuario>? = null
}