import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import bts.sio.azurimmo.model.Garant
import androidx.compose.foundation.lazy.items

@Composable
fun GarantList( viewModel: GarantViewModel = viewModel()) {
    val garants = viewModel.garants.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Erreur inconnue",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {
                LazyColumn {
                    items(garants ?: emptyList()) { garant ->
                        GarantCard(garant = garant)
                    }
                }
            }
        }
    }
}