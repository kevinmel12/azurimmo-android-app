package bts.sio.azurimmo.views.locataire

import LocataireViewModel
import ContratViewModel
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
import bts.sio.azurimmo.model.Contrat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LocataireAdd(
    onAddLocataire: () -> Unit,
    onBackClick: () -> Unit,
    contratId: Int? = null // ✅ PARAMÈTRE OPTIONNEL pour assignation automatique
) {
    val locataireViewModel: LocataireViewModel = viewModel()
    val contratViewModel: ContratViewModel = viewModel()
    val contrat = contratViewModel.contrat.value

    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var lieuN by remember { mutableStateOf("") }
    var dateN by remember { mutableStateOf("") }

    // ✅ Si contratId fourni, charger le contrat pour assignation
    LaunchedEffect(contratId) {
        if (contratId != null) {
            println("🔍 Android - Chargement du contrat $contratId pour assignation")
            contratViewModel.getContratById(contratId)
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
                text = if (contratId != null) "Ajouter et Assigner un Locataire" else "Ajouter un Locataire",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // ✅ Afficher les informations du contrat si assignation
        if (contratId != null && contrat != null) {
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
                        text = "🔗 Sera assigné au contrat :",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Contrat #${contrat.id}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Loyer : ${contrat.montantLoyer}€",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
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

            // Bouton Ajouter (avec assignation si contrat)
            Button(
                onClick = {
                    if (nom.isNotBlank() && prenom.isNotBlank() &&
                        dateN.isNotBlank() && lieuN.isNotBlank()) {

                        println("🔍 Android - Création locataire: $prenom $nom")

                        try {
                            // Conversion de date
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val parsedDate = try {
                                dateFormat.parse(dateN)
                            } catch (e: Exception) {
                                println("❌ Erreur parsing date: ${e.message}")
                                Date()
                            }

                            // Créer le locataire
                            val nouveauLocataire = Locataire(
                                id = 0,
                                nom = nom,
                                prenom = prenom,
                                lieuN = lieuN,
                                dateN = parsedDate?.let { java.sql.Date(it.time) } ?: java.sql.Date(Date().time)
                            )

                            println("👤 Android - Locataire créé: ${nouveauLocataire.prenom} ${nouveauLocataire.nom}")

                            // ✅ LOGIQUE D'ASSIGNATION AUTOMATIQUE (version simplifiée)
                            if (contratId != null && contrat != null) {
                                println("🔗 Android - Assignation automatique au contrat $contratId")

                                // Créer un contrat modifié avec le locataire assigné
                                val contratModifie = Contrat(
                                    id = contrat.id,
                                    dateEntree = contrat.dateEntree,
                                    dateSortie = contrat.dateSortie,
                                    montantLoyer = contrat.montantLoyer,
                                    montantCharges = contrat.montantCharges,
                                    statut = contrat.statut,
                                    appartement = contrat.appartement,
                                    locataire = nouveauLocataire // ✅ ASSIGNATION !
                                )

                                // D'abord créer le locataire
                                locataireViewModel.addLocataire(nouveauLocataire)

                                // Puis mettre à jour le contrat avec le locataire assigné
                                contratViewModel.updateContrat(contratModifie)
                                println("✅ Android - Locataire créé et assigné au contrat")
                            } else {
                                // Mode normal : juste créer le locataire
                                locataireViewModel.addLocataire(nouveauLocataire)
                            }

                            onAddLocataire()

                        } catch (e: Exception) {
                            println("Android - Erreur création locataire: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = nom.isNotBlank() && prenom.isNotBlank() &&
                        dateN.isNotBlank() && lieuN.isNotBlank()
            ) {
                Text(if (contratId != null) "Ajouter et Assigner" else "Ajouter")
            }
        }
    }
}