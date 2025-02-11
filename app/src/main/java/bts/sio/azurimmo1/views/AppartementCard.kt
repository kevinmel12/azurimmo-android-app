import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bts.sio.azurimmo1.model.Appartement
import bts.sio.azurimmo1.model.Batiment




@Composable

fun AppartementCard(appartement: Appartement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = appartement.description ?: "Pas de description", style = MaterialTheme.typography.bodyLarge)
            Text(text = appartement.numero ?: "Numéro inconnu", style = MaterialTheme.typography.bodyMedium)
            Text(text = appartement.pieces_principales ?: "Non spécifié", style = MaterialTheme.typography.bodyLarge)
            Text(text = appartement.surface ?: "Surface inconnue", style = MaterialTheme.typography.bodyLarge)
            Text(text = appartement.batiment?.adresse ?: "Adresse inconnue", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
