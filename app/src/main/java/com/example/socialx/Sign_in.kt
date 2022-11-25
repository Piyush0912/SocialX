package com.example.socialx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Sign_in.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sign_in : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var email :EditText
    lateinit var password : EditText
    lateinit var btn : Button
    lateinit var google : ImageView
    lateinit var firebaseAuth : FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
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
        val view =  inflater.inflate(R.layout.fragment_sign_in, container, false)
        email = view.findViewById(R.id.editEmailSignIN)
        password = view.findViewById(R.id.editPassSignIn)
        btn = view.findViewById(R.id.signInBtn)
        google = view.findViewById(R.id.googleLoginBtn)
        firebaseAuth = FirebaseAuth.getInstance()
        btn.setOnClickListener{
            var et_email = email.text.toString();
            var et_password = password.text.toString()
            if(et_email.isBlank()) {
                Toast.makeText(requireActivity().applicationContext, "Please Enter Email", Toast.LENGTH_SHORT).show()
            }
            firebaseAuth.signInWithEmailAndPassword(et_email,et_password)
                .addOnCompleteListener(){
                    if(it.isSuccessful){
                        val intent = Intent(requireActivity().applicationContext,Home:: class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(requireActivity().applicationContext, "Email or password is incorrect ", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
        google.setOnClickListener{
            signInGoogle()
        }
       return view
    }
    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }
    private fun handleResults(task: Task<GoogleSignInAccount>?) {
        if(task!!.isSuccessful){
            val account : GoogleSignInAccount ? =task.result
            if(account!=null){
                updateUi(account)
            }
        }
    }
    private fun updateUi(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(requireActivity().applicationContext,Home:: class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(requireActivity().applicationContext, "Sign in Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Sign_in.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sign_in().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}