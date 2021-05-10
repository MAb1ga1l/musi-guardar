package com.example.proyectofinal.ui.Auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.proyectofinal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "emailiIngresado"
class AuthLog : Fragment() {
    private lateinit var buttonRegistrar: Button
    private lateinit var buttonIngresar: Button
    private lateinit var textEmail: EditText
    private lateinit var textPassword:EditText
    private var emailiIngresado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            emailiIngresado = it.getString(ARG_PARAM1)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(email: String) =
            AuthLog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, email)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_auth_log, container, false)
        buttonRegistrar = root.findViewById(R.id.signUpButton)
        buttonIngresar = root.findViewById(R.id.log_in_button)
        textEmail = root.findViewById(R.id.text_email)
        textPassword = root.findViewById(R.id.text_password)
        return root
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onStart() {
        super.onStart()
        buttonIngresar.setOnClickListener {
            //para aseugarar que existe un usario y contraseña
            if(textEmail.text.isNotEmpty() && textPassword.text.isNotEmpty()){
                //se inicia servicio de registro de usuario con Firebase
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(textEmail.text.toString(),
                textPassword.text.toString()).addOnCompleteListener {
                    //Este lo pondremos para asegurar el ingreso en firebase
                    if(it.isSuccessful){
                        //Si es correcto ingresaremos al menu

                    }else{
                        showAlert()
                    }
                }
            }
        }
        buttonRegistrar.setOnClickListener {
            //para aseugarar que existe un usario y contraseña
            if(textEmail.text.isNotEmpty() && textPassword.text.isNotEmpty()){
                //se inicia servicio de registro de usuario con Firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(textEmail.text.toString(),
                    textPassword.text.toString()).addOnCompleteListener {
                    //Este lo pondremos para asegurar el registro en firebase
                    if(it.isSuccessful){
                        //Si es correcto ingresaremos al menu
                        activity?.onBackPressed()
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }


}