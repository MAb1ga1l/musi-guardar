package com.example.proyectofinal.ui.home.secciones

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.filesPlus.Inventarios
import java.nio.file.attribute.GroupPrincipal

/**
 * A simple [Fragment] subclass.
 * Use the [Torso.newInstance] factory method to
 * create an instance of this fragment.
 */
class Torso : Fragment() {

    val inventarios = Inventarios()
    private lateinit var labelPrincipal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_torso, container, false)
        labelPrincipal = vista.findViewById(R.id.ejemploTorso)
        Log.d("TORSO","inventario recibido: ${inventarios.inventarioTorso.size}")
        return vista
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Torso().apply {
                arguments = Bundle().apply {
                }
            }
    }
}