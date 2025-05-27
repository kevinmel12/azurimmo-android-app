package bts.sio.azurimmo.views.locataire

import LocataireViewModel
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
    onLocataireClick: (Int) -> Unit,
    onEditLocataire: (Locataire) -> Unit,
    onDeleteLocataire: (Locataire) -> Unit,
    onBackClick: () -> Unit
) {
    val locataires = viewModel.locataires.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    // Charger les locataires au démarrage
    LaunchedEffect(Unit) {
        viewModel.getLocataires()
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
                            text = if (contratId != null) "Locataires du contrat" else "Tous les locataires",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

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
                                        viewModel.deleteLocataire(locataireToDelete.id.toInt())
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