package bts.sio.azurimmo.model

import java.sql.Date // ✅ CORRIGÉ: java.sql.Date comme le backend !

data class Locataire(
    val id: Long?, // ✅ CORRIGÉ: Long? (nullable) pour permettre les nouveaux locataires
    val nom: String,
    val prenom: String,
    val dateN: Date, // ✅ CORRIGÉ: java.sql.Date au lieu de java.util.Date
    val lieuN: String
)