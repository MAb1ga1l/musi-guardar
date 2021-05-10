package com.example.proyectofinal.ui.home.secciones

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.filesPlus.Prenda
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PiesFragment : Fragment() {

    //Adaptar al inventario de la sección a traves de firebase
    val inventarioTorso: MutableList<Prenda> = mutableListOf<Prenda>()
    private lateinit var tableRecyclerViewShoes: RecyclerView
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for (i in 1 .. 20){
            inventarioTorso.add(Prenda())
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_pies, container, false)
        tableRecyclerViewShoes = vista.findViewById(R.id.recyclerView_shoes) as RecyclerView
        tableRecyclerViewShoes.layoutManager = LinearLayoutManager(context,
            OrientationHelper.HORIZONTAL,false)
        tableRecyclerViewShoes.adapter = PrendasAdapter(inventarioTorso)
        return vista
    }

    private inner class PrendasAdapter(var inventarioT: List<Prenda>) : RecyclerView.Adapter<PrendaHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrendaHolder {
            val tarjeta = layoutInflater.inflate(R.layout.fragment_celda_prenda,parent,false)
            return PrendaHolder(tarjeta)
        }

        override fun onBindViewHolder(holder: PrendaHolder, position: Int) {
            val prenda = inventarioT[position]
            holder.holderBinding(prenda)
        }

        override fun getItemCount() = inventarioT.size
    }

    private inner  class PrendaHolder(tarjeta:View): RecyclerView.ViewHolder(tarjeta), View.OnClickListener{
        private lateinit var prenda: Prenda
        val ultimoUsoTextView : TextView = itemView.findViewById(R.id.label_ultimo_uso)
        val numUsosTextView: TextView = itemView.findViewById(R.id.label_num_usos)
        val vistaFoto : ImageView = itemView.findViewById(R.id.prenda_imagen)

        init{
            itemView.setOnClickListener(this)
        }

        fun holderBinding(prenda: Prenda){
            this.prenda = prenda
            ultimoUsoTextView.text = prenda.ultimaFechaDeUso
            numUsosTextView.text = prenda.Usos.size.toString()
            vistaFoto.setImageResource(R.drawable.zapatos)
        }

        override fun onClick(v: View?) {
            //salrdan las opciones para eliminar o seleccionar como outfit diario
        }
    }
}