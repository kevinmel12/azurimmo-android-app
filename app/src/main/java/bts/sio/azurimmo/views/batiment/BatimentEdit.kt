package bts.sio.azurimmo.views.batiment

import BatimentViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.model.Batiment

@Composable
fun BatimentEdit(
    batimentId: Int,
    onBatimentUpdate: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: BatimentViewModel = viewModel()
    val batiment = viewModel.batiment.value
    var adresse by remember { mutableStateOf("") }
    var ville by remember { mutableStateOf("") }

    // Charger le bâtiment à modifier
    LaunchedEffect(batimentId) {
        viewModel.getBatiment(batimentId)
    }

    // Peupler les champs une fois le bâtiment chargé
    LaunchedEffect(batiment) {
        batiment?.let {
            adresse = it.adresse ?: ""
            ville = it.ville ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Bouton retour
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
            Text(
                text = "Modifier le Bâtiment",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(
            value = adresse,
            onValueChange = { adresse = it },
            label = { Text("Adresse") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = ville,
            onValueChange = { ville = it },
            label = { Text("Ville") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bouton Annuler
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Annuler")
            }

            // Bouton Modifier
            Button(
                onClick = {
                    if (adresse.isNotBlank() && ville.isNotBlank() && batiment != null) {
                        val updatedBatiment = Batiment(
                            id = batiment.id, // ✅ Direct car non-nullable
                            adresse = adresse,
                            ville = ville
                        )
                        viewModel.updateBatiment(updatedBatiment)
                        onBatimentUpdate()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = adresse.isNotBlank() && ville.isNotBlank()
            ) {
                Text("Modifier")
            }
        }
    }
}