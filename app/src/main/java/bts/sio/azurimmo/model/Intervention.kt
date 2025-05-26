package bts.sio.azurimmo.model

import java.util.Date

data class Intervention(
    val id: Int,
    val description: String,
    val typeInter: String,
    val dateInter: Date
)