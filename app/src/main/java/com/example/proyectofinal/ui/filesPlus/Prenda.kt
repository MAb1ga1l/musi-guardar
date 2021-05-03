package com.example.proyectofinal.ui.filesPlus

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Prenda (): Parcelable {
    //Especificació de la información que es necesaria para guardar una prenda
    var tipoPrenda: String = ""
    var idPrenda: String = UUID.randomUUID().toString().substring(0,6)
    @SuppressLint("SimpleDateFormat")
    private var formatter = SimpleDateFormat("dd-MMMM-yyyy")
    var fechaDeCreacion : String = formatter.format(Date())
    var ultimaFechaDeUso: String = formatter.format(Date())
    var Usos:ArrayList<String> = ArrayList()
    var seccionPrenda:String = ""

    constructor(parcel: Parcel) : this() {
        //Aquí se especifica cómo se desempaqueta
        tipoPrenda = parcel.readString().toString()
        fechaDeCreacion = parcel.readString().toString()
        ultimaFechaDeUso = parcel.readString().toString()
        seccionPrenda = parcel.readString().toString()
        Usos = parcel.readArrayList(null) as ArrayList<String>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tipoPrenda)
        parcel.writeString(idPrenda)
        parcel.writeString(fechaDeCreacion)
        parcel.writeString(ultimaFechaDeUso)
        parcel.writeString(seccionPrenda)
        parcel.writeList(Usos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Prenda> {
        override fun createFromParcel(parcel: Parcel): Prenda {
            return Prenda(parcel)
        }

        override fun newArray(size: Int): Array<Prenda?> {
            return arrayOfNulls(size)
        }
    }

}
