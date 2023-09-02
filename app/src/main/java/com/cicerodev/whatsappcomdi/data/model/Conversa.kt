package com.cicerodev.whatsappcomdi.data.model

data class Conversa(
    var idRemetente: String = "",
    var idDestinatario: String = "",
    var ultimaMensagem: String = "",
    var usuarioExibicao: User? = User(),
    var isGroup: String = "",
    var grupo: Grupo? = null
): java.io.Serializable {
    init {
        isGroup = "false"
    }
}
