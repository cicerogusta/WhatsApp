package com.cicerodev.whatsappcomdi.data.model

class Mensagem(
    var idUsuario: String? = null,
    var mensagem: String? = null,
    val imagem: String? = null,
    var nome: String? = null
) : java.io.Serializable {

    init {
        nome = ""
    }
}