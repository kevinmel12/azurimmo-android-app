package bts.sio.azurimmo.views.locataire

import LocataireViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.model.Locataire
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LocataireEdit(
    locataireId: Int, // ✅ Garde Int pour la compatibilité avec la navigation
    onLocataireUpdate: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: LocataireViewModel = viewModel()
    val locataire = viewModel.locataire.value
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var lieuN by remember { mutableStateOf("") }
    var dateN by remember { mutableStateOf("") }

    // Charger le locataire à modifier
    LaunchedEffect(locataireId) {
        viewModel.getLocataireById(locataireId)
    }

    // Peupler les champs une fois le locataire chargé
    LaunchedEffect(locataire) {
        locataire?.let {
            nom = it.nom ?: ""
            prenom = it.prenom ?: ""
            lieuN = it.lieuN ?: ""
            dateN = it.dateN?.let { date ->
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            } ?: ""
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
                text = "Modifier le Locataire",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = prenom,
            onValueChange = { prenom = it },
            label = { Text("Prénom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dateN,
            onValueChange = { dateN = it },
            label = { Text("Date de naissance (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = lieuN,
            onValueChange = { lieuN = it },
            label = { Text("Lieu de naissance") },
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
                    if (nom.isNotBlank() && prenom.isNotBlank() && locataire != null) {
                        // Conversion de la date
                        val parsedDate = try {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateN)
                        } catch (e: Exception) {
                            locataire.dateN
                        }

                        val updatedLocataire = Locataire(
                            id = locataire.id, // ✅ CORRIGÉ: Garder l'ID original (nullable)
                            nom = nom,
                            prenom = prenom,
                            lieuN = lieuN,
                            dateN = parsedDate?.let { java.sql.Date(it.time) } ?: locataire.dateN
                        )
                        viewModel.updateLocataire(updatedLocataire)
                        onLocataireUpdate()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = nom.isNotBlank() && prenom.isNotBlank()
            ) {
                Text("Modifier")
            }
        }
    }
}