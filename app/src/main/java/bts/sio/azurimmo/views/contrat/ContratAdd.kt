package bts.sio.azurimmo.views.contrat

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
import bts.sio.azurimmo.model.Appartement
import bts.sio.azurimmo.model.Batiment
import bts.sio.azurimmo.model.Contrat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ContratAdd(onAddContrat: () -> Unit, appartementId: Int, onBackClick: () -> Unit) {
    val viewModel: ContratViewModel = viewModel()
    var dateEntree by remember { mutableStateOf("") }
    var dateSortie by remember { mutableStateOf("") }
    var montantLoyer by remember { mutableStateOf("") }
    var montantCharges by remember { mutableStateOf("") }
    var statut by remember { mutableStateOf("") }

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
                text = "Ajouter un Contrat",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(
            value = dateEntree,
            onValueChange = { dateEntree = it },
            label = { Text("Date d'entrée (DD/MM/YYYY)") },
            placeholder = { Text("10/02/2025") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dateSortie,
            onValueChange = { dateSortie = it },
            label = { Text("Date de sortie (DD/MM/YYYY)") },
            placeholder = { Text("10/02/2026") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = montantLoyer,
            onValueChange = { montantLoyer = it },
            label = { Text("Montant du loyer") },
            placeholder = { Text("800") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = montantCharges,
            onValueChange = { montantCharges = it },
            label = { Text("Montant des charges") },
            placeholder = { Text("100") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = statut,
            onValueChange = { statut = it },
            label = { Text("Statut") },
            placeholder = { Text("Actif") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (dateEntree.isNotBlank() && dateSortie.isNotBlank() &&
                    montantLoyer.isNotBlank() && montantCharges.isNotBlank() &&
                    statut.isNotBlank()) {

                    println("Android - Création contrat pour appartement: $appartementId")

                    try {
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                        val parsedDateEntree: java.sql.Date = try {
                            val utilDate = dateFormat.parse(dateEntree)
                            java.sql.Date(utilDate!!.time)
                        } catch (e: Exception) {
                            println("Erreur parsing date entrée: ${e.message}")
                            java.sql.Date(System.currentTimeMillis())
                        }

                        val parsedDateSortie: java.sql.Date = try {
                            val utilDate = dateFormat.parse(dateSortie)
                            java.sql.Date(utilDate!!.time)
                        } catch (e: Exception) {
                            println("Erreur parsing date sortie: ${e.message}")
                            java.sql.Date(System.currentTimeMillis())
                        }

                        val loyer = montantLoyer.toDoubleOrNull() ?: 0.0
                        val charges = montantCharges.toDoubleOrNull() ?: 0.0

                        val appartementLien = Appartement(
                            id = appartementId.toLong(),
                            numero = 0,
                            description = "temp",
                            surface = 0f,
                            nbPieces = 0,
                            batiment = Batiment(id = 0L, adresse = "temp", ville = "temp")
                        )

                        val nouveauContrat = Contrat(
                            id = 0L,
                            dateEntree = parsedDateEntree,
                            dateSortie = parsedDateSortie,
                            montantLoyer = loyer,
                            montantCharges = charges,
                            statut = statut,
                            appartement = appartementLien,
                            locataire = null
                        )

                        println("📄 Android - Contrat créé: loyer=${nouveauContrat.montantLoyer}€, appartement=${nouveauContrat.appartement?.id}")

                        viewModel.addContrat(nouveauContrat)

                        onAddContrat()

                    } catch (e: Exception) {
                        println("Android - Erreur critique création contrat: ${e.message}")
                        e.printStackTrace()
                    }
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = dateEntree.isNotBlank() && dateSortie.isNotBlank() &&
                    montantLoyer.isNotBlank() && montantCharges.isNotBlank() &&
                    statut.isNotBlank()
        ) {
            Text("Ajouter le contrat")
        }
    }
}