import bts.sio.azurimmo.model.Appartement
import retrofit2.http.GET
import bts.sio.azurimmo.model.Batiment
import bts.sio.azurimmo.model.Contrat
import bts.sio.azurimmo.model.Garant
import bts.sio.azurimmo.model.Intervention
import bts.sio.azurimmo.model.Locataire
import bts.sio.azurimmo.model.Paiement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    // BATIMENTS
    @GET("api/batiments/")
    suspend fun getBatiments(): List<Batiment>

    @GET("api/batiments/{id}")
    suspend fun getBatimentById(@Path("id") id: Long): Batiment

    @POST("api/batiments/")
    suspend fun addBatiment(@Body batiment: Batiment): Response<Batiment>

    @PUT("api/batiments/{id}")
    suspend fun updateBatiment(@Path("id") id: Long, @Body batiment: Batiment): Response<Batiment>

    @DELETE("api/batiments/{id}")
    suspend fun deleteBatiment(@Path("id") id: Long): Response<Unit>

    // APPARTEMENTS
    @GET("api/appartements/")
    suspend fun getAppartements(): List<Appartement>

    @GET("/api/appartements/batiment/{batimentId}")
    suspend fun getAppartementsByBatimentId(@Path("batimentId") batimentId: Int): List<Appartement>

    @GET("api/appartements/{id}")
    suspend fun getAppartementById(@Path("id") id: Long): Appartement

    @POST("api/appartements/")
    suspend fun addAppartement(@Body appartement: Appartement): Response<Appartement>

    @PUT("api/appartements/{id}")
    suspend fun updateAppartement(@Path("id") id: Long, @Body appartement: Appartement): Response<Appartement>

    @DELETE("api/appartements/{id}")
    suspend fun deleteAppartement(@Path("id") id: Long): Response<Unit>

    // CONTRATS
    @GET("api/contrats/")
    suspend fun getContrats(): List<Contrat>

    @GET("api/contrats/appartement/{appartementId}")
    suspend fun getContratsByAppartementId(@Path("appartementId") appartementId: Int): List<Contrat>

    @GET("api/contrats/{id}")
    suspend fun getContratById(@Path("id") id: Long): Contrat

    @POST("api/contrats/")
    suspend fun addContrat(@Body contrat: Contrat): Response<Contrat>

    @PUT("api/contrats/{id}")
    suspend fun updateContrat(@Path("id") id: Long, @Body contrat: Contrat): Response<Contrat>

    @DELETE("api/contrats/{id}")
    suspend fun deleteContrat(@Path("id") id: Long): Response<Unit>

    // LOCATAIRES (CORRIGÉ: Long au lieu de Int)
    @GET("api/locataires/")
    suspend fun getLocataires(): List<Locataire>

    @GET("api/locataires/{id}")
    suspend fun getLocataireById(@Path("id") id: Long): Locataire

    @POST("api/locataires/")
    suspend fun addLocataire(@Body locataire: Locataire): Response<Locataire>

    @PUT("api/locataires/{id}")
    suspend fun updateLocataire(@Path("id") id: Long, @Body locataire: Locataire): Response<Locataire>

    @DELETE("api/locataires/{id}")
    suspend fun deleteLocataire(@Path("id") id: Long): Response<Unit>

    // AUTRES (inchangé)
    @GET("api/interventions/")
    suspend fun getInterventions(): List<Intervention>

    @GET("api/garants/")
    suspend fun getGarants(): List<Garant>

    @GET("api/paiements/")
    suspend fun getPaiements(): List<Paiement>
}