import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import bts.sio.azurimmo.api.RetrofitInstance
import bts.sio.azurimmo.model.Paiement
import kotlinx.coroutines.launch

class PaiementViewModel : ViewModel() {

    // Liste mutable des interventions
    private val _paiements = mutableStateOf<List<Paiement>>(emptyList())
    val paiements: State<List<Paiement>> = _paiements

    // État de chargement
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Message d'erreur
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

}
