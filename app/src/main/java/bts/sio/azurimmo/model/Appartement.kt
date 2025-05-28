package bts.sio.azurimmo.model

data class Appartement(
    val id: Long?, // ✅ CORRIGÉ: Long? au lieu de Int? pour correspondre au backend
    val numero: Int, // ✅ CORRIGÉ: Int au lieu de String pour correspondre au backend (numero est int côté backend)
    val surface: Float,
    val nbPieces: Int, // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces pour correspondre au backend
    val description: String,
    val batiment: Batiment
)