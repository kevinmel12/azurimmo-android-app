import bts.sio.azurimmo.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // ============ BATIMENTS ============
    @GET("api/batiments/")
    suspend fun getBatiments(): List<Batiment>

    @GET("api/batiments/{id}")
    suspend fun getBatimentById(@Path("id") id: Long): Batiment

    @POST("api/batiments/")
    suspend fun addBatiment(@Body batiment: Batiment): Response<Batiment>

    @PUT("api/batiments/{id}")
    suspend fun updateBatiment(@Path("id") id: Long, @Body batiment: Batiment): Response<Batiment>

    @DELETE("api/batiments/{id}")
    suspend fun deleteBatiment(@Path("id") id: Long): Response<Void>

    // ============ APPARTEMENTS ============
    @GET("api/appartements/")
    suspend fun getAppartements(): List<Appartement>

    @GET("api/appartements/{id}")
    suspend fun getAppartementById(@Path("id") id: Long): Appartement

    @GET("api/appartements/batiment/{batimentId}")
    suspend fun getAppartementsByBatimentId(@Path("batimentId") batimentId: Int): List<Appartement>

    @POST("api/appartements/")
    suspend fun addAppartement(@Body appartement: Appartement): Response<Appartement>

    @PUT("api/appartements/{id}")
    suspend fun updateAppartement(@Path("id") id: Long, @Body appartement: Appartement): Response<Appartement>

    @DELETE("api/appartements/{id}")
    suspend fun deleteAppartement(@Path("id") id: Long): Response<Void>

    // ============ CONTRATS ============
    @GET("api/contrats/")
    suspend fun getContrats(): List<Contrat>

    @GET("api/contrats/{id}")
    suspend fun getContratById(@Path("id") id: Long): Contrat

    @GET("api/contrats/appartement/{appartementId}")
    suspend fun getContratsByAppartementId(@Path("appartementId") appartementId: Int): List<Contrat>

    @POST("api/contrats/")
    suspend fun addContrat(@Body contrat: Contrat): Response<Contrat>

    @PUT("api/contrats/{id}")
    suspend fun updateContrat(@Path("id") id: Long, @Body contrat: Contrat): Response<Contrat>

    @DELETE("api/contrats/{id}")
    suspend fun deleteContrat(@Path("id") id: Long): Response<Void>

    // ============ LOCATAIRES ============
    @GET("api/locataires/")
    suspend fun getLocataires(): List<Locataire>

    @GET("api/locataires/{id}")
    suspend fun getLocataireById(@Path("id") id: Long): Locataire

    @POST("api/locataires/")
    suspend fun addLocataire(@Body locataire: Locataire): Response<Locataire>

    @PUT("api/locataires/{id}")
    suspend fun updateLocataire(@Path("id") id: Long, @Body locataire: Locataire): Response<Locataire>

    @DELETE("api/locataires/{id}")
    suspend fun deleteLocataire(@Path("id") id: Long): Response<Void>

    // ============ ASSOCIÉS ============
    @GET("api/associes/")
    suspend fun getAssocies(): List<Associe>

    @GET("api/associes/{id}")
    suspend fun getAssocieById(@Path("id") id: Long): Associe

    @POST("api/associes/")
    suspend fun addAssocie(@Body associe: Associe): Response<Associe>

    @PUT("api/associes/{id}")
    suspend fun updateAssocie(@Path("id") id: Long, @Body associe: Associe): Response<Associe>

    @DELETE("api/associes/{id}")
    suspend fun deleteAssocie(@Path("id") id: Long): Response<Void>
}