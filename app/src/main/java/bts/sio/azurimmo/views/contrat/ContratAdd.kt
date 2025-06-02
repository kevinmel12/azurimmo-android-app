package bts.sio.azurimmo.views.contrat

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bts.sio.azurimmo.api.RetrofitInstance
import bts.sio.azurimmo.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContratAdd(onAddContrat: () -> Unit, appartementId: Int, onBackClick: () -> Unit) {
    var selectedLocataireId by remember { mutableStateOf(0L) }
    var selectedAssocieId by remember { mutableStateOf(0L) }
    var loyer by remember { mutableStateOf("") }
    var charges by remember { mutableStateOf("") }
    var statut by remember { mutableStateOf("") }
    var parsedDateEntree by remember { mutableStateOf<Date?>(null) }
    var parsedDateSortie by remember { mutableStateOf<Date?>(null) }

    var locataires by remember { mutableStateOf<List<Locataire>>(emptyList()) }
    var associes by remember { mutableStateOf<List<Associe>>(emptyList()) }
    var showDateEntreePicker by remember { mutableStateOf(false) }
    var showDateSortiePicker by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Charger les données au démarrage
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            locataires = RetrofitInstance.api.getLocataires()
            associes = RetrofitInstance.api.getAssocies()
            Log.d("ContratAdd", "Chargé ${locataires.size} locataires et ${associes.size} associés")
        } catch (e: Exception) {
            Log.e("ContratAdd", "Erreur chargement données", e)
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header avec bouton retour
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
            Text(
                text = "Ajouter un contrat",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Dropdown Locataire
            var expandedLocataire by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedLocataire,
                onExpandedChange = { expandedLocataire = !expandedLocataire }
            ) {
                OutlinedTextField(
                    value = locataires.find { it.id == selectedLocataireId }?.let { "${it.prenom} ${it.nom}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sélectionner un locataire") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocataire) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedLocataire,
                    onDismissRequest = { expandedLocataire = false }
                ) {
                    locataires.forEach { locataire ->
                        DropdownMenuItem(
                            text = { Text("${locataire.prenom} ${locataire.nom}") },
                            onClick = {
                                selectedLocataireId = locataire.id
                                expandedLocataire = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Associé
            var expandedAssocie by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedAssocie,
                onExpandedChange = { expandedAssocie = !expandedAssocie }
            ) {
                OutlinedTextField(
                    value = associes.find { it.id == selectedAssocieId }?.let { "${it.prenom} ${it.nom}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sélectionner un associé responsable") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAssocie) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedAssocie,
                    onDismissRequest = { expandedAssocie = false }
                ) {
                    associes.forEach { associe ->
                        DropdownMenuItem(
                            text = { Text("${associe.prenom} ${associe.nom}") },
                            onClick = {
                                selectedAssocieId = associe.id
                                expandedAssocie = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Champ Loyer
            OutlinedTextField(
                value = loyer,
                onValueChange = { loyer = it },
                label = { Text("Montant du loyer (€)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ Charges
            OutlinedTextField(
                value = charges,
                onValueChange = { charges = it },
                label = { Text("Montant des charges (€)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Statut
            var expandedStatut by remember { mutableStateOf(false) }
            val statutOptions = listOf("Actif", "Résilié", "En attente")

            ExposedDropdownMenuBox(
                expanded = expandedStatut,
                onExpandedChange = { expandedStatut = !expandedStatut }
            ) {
                OutlinedTextField(
                    value = statut,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Statut") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatut) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedStatut,
                    onDismissRequest = { expandedStatut = false }
                ) {
                    statutOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                statut = option
                                expandedStatut = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton de création
            val isFormValid = selectedLocataireId != 0L &&
                    selectedAssocieId != 0L &&
                    loyer.isNotEmpty() &&
                    charges.isNotEmpty() &&
                    statut.isNotEmpty()

            Button(
                onClick = {
                    if (isFormValid) {
                        val contratScope = CoroutineScope(Dispatchers.IO)
                        contratScope.launch {
                            try {
                                val selectedLocataire = locataires.find { it.id == selectedLocataireId }
                                val selectedAssocie = associes.find { it.id == selectedAssocieId }

                                val nouveauContrat = Contrat(
                                    id = 0,
                                    dateEntree = parsedDateEntree,
                                    dateSortie = parsedDateSortie,
                                    montantLoyer = loyer.toDoubleOrNull() ?: 0.0,
                                    montantCharges = charges.toDoubleOrNull() ?: 0.0,
                                    statut = statut,
                                    appartement = Appartement(id = appartementId.toLong()),
                                    locataire = selectedLocataire,
                                    associe = selectedAssocie
                                )

                                Log.d("ContratAdd", "Création contrat: $nouveauContrat")

                                val response = RetrofitInstance.api.addContrat(nouveauContrat)
                                if (response.isSuccessful) {
                                    Log.d("ContratAdd", "Contrat créé avec succès")
                                    withContext(Dispatchers.Main) {
                                        onAddContrat()
                                    }
                                } else {
                                    Log.e("ContratAdd", "Erreur response: ${response.code()}, ${response.message()}")
                                }
                            } catch (e: Exception) {
                                Log.e("ContratAdd", "Erreur création contrat", e)
                            }
                        }
                    } else {
                        Log.w("ContratAdd", "Formulaire invalide")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Créer le contrat")
            }

            // Debug info
            if (selectedLocataireId != 0L || selectedAssocieId != 0L) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Sélection actuelle:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (selectedLocataireId != 0L) {
                            Text(
                                text = "Locataire: ${locataires.find { it.id == selectedLocataireId }?.let { "${it.prenom} ${it.nom}" }}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (selectedAssocieId != 0L) {
                            Text(
                                text = "Associé: ${associes.find { it.id == selectedAssocieId }?.let { "${it.prenom} ${it.nom}" }}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}