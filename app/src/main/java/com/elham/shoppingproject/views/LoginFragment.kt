package com.elham.shoppingproject.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
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
            if (binding.edtxtEmailAddress.text.isNullOrEmpty()&&
                binding.edtxtPassword.text.isNullOrEmpty()) {
                Toast.makeText(context, "فیلدها را کامل نمایید! ", Toast.LENGTH_SHORT).show()
            }else{
                adminLogin()
                val hide: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
                //launchFragment()
            }
        }
    }
    //------------admin login
    private fun adminLogin() {
        if (binding.edtxtEmailAddress.text.toString() == "admin@gmail.com"
            && binding.edtxtPassword.text.toString() == "123456"
        ) {
            Toast.makeText(
                context,
                "${binding.edtxtEmailAddress} ورود با موفقیت ",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "عدم تطابق نام کاربری/رمز عبور", Toast.LENGTH_SHORT).show()
        }
        binding.edtxtEmailAddress.setText("")
        binding.edtxtPassword.setText("")
    }

   /* //------------launchFragment
    private fun launchFragment(){
        val homeFragment:HomeFragment=HomeFragment()
        val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginContainer, homeFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }*/
}
