package bts.sio.azurimmo.model

data class Appartement(
    val id: Long = 0,
    val numero: Int = 0,
    val surface: Float = 0f,
    val nbPieces: Int = 0,
    val description: String = "",
    val batiment: Batiment? = null
)