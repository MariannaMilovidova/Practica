package com.example.practica

import ApiInterface.Interface
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.get
import com.example.practica.model.Weather
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment_second : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        if (activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getBoolean("Veteran",false)==true){

        }
        else {

            val nameaArray = resources.getStringArray(R.array.CityNames)
            val builder = AlertDialog.Builder(this.context)
            val checkedArray= booleanArrayOf(
                false,false,false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false,false,false)

            builder.setTitle("Выберите поле из списка")
                .setMultiChoiceItems(nameaArray,checkedArray){
                        dialog, which, isChecked ->
                    checkedArray[which] = isChecked
                    val name = nameaArray[which]
                }
                .setPositiveButton("OK") { dialog, id ->
                    citiess(checkedArray,nameaArray)
                    favCities.isClickable = true
                }
                .setNeutralButton("Отмена") { dialog, id -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_second().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getBoolean("Veteran",false)==true){
            favCities.isClickable = true
        }
        else {
            favCities.isClickable = false
        }
        var count = 0
        count = count + activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getInt("count",1)!!.toInt()

        Log.i("LOL1",count.toString())
        var Citys = ArrayList<String>()
        var i = 0
        var sel_itemInt = 0
        val sel_itemStr = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getString("selectedItem","ошЫбкаа").toString()
        do{
            Citys.add(activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getString("City" + i.toString(),"")!!.toString())
            if (Citys[i]?.toString() == sel_itemStr)
                sel_itemInt = i
            i++
        }while (i < count )

        val arrayAdapter = activity?.let { ArrayAdapter (it, android.R.layout.simple_spinner_item, Citys) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        favCities.adapter = arrayAdapter

        favCities.setSelection(sel_itemInt)
        favCities.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.edit()?.putString("selectedItem", favCities.selectedItem.toString())
                    ?.apply()
                Log.i("LOL",activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getString("selectedItem","ошЫбкаа").toString())
            }
        }
    }
    fun citiess(checkedArray:BooleanArray, nameaArray: Array<String>){
        var count: Int = 0
        var check: Int = 0
        for(i in 0..checkedArray.size-1){
            if (checkedArray[i]){
                check++
            }
        }
        val editor = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.edit()
        val favCitiesInput = Array(check, {"a"})
        for (i in 0..checkedArray.size-1){
            if (checkedArray[i]){
                editor?.putString("City"+count.toString(),nameaArray[i])?.apply()
                favCitiesInput[count]=nameaArray[i]
                count++
                editor?.putBoolean("Veteran",true)?.apply()
            }
        }

        editor?.putInt("count", count)?.apply()

        //editor.putString("selectedCiyt",favCities.selectedItem.toString())

        Log.i("LOL",(activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getInt("count", 0)).toString())

        for(i in 0..count-1){
            Log.i("LOL",(activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getString("City"+i.toString(), "")).toString())
        }

        val arrayAdapter = activity?.let { ArrayAdapter (it, android.R.layout.simple_spinner_item, favCitiesInput) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        favCities.adapter = arrayAdapter
    }

}