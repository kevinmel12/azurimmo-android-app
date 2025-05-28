package bts.sio.azurimmo.model

data class Appartement(
    val id: Long?,
    val numero: Int,
    val surface: Float,
    val nbPieces: Int,
    val description: String,
    val batiment: Batiment
)