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
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/batiments/")
    suspend fun getBatiments(): List<Batiment>

    @GET("api/appartements/")
    suspend fun getAppartements(): List<Appartement>

    @GET("api/contrats/")
    suspend fun getContrats(): List<Contrat>

    @GET("api/interventions/")
    suspend fun getInterventions(): List<Intervention>

    @GET("api/garants/")
    suspend fun getGarants(): List<Garant>

    @GET("api/paiements/")
    suspend fun getPaiements(): List<Paiement>

    @GET("api/locataires/")
    suspend fun getLocataires(): List<Locataire>

    @GET("api/batiments/{id}")
    suspend fun getBatimentById(@Path("id") id: Int): Batiment

    @GET("/api/appartements/batiment/{batimentId}")
    suspend fun getAppartementsByBatimentId(@Path("batimentId") batimentId: Int): List<Appartement>

    @POST("api/batiments/")
    suspend fun addBatiment(@Body batiment: Batiment): Response<Batiment>

    @POST("api/appartements/")
    suspend fun addAppartement(@Body appartement: Appartement): Response<Appartement>

    @POST("api/contrats/")
    suspend fun addContrat(@Body contrat: Contrat): Response<Contrat>

    @GET("api/contrats/appartement/{appartementId}")
    suspend fun getContratsByAppartementId(@Path("appartementId") appartementId: Int): List<Contrat>
}