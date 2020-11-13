package com.example.practica

import ApiInterface.Interface
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.*
import androidx.core.view.get
import com.example.practica.model.Weather
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var KEY = BuildConfig.API_KEY
class Fragment_main : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var anim:AnimationDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //читать сшаред преф селектед айтем
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        var leng = (activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            ?.getInt("count", 0))// ну тут мы читаем count

        fragcity.text = (activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getString("selectedItem", "London")).toString()

        sendNetworkRequest()
        (clouds.background as AnimationDrawable).start()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun  sendNetworkRequest() {
        if (fragcity.text != "Город не выбран"&&fragcity.text != "") {
            val builder = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
            val retrofit = builder.build()
            val apiInterface: Interface = retrofit.create<Interface>(Interface::class.java)
            val call: retrofit2.Call<Weather> =
                apiInterface.getFile(fragcity.text.toString(), KEY)
            call.enqueue(object : Callback<Weather> {
                override fun onFailure(call: retrofit2.Call<Weather>, t: Throwable) {
                    Log.i("LOL", t.message.toString())
                }

                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    val statusResponse = response.body()!!
                    var i = 0;
                    var bol = true
                    while (bol) {
                        try {
                            i++
                            activity?.runOnUiThread {
                                temp.text = statusResponse?.main?.temp?.toString()
                                temp.text = (temp.text.toString().toDouble() - 273.15).toString()
                                felstemp.text = statusResponse.main?.feels_like
                                felstemp.text = (felstemp.text.toString().toDouble() - 273.15).toString()
                                val pressure = statusResponse.main?.pressure
                                davl.text = pressure.toString()
                                val humidity = statusResponse.main?.humidity
                                vlag.text = humidity.toString()
                                val weather = statusResponse.weather[0]?.main



                                Log.i("LOL", statusResponse?.main?.temp.toString())

                                if (temp.text.toString().toDouble() > 0){
                                    Sovet.text = "Погода странная, определитесь сами"
                                }
                                if (temp.text.toString().toDouble() > 10){
                                    Sovet.text = "Прохладно, оденьтесь теплее"
                                }
                                if (temp.text.toString().toDouble() > 20){
                                    Sovet.text = "На улице жарко, не забудьте взять водичку"
                                }
                                if (temp.text.toString().toDouble() < 10){
                                    Sovet.text = "На улице холодновато, рекомендуем надеть ветровку"
                                }
                                if (temp.text.toString().toDouble() < 0){
                                    Sovet.text = "Меньще нуля, одевайтесь теплее"
                                }
                                if (temp.text.toString().toDouble() < -10){
                                    Sovet.text = "На улице настоящая зима, всегда носите шапку и теплую куртку"
                                }
                                if (weather.toString() == "Rain") {
                                    weatherPNG.setImageDrawable(resources.getDrawable(R.drawable.rain))
                                    pain.text = "Дождь"
                                }else
                                if (weather.toString() == "Clear") {
                                    weatherPNG.setImageDrawable(resources.getDrawable(R.drawable.sun))
                                    pain.text = "Ясно"
                                }else
                                if (weather.toString() == "Snow") {
                                    weatherPNG.setImageDrawable(resources.getDrawable(R.drawable.snow))
                                    pain.text = "Снег"
                                }else
                                if (weather.toString() == "Clouds") {
                                    weatherPNG.setImageDrawable(resources.getDrawable(R.drawable.cloud))
                                    pain.text = "Облачно"
                                }
                                else {
                                    weatherPNG.setImageDrawable(resources.getDrawable(R.drawable.how))
                                    pain.text = weather.toString()
                                }
                                bol = false

                            }
                        } catch (e: Exception) {
                            Log.i("LOL", e.message.toString())
                            bol = false
                        }
                    }
                }
            })
        }
        else {
            fragcity.text = "Город не выбран"
        }
    }
}