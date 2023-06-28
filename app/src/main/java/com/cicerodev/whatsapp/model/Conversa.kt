package com.cicerodev.whatsapp.model

/**
 * Created by jamiltondamasceno
 */
class Conversa {
    var idRemetente: String? = null
    var idDestinatario: String? = null
    var ultimaMensagem: String? = null
    var usuarioExibicao: Usuario? = null
    var isGroup: String? = null
    var grupo: Grupo? = null

    init {
        isGroup = "false"
    }
}