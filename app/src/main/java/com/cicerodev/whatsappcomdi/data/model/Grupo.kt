package com.cicerodev.whatsappcomdi.data.model

data class Grupo(var id: String = "",
                 var nome: String = "",
                 var foto: String = "",
                 var membros: MutableList<User> = mutableListOf()): java.io.Serializable
