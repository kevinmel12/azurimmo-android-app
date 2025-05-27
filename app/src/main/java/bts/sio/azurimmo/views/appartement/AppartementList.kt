package bts.sio.azurimmo.views.appartement

import AppartementViewModel
import BatimentViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.model.Appartement

@Composable
fun AppartementList(
    viewModel: AppartementViewModel = viewModel(),
    batimentId: Int,
    onAddAppartementClick: () -> Unit,
    onAppartementClick: (Int) -> Unit,
    onEditAppartement: (Appartement) -> Unit,
    onDeleteAppartement: (Appartement) -> Unit,
    onBackClick: () -> Unit
) {

    val viewModelBat: BatimentViewModel = viewModel()
    val appartements = viewModel.appartements.value
    val batiment = viewModelBat.batiment.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    LaunchedEffect(batimentId) {
        println("Chargement des données pour le bâtiment : $batimentId")
        viewModel.getAppartementsByBatimentId(batimentId)
        viewModelBat.getBatiment(batimentId)
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
                FloatingActionButton(
                    onClick = {
                        println("Bouton + cliqué")
                        onAddAppartementClick()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    content = {
                        Icon(Icons.Default.Add, contentDescription = "Ajouter un appartement")
                    }
                )

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
                            text = "Appartements",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    LazyColumn {
                        if (batiment != null) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Informations sur le bâtiment",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Adresse : ${batiment.adresse}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Ville : ${batiment.ville}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            if (appartements.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Liste des appartements",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                items(appartements) { appartement ->
                                    AppartementCard(
                                        appartement = appartement,
                                        onClick = { onAppartementClick(appartement.id ?: 0) },
                                        onEdit = onEditAppartement,
                                        onDelete = { appartementToDelete ->
                                            viewModel.deleteAppartement(appartementToDelete.id ?: 0)
                                            onDeleteAppartement(appartementToDelete)
                                        }
                                    )
                                }
                            } else {
                                item {
                                    Text(
                                        text = "Pas d'appartement pour ce bâtiment",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}