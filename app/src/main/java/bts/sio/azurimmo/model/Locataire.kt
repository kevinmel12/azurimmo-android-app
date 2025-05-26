package bts.sio.azurimmo.model

import java.util.Date

data class Locataire(
    val id: Int,
    val nom: String,
    val prenom: String,
    val dateN: Date,
    val lieuN: String
)