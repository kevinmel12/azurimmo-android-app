package bts.sio.azurimmo.model

import java.sql.Date

data class Locataire(
    val id: Long?,
    val nom: String,
    val prenom: String,
    val dateN: Date,
    val lieuN: String
)