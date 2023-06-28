package com.cicerodev.whatsapp.model

import java.io.Serializable

/**
 * Created by jamiltondamasceno
 */
class Usuario : Serializable {

    var id: String? = null
    var nome: String? = null
    var email: String? = null
    var senha: String? = null
    var foto: String? = null

    fun converterParaMap(): Map<String, Any?> {
        val usuarioMap = HashMap<String, Any?>()
        usuarioMap["email"] = email
        usuarioMap["nome"] = nome
        usuarioMap["foto"] = foto
        return usuarioMap
    }
}