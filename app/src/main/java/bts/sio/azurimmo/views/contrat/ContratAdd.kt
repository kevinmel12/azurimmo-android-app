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
import java.util.Date

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
            label = { Text("Date d'entrée") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = dateSortie,
            onValueChange = { dateSortie = it },
            label = { Text("Date de sortie") },
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
        Button(
            onClick = {
                if (dateEntree.isNotBlank() && dateSortie.isNotBlank() && montantLoyer.isNotBlank() && montantCharges.isNotBlank() && statut.isNotBlank()) {
                    val contrat = Contrat(
                        id = 0,
                        dateEntree = Date(), // Pour simplifier, on met la date actuelle
                        dateSortie = Date(),
                        montantLoyer = montantLoyer.toDouble(),
                        montantCharges = montantCharges.toDouble(),
                        statut = statut
                    )
                    viewModel.addContrat(contrat)
                    onAddContrat() // On notifie le parent que l'ajout est fait
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = dateEntree.isNotBlank() && dateSortie.isNotBlank() && montantLoyer.isNotBlank() && montantCharges.isNotBlank() && statut.isNotBlank()
        ) {
            Text("Ajouter le contrat")
        }
    }
}