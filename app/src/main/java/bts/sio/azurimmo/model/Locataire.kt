package bts.sio.azurimmo.model

import java.util.Date

data class Locataire(
    val id: Long = 0,
    val nom: String = "",
    val prenom: String = "",
    val dateN: Date? = null,
    val lieuN: String = ""
)