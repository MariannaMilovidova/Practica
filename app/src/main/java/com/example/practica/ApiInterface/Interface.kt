package ApiInterface

import com.example.practica.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

//var city = "London"

interface Interface {
    @GET("weather")
    fun  getFile(@Query("q") city : String,
                 @Query("appid") id:String
    ): Call<Weather>
}