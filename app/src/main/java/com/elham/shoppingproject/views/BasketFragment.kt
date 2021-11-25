package com.elham.shoppingproject.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.elham.shoppingproject.adapter.BasketAdapter
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.databinding.FragmentBasketBinding
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.OnAdapterUpdate
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BasketFragment : Fragment() {
    private lateinit var binding:FragmentBasketBinding
    private var productDataBase: Database? = null
    private var executor: Executor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentBasketBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basketExhibition()
    }

    //-----------basket list exhibition
    @SuppressLint("NotifyDataSetChanged")
    private fun basketExhibition(){
        var basketAdapter:BasketAdapter
        productDataBase = Database.getInstance(context?.applicationContext)
        executor = Executors.newSingleThreadExecutor()
        (executor as ExecutorService?)?.execute{
            val productList= productDataBase!!.productDao().getAllProduct() as MutableList<Product>
            activity?.runOnUiThread{
                basketAdapter = BasketAdapter(productList, requireContext().applicationContext)
                basketAdapter.setOnAdapterUpdate(object : OnAdapterUpdate {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAdapterUpdate() {
                        basketAdapter.notifyDataSetChanged()
                    }
                })
                binding.recyclerViewBasket.adapter = basketAdapter
                binding.recyclerViewBasket.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            }
        }
    }
}