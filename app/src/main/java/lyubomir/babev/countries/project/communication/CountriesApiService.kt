package lyubomir.babev.countries.project.communication

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import lyubomir.babev.countries.project.models.CountryModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://excitel-countries.azurewebsites.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CountriesApiService {
    @GET("countries")
    suspend fun getCountries(): List<CountryModel>
}

object CountriesApi {
    val retrofitService: CountriesApiService by lazy { retrofit.create(CountriesApiService::class.java) }
}