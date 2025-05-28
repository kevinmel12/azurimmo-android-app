package bts.sio.azurimmo.model

import java.sql.Date // ✅ CORRIGÉ: java.sql.Date comme le backend !

data class Contrat(
    val id: Long?,
    val dateEntree: Date,
    val dateSortie: Date,
    val montantLoyer: Double,
    val montantCharges: Double,
    val statut: String,
    val appartement: Appartement? = null,
    val locataire: Locataire? = null
)