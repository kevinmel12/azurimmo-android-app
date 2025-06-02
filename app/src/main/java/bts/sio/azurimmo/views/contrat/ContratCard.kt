package bts.sio.azurimmo.views.contrat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bts.sio.azurimmo.model.Contrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContratCard(
    contrat: Contrat,
    onEdit: (Contrat) -> Unit = {},
    onDelete: (Contrat) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header avec titre et menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Contrat #${contrat.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Modifier")
                                }
                            },
                            onClick = {
                                showMenu = false
                                onEdit(contrat)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Supprimer")
                                }
                            },
                            onClick = {
                                showMenu = false
                                onDelete(contrat)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Informations financières
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Loyer: ${contrat.montantLoyer}€",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Charges: ${contrat.montantCharges}€",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Statut avec badge coloré
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Statut: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val (statutColor, statutTextColor) = when (contrat.statut) {
                    "Actif" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                    "Résilié" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
                    "En attente" -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                }

                Surface(
                    color = statutColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = contrat.statut,
                        style = MaterialTheme.typography.bodySmall,
                        color = statutTextColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Locataire
            Text(
                text = "Locataire: ${contrat.locataire?.let { "${it.prenom} ${it.nom}" } ?: "Aucun"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ✅ ASSOCIÉ - NOUVEAU
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Associé: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val associeText = contrat.associe?.let { "${it.prenom} ${it.nom}" } ?: "Non assigné"
                val associeColor = if (contrat.associe != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }

                Surface(
                    color = if (contrat.associe != null) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = associeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (contrat.associe != null) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        },
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Appartement info (si disponible)
            contrat.appartement?.let { appartement ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Appartement: N°${appartement.numero} - ${appartement.surface}m²${appartement.batiment?.let { " - ${it.adresse}" } ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Dates (si disponibles)
            contrat.dateEntree?.let { dateEntree ->
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Entrée: ${java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(dateEntree)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    contrat.dateSortie?.let { dateSortie ->
                        Text(
                            text = " • Sortie: ${java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(dateSortie)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Version simplifiée si tu préfères quelque chose de plus compact
@Composable
fun ContratCardSimple(
    contrat: Contrat,
    onEdit: (Contrat) -> Unit = {},
    onDelete: (Contrat) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Contrat #${contrat.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Loyer: ${contrat.montantLoyer}€",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Charges: ${contrat.montantCharges}€",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Statut: ${contrat.statut}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Locataire: ${contrat.locataire?.let { "${it.prenom} ${it.nom}" } ?: "Aucun"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                // ✅ ASSOCIÉ AJOUTÉ
                Text(
                    text = "Associé: ${contrat.associe?.let { "${it.prenom} ${it.nom}" } ?: "Non assigné"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Modifier") },
                        onClick = {
                            showMenu = false
                            onEdit(contrat)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Supprimer") },
                        onClick = {
                            showMenu = false
                            onDelete(contrat)
                        }
                    )
                }
            }
        }
    }
}