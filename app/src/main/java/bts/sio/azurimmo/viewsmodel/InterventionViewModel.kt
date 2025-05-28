import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import bts.sio.azurimmo.api.RetrofitInstance
import bts.sio.azurimmo.model.Intervention
import kotlinx.coroutines.launch

class InterventionViewModel : ViewModel() {

    // Liste mutable des interventions
    private val _interventions = mutableStateOf<List<Intervention>>(emptyList())
    val interventions: State<List<Intervention>> = _interventions

    // État de chargement
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Message d'erreur
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


}
