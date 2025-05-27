package bts.sio.azurimmo.model

import java.util.Date

data class Contrat(
    val id: Int,
    val dateEntree: Date,
    val dateSortie: Date,
    val montantLoyer: Double,
    val montantCharges: Double,
    val statut: String,
    val appartement: Appartement? = null,  // AJOUTÉ
    val locataire: Locataire? = null       // AJOUTÉ
)