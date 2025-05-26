package bts.sio.azurimmo.views.batiment

import BatimentViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BatimentList(
    viewModel: BatimentViewModel = viewModel(),
    onBatimentClick: (Int) -> Unit,
    onAddBatimentClick: () -> Unit
) {
    val batiments = viewModel.batiments.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    LaunchedEffect(Unit) {
        viewModel.getBatiments()
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Titre
                    Text(
                        text = "Gestion des Bâtiments",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Bouton pour ajouter un bâtiment
                    Button(
                        onClick = onAddBatimentClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .widthIn(min = 150.dp, max = 300.dp)
                    ) {
                        Text("Ajouter un bâtiment")
                    }

                    // Liste des bâtiments
                    LazyColumn {
                        items(batiments) { batiment ->
                            BatimentCard(
                                batiment = batiment,
                                onClick = { onBatimentClick(batiment.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}