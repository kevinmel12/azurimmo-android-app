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
fun LocataireAdd(onAddLocataire: () -> Unit, onBackClick: () -> Unit) {
    val viewModel: LocataireViewModel = viewModel()
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var lieuN by remember { mutableStateOf("") }
    var dateN by remember { mutableStateOf("") }

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
                text = "Ajouter un Locataire",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            placeholder = { Text("Dupont") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = prenom,
            onValueChange = { prenom = it },
            label = { Text("Prénom") },
            placeholder = { Text("Jean") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dateN,
            onValueChange = { dateN = it },
            label = { Text("Date de naissance (DD/MM/YYYY)") },
            placeholder = { Text("15/03/1990") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = lieuN,
            onValueChange = { lieuN = it },
            label = { Text("Lieu de naissance") },
            placeholder = { Text("Paris") },
            modifier = Modifier.fillMaxWidth()
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
                    if (nom.isNotBlank() && prenom.isNotBlank() &&
                        dateN.isNotBlank() && lieuN.isNotBlank()) {

                        println("🔍 Android - Création locataire: $prenom $nom")

                        try {
                            // ✅ CORRIGÉ: Conversion de date plus robuste
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val parsedDate = try {
                                dateFormat.parse(dateN)
                            } catch (e: Exception) {
                                println("❌ Erreur parsing date: ${e.message}")
                                Date() // Date actuelle par défaut si erreur
                            }

                            // ✅ CORRIGÉ: Utiliser id = null pour nouveau locataire
                            val locataire = Locataire(
                                id = null, // ✅ NULL pour nouveau locataire (auto-increment)
                                nom = nom,
                                prenom = prenom,
                                lieuN = lieuN,
                                dateN = parsedDate?.let { java.sql.Date(it.time) } ?: java.sql.Date(Date().time)
                            )

                            println("👤 Android - Locataire créé: ${locataire.prenom} ${locataire.nom}, né le ${locataire.dateN}")
                            viewModel.addLocataire(locataire)
                            onAddLocataire()

                        } catch (e: Exception) {
                            println("❌ Android - Erreur création locataire: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = nom.isNotBlank() && prenom.isNotBlank() &&
                        dateN.isNotBlank() && lieuN.isNotBlank()
            ) {
                Text("Ajouter")
            }
        }
    }
}