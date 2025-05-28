package bts.sio.azurimmo.model

data class Batiment(
    val id: Long, // ✅ CORRIGÉ: Long au lieu de Int pour correspondre au backend
    val adresse: String?,
    val ville: String?
)