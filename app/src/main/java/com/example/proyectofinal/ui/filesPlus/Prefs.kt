package com.example.proyectofinal.ui.filesPlus

import android.content.Context

class Prefs(val context: Context) {
    //nombre de archivo
    val SHARED_NAME = "InventarioGuardado"
    var id = 0

    //se abre el archivo como preferencia
    val storage = context.getSharedPreferences(SHARED_NAME,0)

    fun verificarPref():Boolean{
        return storage.contains(id.toString())
    }

    fun saveUUID(uuid:String){
        storage.edit().putString(id.toString(),uuid).apply()
    }

    fun saveInfoCosa(uuid: String, cosa:String,valor:Int){
        val info = setOf<String>(cosa,valor.toString())
        storage.edit().putStringSet(uuid, info).apply()
    }

    fun getUUID():String{
        //!! Para evitar error, ya que detecta que puede ser nullo, pero cómo no debería se pone los signos
        return storage.getString(id.toString(),"")!!
    }

    fun getInfoCosa(uuid: String):Set<String>{
        return storage.getStringSet(uuid,null)!!
    }

    fun resetData(){
        storage.edit().clear().apply()
    }

}