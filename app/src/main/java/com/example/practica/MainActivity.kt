package com.example.practica

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_second.*

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

    }
    fun showDialogRadioButton(view: View) {
        val nameaArray = resources.getStringArray(R.array.CityNames)
        val builder = AlertDialog.Builder(view.context)
        val checkedArray= booleanArrayOf(
            false,false,false,false,false,false,false,false,false,false,
            false,false,false,false,false,false,false,false,false,false,
            false,false,false,false,false,false,false,false,false,false)

        builder.setTitle("Выберите поле из списка")
            .setMultiChoiceItems(nameaArray,checkedArray){
                    dialog, which, isChecked ->
                checkedArray[which] = isChecked
                val name = nameaArray[which]
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("OK") { dialog, id ->
                cities(checkedArray,nameaArray)
            }
            .setNeutralButton("Отмена") { dialog, id -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun cities(checkedArray:BooleanArray, nameaArray: Array<String>){
        var count: Int = 0
        var check: Int = 0
        for(i in 0..checkedArray.size-1){
            if (checkedArray[i]){
                check++
            }
        }
        val editor = prefs.edit()
        val favCitiesInput = Array(check, {"a"})
        for (i in 0..checkedArray.size-1){
            if (checkedArray[i]){
                editor.putString("City"+count.toString(),nameaArray[i]).apply()
                favCitiesInput[count]=nameaArray[i]
                count++
                favCities.isClickable = true
                editor?.putBoolean("Veteran",true)?.apply()
            }
        }

        editor.putInt("count", count).apply()

        //editor.putString("selectedCiyt",favCities.selectedItem.toString())

        Log.i("LOL",(prefs.getInt("count", 0)).toString())

        for(i in 0..count-1){
            Log.i("LOL",(prefs.getString("City"+i.toString(), "")).toString())
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            favCitiesInput
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        favCities.adapter = adapter;
    }
}