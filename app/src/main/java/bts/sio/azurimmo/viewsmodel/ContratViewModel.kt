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
        println("🔄 ContratViewModel - Initialisé sans chargement automatique")
    }

    private fun getContrats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("🔄 Android - Chargement de TOUS les contrats")
                val response = RetrofitInstance.api.getContrats()
                _contrats.value = response
                println("📊 Android - ${response.size} contrats chargés (tous)")
            } catch (e: Exception) {
                println("❌ Android - Erreur chargement tous contrats: ${e.message}")
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllContrats() {
        getContrats()
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

    // ✅ CRITIQUE: Le backend attend INT pour appartementId (pas Long)
    fun getContratsByAppartementId(appartementId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _contrats.value = emptyList() // ✅ CRITIQUE: vider d'abord pour éviter les anciens résultats !
            _errorMessage.value = null // Reset erreur

            try {
                println("🔍 Android - Recherche contrats pour appartement: $appartementId")
                // ✅ CORRIGÉ: Passer directement l'Int (pas de conversion en Long)
                val response = RetrofitInstance.api.getContratsByAppartementId(appartementId)

                println("📊 Android - Nombre de contrats reçus: ${response.size}")
                response.forEach { contrat ->
                    println("📄 Android - Contrat ${contrat.id} - Appartement ID: ${contrat.appartement?.id}")
                }

                if (response.isNotEmpty()) {
                    _contrats.value = response
                    println("✅ Android - Contrats chargés avec succès")
                } else {
                    println("⚠️ Android - Aucun contrat trouvé pour l'appartement $appartementId")
                    _contrats.value = emptyList()
                }
            } catch (e: Exception) {
                println("❌ Android - Erreur lors du chargement des contrats: ${e.message}")
                _errorMessage.value = "Erreur lors du chargement des contrats : ${e.message}"
                _contrats.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ CORRIGÉ: Recharger les contrats de l'appartement après ajout
    fun addContrat(contrat: Contrat) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("🔍 Android - Envoi contrat au backend: ${contrat.montantLoyer}€, appartement: ${contrat.appartement?.id}")
                val response = RetrofitInstance.api.addContrat(contrat)
                if (response.isSuccessful) {
                    println("✅ Android - Contrat ajouté avec succès")
                    // ✅ CORRIGER: Recharger les contrats de cet appartement spécifique !
                    contrat.appartement?.id?.let { appartementIdLong ->
                        // ✅ CORRECTION: Convertir Long -> Int pour l'API backend
                        getContratsByAppartementId(appartementIdLong.toInt())
                    } ?: getContrats() // Fallback: recharge tous les contrats
                } else {
                    println("❌ Android - Erreur ajout contrat: ${response.message()}")
                    _errorMessage.value = "Erreur lors de l'ajout du contrat : ${response.message()}"
                }
            } catch (e: Exception) {
                println("❌ Android - Exception ajout contrat: ${e.message}")
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
                val contratId = contrat.id
                if (contratId != null) {
                    val response = RetrofitInstance.api.updateContrat(contratId, contrat)
                    if (response.isSuccessful) {
                        // Recharge les contrats du même appartement si disponible
                        contrat.appartement?.id?.let { appartementIdLong ->
                            getContratsByAppartementId(appartementIdLong.toInt())
                        } ?: getContrats() // Sinon recharge tous les contrats
                    } else {
                        _errorMessage.value = "Erreur lors de la modification du contrat : ${response.message()}"
                    }
                } else {
                    _errorMessage.value = "Erreur : ID de contrat manquant"
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
                    currentContrat?.appartement?.id?.let { appartementIdLong ->
                        getContratsByAppartementId(appartementIdLong.toInt())
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