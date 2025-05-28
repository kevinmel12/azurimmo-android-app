package bts.sio.azurimmo.views.appartement

import AppartementViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.model.Appartement

@Composable
fun AppartementEdit(
    appartementId: Long, // ✅ CORRIGÉ: Long au lieu de Int pour correspondre au modèle
    onAppartementUpdate: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: AppartementViewModel = viewModel()
    val appartement = viewModel.appartement.value
    var description by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var nbPieces by remember { mutableStateOf("") } // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces
    var surface by remember { mutableStateOf("") }

    // Charger l'appartement à modifier
    LaunchedEffect(appartementId) {
        viewModel.getAppartementById(appartementId.toInt()) // ✅ Conversion Long -> Int pour l'API
    }

    // Peupler les champs une fois l'appartement chargé
    LaunchedEffect(appartement) {
        appartement?.let {
            description = it.description
            numero = it.numero.toString() // ✅ CORRIGÉ: toString() car numero est Int
            nbPieces = it.nbPieces.toString() // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces
            surface = it.surface.toString()
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
                text = "Modifier l'Appartement",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Numéro") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = surface,
            onValueChange = { surface = it },
            label = { Text("Surface (m²)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nbPieces, // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces
            onValueChange = { nbPieces = it },
            label = { Text("Nombre de pièces") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
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
                    if (description.isNotBlank() && numero.isNotBlank() &&
                        nbPieces.isNotBlank() && surface.isNotBlank() && appartement != null) {

                        val updatedAppartement = Appartement(
                            id = appartement.id,
                            numero = numero.toIntOrNull() ?: appartement.numero, // ✅ CORRIGÉ: Int
                            description = description,
                            surface = surface.toFloatOrNull() ?: appartement.surface,
                            nbPieces = nbPieces.toIntOrNull() ?: appartement.nbPieces, // ✅ CORRIGÉ: nbPieces
                            batiment = appartement.batiment
                        )
                        viewModel.updateAppartement(updatedAppartement)
                        onAppartementUpdate()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = description.isNotBlank() && numero.isNotBlank() &&
                        nbPieces.isNotBlank() && surface.isNotBlank()
            ) {
                Text("Modifier")
            }
        }
    }
}