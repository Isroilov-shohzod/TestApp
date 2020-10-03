package tj.isroilov.testapp.model.network

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import tj.isroilov.testapp.model.Configs

interface AppNetworkAPI {


    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("flats")
    fun getFlats(
        @Query("agent") id: Int
    ): Call<JsonObject>


    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("rooms")
    fun getRooms(
        @Query("agent") id: Int
    ): Call<JsonObject>

    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("country")
    fun getCountry(
        @Query("agent") id: Int
    ): Call<JsonObject>

    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("commercial")
    fun getCommercial(
        @Query("agent") id: Int
    ): Call<JsonObject>

    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("new")
    fun getNew(
        @Query("agent") id: Int
    ): Call<JsonObject>

    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("rent")
    fun getRent(
        @Query("agent") id: Int
    ): Call<JsonObject>
    @Headers(
        "Accept: application/json",
        "Authorization: ${Configs.API_KEY}"
    )
    @GET("foreign")
    fun getForeign(
        @Query("agent") id: Int
    ): Call<JsonObject>

    companion object {
        operator fun invoke(): AppNetworkAPI {

            val okHttpClient = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Configs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppNetworkAPI::class.java)
        }
    }

}
