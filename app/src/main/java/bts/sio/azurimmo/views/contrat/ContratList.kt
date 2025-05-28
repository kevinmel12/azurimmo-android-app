package bts.sio.azurimmo.views.contrat

import ContratViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import bts.sio.azurimmo.model.Contrat
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*

@Composable
fun ContratList(
    viewModel: ContratViewModel = viewModel(),
    appartementId: Int? = null,
    onAddContratClick: () -> Unit,
    onContratClick: (Long) -> Unit,
    onEditContrat: (Contrat) -> Unit,
    onDeleteContrat: (Contrat) -> Unit,
    onBackClick: () -> Unit
) {
    val contrats = viewModel.contrats.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    // Charger les contrats de cet appartement
    LaunchedEffect(appartementId) {
        if (appartementId != null) {
            println("Chargement des contrats pour l'appartement : $appartementId")
            viewModel.getContratsByAppartementId(appartementId)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Erreur inconnue",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Bouton retour
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                        Text(
                            text = if (appartementId != null) "Contrats de l'appartement" else "Tous les contrats",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Bouton pour ajouter un contrat
                    Button(
                        onClick = onAddContratClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("Ajouter un contrat")
                    }

                    if (contrats.isEmpty()) {
                        Text(
                            text = "Aucun contrat pour cet appartement",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn {
                            items(contrats) { contrat ->
                                ContratCard(
                                    contrat = contrat,
                                    onClick = onContratClick,
                                    onEdit = onEditContrat,
                                    onDelete = onDeleteContrat
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContratCard(
    contrat: Contrat,
    onClick: (Long) -> Unit,
    onEdit: (Contrat) -> Unit,
    onDelete: (Contrat) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                contrat.id?.let { id -> onClick(id) }
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
                Text(
                    text = "Contrat #${contrat.id ?: "Nouveau"}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
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