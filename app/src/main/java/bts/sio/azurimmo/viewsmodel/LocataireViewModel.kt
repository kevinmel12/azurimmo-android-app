import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import bts.sio.azurimmo.api.RetrofitInstance
import bts.sio.azurimmo.model.Locataire
import kotlinx.coroutines.launch

class LocataireViewModel : ViewModel() {

    // Liste mutable des locataires
    private val _locataires = mutableStateOf<List<Locataire>>(emptyList())
    val locataires: State<List<Locataire>> = _locataires

    private val _locataire = mutableStateOf<Locataire?>(null)
    val locataire: State<Locataire?> = _locataire

    // État de chargement
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Message d'erreur
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        // Charger les locataires au démarrage
        getLocataires()
    }

    fun getLocataires() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getLocataires()
                _locataires.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getLocataireById(locataireId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getLocataireById(locataireId.toLong())
                _locataire.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement du locataire : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addLocataire(locataire: Locataire) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.addLocataire(locataire)
                if (response.isSuccessful) {
                    getLocataires() // Recharge la liste
                } else {
                    _errorMessage.value = "Erreur lors de l'ajout du locataire : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateLocataire(locataire: Locataire) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // ✅ CORRIGÉ: Gérer l'ID nullable
                val locataireId = locataire.id
                if (locataireId != null) {
                    val response = RetrofitInstance.api.updateLocataire(locataireId, locataire)
                    if (response.isSuccessful) {
                        getLocataires() // Recharge la liste
                    } else {
                        _errorMessage.value = "Erreur lors de la modification du locataire : ${response.message()}"
                    }
                } else {
                    _errorMessage.value = "Erreur : ID de locataire manquant"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteLocataire(locataireId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.deleteLocataire(locataireId.toLong())
                if (response.isSuccessful) {
                    getLocataires() // Recharge la liste
                } else {
                    _errorMessage.value = "Erreur lors de la suppression du locataire : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}