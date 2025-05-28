package bts.sio.azurimmo.model

import java.sql.Date // ✅ CORRIGÉ: java.sql.Date comme le backend !

data class Contrat(
    val id: Long?, // ✅ CORRIGÉ: Long au lieu de Int pour correspondre au backend
    val dateEntree: Date, // ✅ CORRIGÉ: java.sql.Date au lieu de java.util.Date
    val dateSortie: Date, // ✅ CORRIGÉ: java.sql.Date au lieu de java.util.Date
    val montantLoyer: Double,
    val montantCharges: Double,
    val statut: String,
    val appartement: Appartement? = null,
    val locataire: Locataire? = null
)