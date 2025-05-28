package bts.sio.azurimmo.views.locataire

import LocataireViewModel
import ContratViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.model.Locataire

@Composable
fun LocataireList(
    viewModel: LocataireViewModel = viewModel(),
    contratId: Int? = null,
    onAddLocataireClick: () -> Unit,
    onLocataireClick: (Long) -> Unit,
    onEditLocataire: (Locataire) -> Unit,
    onDeleteLocataire: (Locataire) -> Unit,
    onBackClick: () -> Unit
) {
    // ✅ NOUVEAU: ViewModel pour récupérer le contrat
    val contratViewModel: ContratViewModel = viewModel()
    val contrat = contratViewModel.contrat.value

    val locataires = viewModel.locataires.value
    val isLoading = viewModel.isLoading.value || contratViewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value ?: contratViewModel.errorMessage.value

    // ✅ NOUVEAU: Charger le contrat spécifique au lieu de tous les locataires
    LaunchedEffect(contratId) {
        if (contratId != null) {
            println("🔍 Android - Chargement du contrat: $contratId")
            contratViewModel.getContratById(contratId)
        } else {
            // Si pas de contratId, charger tous les locataires (mode général)
            viewModel.getLocataires()
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
                    text = errorMessage,
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
                            text = if (contratId != null) "Locataire du contrat #$contratId" else "Tous les locataires",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // ✅ LOGIQUE DIFFÉRENTE selon le contexte
                    if (contratId != null) {
                        // MODE CONTRAT : Afficher LE locataire du contrat
                        contrat?.let { currentContrat ->
                            if (currentContrat.locataire != null) {
                                // Le contrat a un locataire assigné
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Locataire assigné :",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        LocataireCard(
                                            locataire = currentContrat.locataire!!,
                                            onClick = onLocataireClick,
                                            onEdit = onEditLocataire,
                                            onDelete = onDeleteLocataire
                                        )
                                    }
                                }
                            } else {
                                // Le contrat n'a pas de locataire assigné
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "⚠️ Aucun locataire assigné",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Ce contrat n'a pas encore de locataire assigné.",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Bouton pour assigner un locataire
                                    Button(
                                        onClick = onAddLocataireClick,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Assigner un locataire")
                                    }
                                }
                            }
                        }
                    } else {
                        // MODE GÉNÉRAL : Afficher tous les locataires
                        // Bouton pour ajouter un locataire
                        Button(
                            onClick = onAddLocataireClick,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text("Ajouter un locataire")
                        }

                        if (locataires.isEmpty()) {
                            Text(
                                text = "Aucun locataire trouvé",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            LazyColumn {
                                items(locataires) { locataire ->
                                    LocataireCard(
                                        locataire = locataire,
                                        onClick = onLocataireClick,
                                        onEdit = onEditLocataire,
                                        onDelete = { locataireToDelete ->
                                            locataireToDelete.id?.let { id ->
                                                viewModel.deleteLocataire(id.toInt())
                                            }
                                            onDeleteLocataire(locataireToDelete)
                                        }
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