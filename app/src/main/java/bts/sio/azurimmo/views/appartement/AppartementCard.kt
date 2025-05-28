package bts.sio.azurimmo.views.appartement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bts.sio.azurimmo.model.Appartement

@Composable
fun AppartementCard(
    appartement: Appartement,
    onClick: (Long) -> Unit, // ✅ CORRIGÉ: Long au lieu de Int pour correspondre au modèle
    onEdit: (Appartement) -> Unit,
    onDelete: (Appartement) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                appartement.id?.let { onClick(it) } // ✅ CORRIGÉ: Gérer l'ID nullable
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Contenu principal de la card
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = "Numero : ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = appartement.numero.toString(), // ✅ CORRIGÉ: toString() car numero est Int
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "Description : ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = appartement.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "Surface : ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = String.format("%.2f m²", appartement.surface),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // ✅ AJOUT: Afficher le nombre de pièces
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Pièces : ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = appartement.nbPieces.toString(), // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Menu contextuel
            Box {
                IconButton(
                    onClick = { showMenu = true }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Options"
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Modifier") },
                        onClick = {
                            showMenu = false
                            onEdit(appartement)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Supprimer") },
                        onClick = {
                            showMenu = false
                            onDelete(appartement)
                        }
                    )
                }
            }
        }
    }
}