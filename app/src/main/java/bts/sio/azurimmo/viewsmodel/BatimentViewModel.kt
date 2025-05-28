import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import bts.sio.azurimmo.model.Batiment
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import bts.sio.azurimmo.api.RetrofitInstance
import kotlinx.coroutines.launch

class BatimentViewModel : ViewModel() {

    // Liste mutable des bâtiments
    private val _batiments = mutableStateOf<List<Batiment>>(emptyList())
    val batiments: State<List<Batiment>> = _batiments

    private val _batiment = mutableStateOf<Batiment?>(null)
    val batiment: State<Batiment?> = _batiment

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        // Chargement des données initiales
        getBatiments()
    }

    fun getBatiments() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getBatiments()
                _batiments.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getBatiment(batimentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getBatimentById(batimentId.toLong())
                _batiment.value = response
                println("Bâtiment chargé : $response")
            } catch (e: Exception) {
                println("Erreur lors du chargement du bâtiment : ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addBatiment(batiment: Batiment) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.addBatiment(batiment)
                if (response.isSuccessful) {
                    getBatiments() // Recharge la liste
                } else {
                    _errorMessage.value = "Erreur lors de l'ajout du bâtiment : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateBatiment(batiment: Batiment) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.updateBatiment(batiment.id, batiment)
                if (response.isSuccessful) {
                    getBatiments() // Recharge la liste
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Erreur lors de la modification du bâtiment : ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteBatiment(batimentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("🗑️ Android - Tentative de suppression du bâtiment: $batimentId")
                val response = RetrofitInstance.api.deleteBatiment(batimentId.toLong())

                if (response.isSuccessful) {
                    println("✅ Android - Bâtiment supprimé avec succès")
                    getBatiments() // Recharge la liste
                    _errorMessage.value = null // Reset l'erreur
                } else {
                    println("❌ Android - Erreur suppression bâtiment: ${response.code()} - ${response.message()}")

                    when (response.code()) {
                        409, 400 -> {
                            _errorMessage.value = "❌ Impossible de supprimer ce bâtiment : il contient encore des appartements. Supprimez d'abord tous les appartements."
                        }
                        500 -> {
                            _errorMessage.value = "❌ Erreur serveur : Le bâtiment est peut-être lié à des données existantes."
                        }
                        else -> {
                            _errorMessage.value = "❌ Erreur lors de la suppression du bâtiment : ${response.message()}"
                        }
                    }
                }
            } catch (e: Exception) {
                println("❌ Android - Exception suppression bâtiment: ${e.message}")

                if (e.message?.contains("constraint", ignoreCase = true) == true ||
                    e.message?.contains("foreign key", ignoreCase = true) == true) {
                    _errorMessage.value = "❌ Ce bâtiment ne peut pas être supprimé car il contient des appartements. Supprimez d'abord tous les appartements."
                } else {
                    _errorMessage.value = "❌ Erreur : ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}