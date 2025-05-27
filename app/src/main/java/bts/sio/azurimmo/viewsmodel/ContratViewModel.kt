import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import bts.sio.azurimmo.api.RetrofitInstance
import bts.sio.azurimmo.model.Contrat
import kotlinx.coroutines.launch

class ContratViewModel : ViewModel() {
    // Liste mutable des contrats
    private val _contrats = mutableStateOf<List<Contrat>>(emptyList())
    val contrats: State<List<Contrat>> = _contrats

    private val _contrat = mutableStateOf<Contrat?>(null)
    val contrat: State<Contrat?> = _contrat

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        // Chargement des données initiales
        getContrats()
    }

    private fun getContrats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getContrats()
                _contrats.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getContratById(contratId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getContratById(contratId.toLong())
                _contrat.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement du contrat : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Récupérer les contrats par appartement
    fun getContratsByAppartementId(appartementId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getContratsByAppartementId(appartementId)
                _contrats.value = response
                println("Contrats chargés pour appartement $appartementId : $response")
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
                println("Erreur lors du chargement des contrats : ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addContrat(contrat: Contrat) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.addContrat(contrat)
                if (response.isSuccessful) {
                    getContrats() // Recharge la liste
                } else {
                    _errorMessage.value = "Erreur lors de l'ajout du contrat : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateContrat(contrat: Contrat) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.updateContrat(contrat.id.toLong(), contrat)
                if (response.isSuccessful) {
                    // Recharge les contrats du même appartement si disponible
                    contrat.appartement?.id?.let { appartementId ->
                        getContratsByAppartementId(appartementId)
                    } ?: getContrats() // Sinon recharge tous les contrats
                } else {
                    _errorMessage.value = "Erreur lors de la modification du contrat : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteContrat(contratId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.deleteContrat(contratId.toLong())
                if (response.isSuccessful) {
                    // Recharge les contrats du même appartement si disponible
                    val currentContrat = _contrat.value
                    currentContrat?.appartement?.id?.let { appartementId ->
                        getContratsByAppartementId(appartementId)
                    } ?: getContrats() // Sinon recharge tous les contrats
                } else {
                    _errorMessage.value = "Erreur lors de la suppression du contrat : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}