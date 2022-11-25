package com.example.socialx

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Sign_Up.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sign_Up : Fragment() {

    lateinit var btn : Button
    lateinit var email : TextView
    lateinit var password : TextView
    lateinit var firebaseAuth : FirebaseAuth
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign__up, container, false)
        btn = view.findViewById(R.id.signUpBtn)
        email = view.findViewById(R.id.editEmailSignUp)
        password = view.findViewById(R.id.editPassSignUp)
        firebaseAuth = FirebaseAuth.getInstance()
        btn.setOnClickListener {
//            val intent = Intent(requireActivity().applicationContext, Home::class.java)
//            startActivity(intent)
            var et_email = email.text.toString();
            var et_password = password.text.toString()
            if (et_email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(et_email).matches()) {
                Toast.makeText(requireActivity().applicationContext, "Please Enter Valid Email !", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            if (et_password.length < 8) {
                Toast.makeText(requireActivity().applicationContext, "Please Enter Password more than 8 character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            firebaseAuth.createUserWithEmailAndPassword(et_email, et_password)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        Toast.makeText(requireActivity().applicationContext, "successfull", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity().applicationContext,Home ::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireActivity().applicationContext, "Email Already exist", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Sign_Up.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sign_Up().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}