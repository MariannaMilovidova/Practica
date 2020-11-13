package com.example.practica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Weather{
    @SerializedName("weather")
    @Expose var weather = ArrayList<weatherSource>()
    @SerializedName("base")
    @Expose var base: String? = null
    @SerializedName("main")
    @Expose var main: mainSource? = null
}

class weatherSource {
    @SerializedName("id")
    @Expose var id: Int? = null
    @SerializedName("main")
    @Expose var main: String? = null
    @SerializedName("description")
    @Expose var description: String? = null
    @SerializedName("icon")
    @Expose var icon: String? = null
}

class mainSource {
    @SerializedName("temp")
    @Expose var temp: String? = null
    @SerializedName("feels_like")
    @Expose var feels_like: String? = null
    @SerializedName("temp_min")
    @Expose var temp_min: String? = null
    @SerializedName("temp_max")
    @Expose var temp_max: String? = null
    @SerializedName("pressure")
    @Expose var pressure: String? = null
    @SerializedName("humidity")
    @Expose var humidity: String? = null
}
