package bts.sio.azurimmo.views.appartement

import AppartementViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.model.Appartement
import bts.sio.azurimmo.model.Batiment


@Composable
fun AppartementAdd(onAddAppartement: (Appartement) -> Unit, batimentId: Int) {
    val viewModel: AppartementViewModel = viewModel()
    var description by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var nbrePieces by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("numero") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = nbrePieces,
            onValueChange = { nbrePieces = it },
            label = { Text("Nombre de pièces") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val batiment = Batiment(id = batimentId, adresse = "zouzou", ville = "labas") // seul l’id nous interesse ici
                val appartement = Appartement(
                    id = 0,
                    numero = numero,
                    description = description,
                    surface = 10.2f,
                    batiment = batiment,
                    nbrePieces = nbrePieces.toInt()
                )
                viewModel.addAppartement(appartement)
                onAddAppartement(appartement)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Ajouter l'appartement ")
        }
    }
}
