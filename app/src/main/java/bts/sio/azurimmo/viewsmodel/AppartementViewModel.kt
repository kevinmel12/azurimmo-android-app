import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import bts.sio.azurimmo.model.Appartement
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import bts.sio.azurimmo.api.RetrofitInstance
import bts.sio.azurimmo.model.Batiment
import kotlinx.coroutines.launch

class AppartementViewModel : ViewModel() {

    // Liste mutable des appartements
    private val _appartements = mutableStateOf<List<Appartement>>(emptyList())
    val appartements: State<List<Appartement>> = _appartements

    private val _appartement = mutableStateOf<Appartement?>(null)
    val appartement: State<Appartement?> = _appartement

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        // Chargement des données initiales
        getAppartements()
    }

    private fun getAppartements() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getAppartements()
                _appartements.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAppartementById(appartementId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getAppartementById(appartementId.toLong())
                _appartement.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement de l'appartement : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ CRITIQUE: Le backend attend INT pour batimentId (pas Long)
    fun getAppartementsByBatimentId(batimentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _appartements.value = emptyList() // ✅ CRITIQUE: vider d'abord pour éviter les anciens résultats !
            _errorMessage.value = null // Reset erreur

            try {
                println("🔍 Android - Recherche appartements pour bâtiment: $batimentId")
                // ✅ CORRIGÉ: Passer directement l'Int (le backend attend int)
                val response = RetrofitInstance.api.getAppartementsByBatimentId(batimentId)

                println("📊 Android - Nombre d'appartements reçus: ${response.size}")
                response.forEach { appartement ->
                    println("🏠 Android - Appartement ${appartement.numero} - Bâtiment ID: ${appartement.batiment?.id}")
                }

                if (response.isNotEmpty()) {
                    _appartements.value = response
                    println("✅ Android - Appartements chargés avec succès")
                } else {
                    println("⚠️ Android - Aucun appartement trouvé pour le bâtiment $batimentId")
                    _appartements.value = emptyList()
                }
            } catch (e: Exception) {
                println("❌ Android - Erreur lors du chargement des appartements: ${e.message}")
                _errorMessage.value = "Erreur lors du chargement des appartements : ${e.message}"
                _appartements.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addAppartement(appartement: Appartement) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.addAppartement(appartement)
                if (response.isSuccessful) {
                    getAppartements() // Recharge la liste
                } else {
                    _errorMessage.value = "Erreur lors de l'ajout de l'appartement : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAppartement(appartement: Appartement) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val appartementId = appartement.id?.toLong() ?: 0L
                val response = RetrofitInstance.api.updateAppartement(appartementId, appartement)
                if (response.isSuccessful) {
                    // Recharge les appartements du même bâtiment
                    appartement.batiment?.let { batiment ->
                        getAppartementsByBatimentId(batiment.id.toInt())
                    }
                } else {
                    _errorMessage.value = "Erreur lors de la modification de l'appartement : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAppartement(appartementId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.deleteAppartement(appartementId.toLong())
                if (response.isSuccessful) {
                    // Recharge les appartements du même bâtiment
                    val currentAppartement = _appartement.value
                    currentAppartement?.batiment?.let { batiment ->
                        getAppartementsByBatimentId(batiment.id.toInt())
                    }
                } else {
                    _errorMessage.value = "Erreur lors de la suppression de l'appartement : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}