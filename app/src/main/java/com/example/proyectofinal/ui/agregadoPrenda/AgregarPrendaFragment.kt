package com.example.proyectofinal.ui.agregadoPrenda

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.filesPlus.Inventarios
import com.example.proyectofinal.ui.filesPlus.Prenda
import com.google.android.material.snackbar.Snackbar
import java.io.File

class AgregarPrendaFragment : Fragment() {

    val inventarios = Inventarios()
    private lateinit var prenda: Prenda
    private lateinit var buttonOk: Button
    private lateinit var buttonCancel: Button
    private lateinit var imageView: ImageView
    private lateinit var imageButton: ImageButton
    private lateinit var archivoFoto: File
    private lateinit var labelSeccion :TextView
    private lateinit var labelTipoPrenda :TextView
    private var flagOk : Boolean = false

    companion object {
        fun newInstance() = AgregarPrendaFragment()
    }

    private lateinit var viewModel: AgregarPrendaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prenda = Prenda()
        //Log.d("AGREGAR PRENDA","Se recibió ${prenda.fechaDeCreacion} id: ${prenda.idPrenda}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.agregar_prenda_fragment, container, false)

        buttonOk = vista.findViewById(R.id.button_ok)
        buttonCancel = vista.findViewById(R.id.buttonCancel)
        imageView = vista.findViewById(R.id.fotoPrenda)
        imageButton = vista.findViewById(R.id.button_foto)
        labelSeccion = vista.findViewById(R.id.label_seccion)
        labelTipoPrenda = vista.findViewById(R.id.label_tipo_prenda)
        archivoFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${prenda.idPrenda}.jpg")

        //foto predeterminada
        imageView.setImageResource(R.drawable.ropa)

        return vista
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AgregarPrendaViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        //Interacción para tomar la foto
        imageButton.apply {
            setOnClickListener {
                val intentFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                archivoFoto = getArchivoFoto("${prenda.idPrenda}.jpg")
                val fileProvider = FileProvider.getUriForFile(context,"com.example.proyectofinal.fileprovider",archivoFoto)
                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
                try {
                    startActivityForResult(intentFoto, 1)
                }catch (e: ActivityNotFoundException){
                }
            }
        }

        buttonCancel.setOnClickListener {
            if(archivoFoto.exists()){
                archivoFoto.delete()
                imageView.setImageResource(R.drawable.ropa)
            }
        }

        buttonOk.setOnClickListener {
            flagOk = true
            //Para asegurar que exista una foto para guardar
            if(archivoFoto.exists()){
                prenda.seccionPrenda = labelSeccion.toString()
                prenda.tipoPrenda = labelTipoPrenda.toString()
                view?.let { it1 ->
                    Snackbar.make(it1, "Tu Prenda se ha guardado. Puedes agregar más", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
                inventarios.agregaPrenda(prenda,labelSeccion.toString())
                //Log.d("AGREGAR PRENDA","Se agregara ${prenda.fechaDeCreacion} id: ${prenda.idPrenda}")
                prenda = Prenda()
                //Log.d("AGREGAR PRENDA","Nueva Prenda ${prenda.fechaDeCreacion} id: ${prenda.idPrenda}")
                imageView.setImageResource(R.drawable.ropa)

            }else{
                view?.let { it1 ->
                    Snackbar.make(it1, "Falta foto para guardar", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
        }
    }

    fun getArchivoFoto(nombreDeArchivo: String): File{
        val rutaDeArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(rutaDeArchivo,nombreDeArchivo)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            val bitmapDeFoto = BitmapFactory.decodeFile(archivoFoto.absolutePath)
            labelSeccion.text = "Torso"
            labelTipoPrenda.text = "Blusa"
            imageView.setImageBitmap(bitmapDeFoto)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //para evitar uso de memoria en imagenes innecesaria
        if (!flagOk){
            //en caso de que se tome a foto pero no se termine el proceso ya sea con Ok o Cancelar
            if(archivoFoto.exists()){
                archivoFoto.delete()
            }

        }
    }

}