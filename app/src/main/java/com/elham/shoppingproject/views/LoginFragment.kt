package com.elham.shoppingproject.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.elham.shoppingproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            adminLogin()
        }
    }

    private fun adminLogin(){
        if (binding.edtxtEmailAddress.text.isNullOrEmpty()&&
            binding.edtxtPassword.text.isNullOrEmpty()) {
            Toast.makeText(context, "Blanks are empty, fill them!", Toast.LENGTH_SHORT).show()
        } else if (binding.edtxtEmailAddress.text.toString()=="admin@gmail.com"
            && binding.edtxtPassword.text.toString() == "123456"){
            Toast.makeText(context,"${binding.edtxtEmailAddress} is logged in successful!", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Login failed!", Toast.LENGTH_SHORT).show()
        }
    }
}
