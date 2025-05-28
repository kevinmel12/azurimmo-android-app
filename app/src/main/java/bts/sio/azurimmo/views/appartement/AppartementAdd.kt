package bts.sio.azurimmo.views.appartement

import AppartementViewModel
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
import bts.sio.azurimmo.model.Appartement
import bts.sio.azurimmo.model.Batiment

@Composable
fun AppartementAdd(
    onAddAppartement: () -> Unit,
    batimentId: Long, // ✅ CORRIGÉ: Long comme dans AppNavigation
    onBackClick: () -> Unit
) {
    val viewModel: AppartementViewModel = viewModel()
    val batimentViewModel: BatimentViewModel = viewModel()
    val batiment = batimentViewModel.batiment.value

    var numero by remember { mutableStateOf("") }
    var surface by remember { mutableStateOf("") }
    var nbPieces by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Charger le bâtiment au démarrage
    LaunchedEffect(batimentId) {
        batimentViewModel.getBatiment(batimentId.toInt()) // ✅ Conversion Long -> Int pour l'API
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
                text = "Ajouter un Appartement",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Afficher les informations du bâtiment
        batiment?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Bâtiment sélectionné :",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Adresse : ${it.adresse}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Ville : ${it.ville}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Champs du formulaire
        TextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Numéro de l'appartement") },
            placeholder = { Text("101") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = surface,
            onValueChange = { surface = it },
            label = { Text("Surface (m²)") },
            placeholder = { Text("45.5") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nbPieces,
            onValueChange = { nbPieces = it },
            label = { Text("Nombre de pièces") },
            placeholder = { Text("3") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Appartement avec balcon") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(24.dp))

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

            // Bouton Ajouter
            Button(
                onClick = {
                    if (numero.isNotBlank() && surface.isNotBlank() &&
                        nbPieces.isNotBlank() && description.isNotBlank() && batiment != null) {

                        val nouvelAppartement = Appartement(
                            id = null, // ✅ null pour nouveau (le backend gère l'auto-increment)
                            numero = numero.toIntOrNull() ?: 0, // ✅ CORRIGÉ: Int au lieu de String
                            surface = surface.toFloatOrNull() ?: 0f,
                            nbPieces = nbPieces.toIntOrNull() ?: 0, // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces
                            description = description,
                            batiment = batiment // ✅ Utiliser le bâtiment chargé
                        )

                        println("🏗️ Android - Création appartement: ${nouvelAppartement.numero} pour bâtiment ${batiment.id}")
                        viewModel.addAppartement(nouvelAppartement)
                        onAddAppartement()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = numero.isNotBlank() && surface.isNotBlank() &&
                        nbPieces.isNotBlank() && description.isNotBlank()
            ) {
                Text("Ajouter")
            }
        }
    }
}