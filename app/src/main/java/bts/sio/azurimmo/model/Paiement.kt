package bts.sio.azurimmo.model

import java.util.Date

data class Paiement(
    val id: Int,
    val montant: Double,
    val datePaiement: Date
)