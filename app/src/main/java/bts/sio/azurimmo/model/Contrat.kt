package bts.sio.azurimmo.model

import java.util.Date

data class Contrat(
    val id: Long = 0,
    val dateEntree: Date? = null,
    val dateSortie: Date? = null,
    val montantLoyer: Double = 0.0,
    val montantCharges: Double = 0.0,
    val statut: String = "",
    val appartement: Appartement? = null,
    val locataire: Locataire? = null,
    val associe: Associe? = null
)