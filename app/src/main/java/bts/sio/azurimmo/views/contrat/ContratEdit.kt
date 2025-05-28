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
import bts.sio.azurimmo.model.Contrat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ContratEdit(
    contratId: Int,
    onContratUpdate: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: ContratViewModel = viewModel()
    val contrat = viewModel.contrat.value
    var dateEntree by remember { mutableStateOf("") }
    var dateSortie by remember { mutableStateOf("") }
    var montantLoyer by remember { mutableStateOf("") }
    var montantCharges by remember { mutableStateOf("") }
    var statut by remember { mutableStateOf("") }

    // Charger le contrat à modifier
    LaunchedEffect(contratId) {
        viewModel.getContratById(contratId)
    }

    // Peupler les champs une fois le contrat chargé
    LaunchedEffect(contrat) {
        contrat?.let {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateEntree = dateFormat.format(it.dateEntree)
            dateSortie = dateFormat.format(it.dateSortie)
            montantLoyer = it.montantLoyer.toString()
            montantCharges = it.montantCharges.toString()
            statut = it.statut
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
                text = "Modifier le Contrat",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(
            value = dateEntree,
            onValueChange = { dateEntree = it },
            label = { Text("Date d'entrée (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dateSortie,
            onValueChange = { dateSortie = it },
            label = { Text("Date de sortie (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = montantLoyer,
            onValueChange = { montantLoyer = it },
            label = { Text("Montant du loyer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = montantCharges,
            onValueChange = { montantCharges = it },
            label = { Text("Montant des charges") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = statut,
            onValueChange = { statut = it },
            label = { Text("Statut") },
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
                    if (dateEntree.isNotBlank() && dateSortie.isNotBlank() &&
                        montantLoyer.isNotBlank() && montantCharges.isNotBlank() &&
                        statut.isNotBlank() && contrat != null) {

                        // Conversion des dates
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val parsedDateEntree = try {
                            dateFormat.parse(dateEntree)
                        } catch (e: Exception) {
                            contrat.dateEntree
                        }
                        val parsedDateSortie = try {
                            dateFormat.parse(dateSortie)
                        } catch (e: Exception) {
                            contrat.dateSortie
                        }

                        val updatedContrat = Contrat(
                            id = contrat.id,
                            dateEntree = parsedDateEntree?.let { java.sql.Date(it.time) } ?: contrat.dateEntree,
                            dateSortie = parsedDateSortie?.let { java.sql.Date(it.time) } ?: contrat.dateSortie,
                            montantLoyer = montantLoyer.toDoubleOrNull() ?: contrat.montantLoyer,
                            montantCharges = montantCharges.toDoubleOrNull() ?: contrat.montantCharges,
                            statut = statut,
                            appartement = contrat.appartement,
                            locataire = contrat.locataire
                        )
                        viewModel.updateContrat(updatedContrat)
                        onContratUpdate()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = dateEntree.isNotBlank() && dateSortie.isNotBlank() &&
                        montantLoyer.isNotBlank() && montantCharges.isNotBlank() &&
                        statut.isNotBlank()
            ) {
                Text("Modifier")
            }
        }
    }
}