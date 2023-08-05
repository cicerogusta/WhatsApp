package com.cicerodev.whatsappcomdi.data.model

data class User(
    var id: String? = "",
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    var foto: String? = null

    ): java.io.Serializable {

    fun map(): Map<String, Any> {
        val userMap = HashMap<String, Any>()
        userMap.put("email", email)
        userMap.put("nome", nome)
        foto?.let { userMap.put("foto", it) }
        return userMap
    }

}