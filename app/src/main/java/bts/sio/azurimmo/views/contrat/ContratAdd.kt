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
                    // ✅ Vérifier que tous les champs sont remplis
                    if (dateEntree.isNotBlank() && dateSortie.isNotBlank() &&
                        montantLoyer.isNotBlank() && montantCharges.isNotBlank() &&
                        statut.isNotBlank()) {

                        println("🔍 Android - Création contrat pour appartement: $appartementId")

                        try {
                            // ✅ CORRIGÉ: Parser les dates comme java.sql.Date (comme le backend attend)
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                            val parsedDateEntree: java.sql.Date = try {
                                val utilDate = dateFormat.parse(dateEntree)
                                java.sql.Date(utilDate!!.time) // ✅ Conversion java.util.Date -> java.sql.Date
                            } catch (e: Exception) {
                                println("❌ Erreur parsing date entrée: ${e.message}")
                                java.sql.Date(System.currentTimeMillis())
                            }

                            val parsedDateSortie: java.sql.Date = try {
                                val utilDate = dateFormat.parse(dateSortie)
                                java.sql.Date(utilDate!!.time) // ✅ Conversion java.util.Date -> java.sql.Date
                            } catch (e: Exception) {
                                println("❌ Erreur parsing date sortie: ${e.message}")
                                java.sql.Date(System.currentTimeMillis())
                            }

                            // ✅ Conversions numériques sécurisées
                            val loyer = montantLoyer.toDoubleOrNull() ?: 0.0
                            val charges = montantCharges.toDoubleOrNull() ?: 0.0

                            // ✅ CORRIGÉ: Créer l'appartement avec Long ID nullable
                            val appartementLien = Appartement(
                                id = appartementId.toLong(), // ✅ Conversion Int -> Long
                                numero = 0, // ✅ CORRIGÉ: Int au lieu de String
                                description = "temp",
                                surface = 0f,
                                nbPieces = 0, // ✅ CORRIGÉ: nbPieces au lieu de nbrePieces
                                batiment = Batiment(id = 0L, adresse = "temp", ville = "temp") // ✅ 0L temporaire
                            )

                            // ✅ CORRIGÉ: Créer le contrat avec les types exacts du backend
                            val nouveauContrat = Contrat(
                                id = null, // ✅ CORRIGÉ: null pour nouveau contrat
                                dateEntree = parsedDateEntree, // ✅ java.sql.Date
                                dateSortie = parsedDateSortie, // ✅ java.sql.Date
                                montantLoyer = loyer,
                                montantCharges = charges,
                                statut = statut,
                                appartement = appartementLien,
                                locataire = null
                            )

                            println("📄 Android - Contrat créé: loyer=${nouveauContrat.montantLoyer}€, appartement=${nouveauContrat.appartement?.id}")

                            // ✅ Envoyer au ViewModel
                            viewModel.addContrat(nouveauContrat)

                            // ✅ Fermer l'écran
                            onAddContrat()

                        } catch (e: Exception) {
                            println("❌ Android - Erreur critique création contrat: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = dateEntree.isNotBlank() && dateSortie.isNotBlank() &&
                        montantLoyer.isNotBlank() && montantCharges.isNotBlank() &&
                        statut.isNotBlank()
            ) {
                Text("Ajouter")
            }
        }
    }
}