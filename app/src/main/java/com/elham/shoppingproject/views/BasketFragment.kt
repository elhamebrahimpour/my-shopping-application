package com.elham.shoppingproject.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elham.shoppingproject.R
import com.elham.shoppingproject.adapter.BasketAdapter
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.OnAdapterUpdate
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BasketFragment : Fragment() {
    private var productDataBase: Database? = null
    private var executor: Executor? = null
    private var recyclerViewBasket:RecyclerView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewBasket = view.findViewById<RecyclerView>(R.id.recyclerViewBasket)
        basketExhibition()

    }

    fun basketExhibition(){
        productDataBase = Database.getInstance(context?.applicationContext)
        executor = Executors.newSingleThreadExecutor()
        (executor as ExecutorService?)?.execute{
            val productList: List<Product> = productDataBase!!.productDao().getAllProduct()
            activity?.runOnUiThread{
                val basketAdapter = BasketAdapter(productList, requireContext().applicationContext)
                basketAdapter.setOnAdapterUpdate(object : OnAdapterUpdate {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAdapterUpdate() {
                        basketAdapter.notifyDataSetChanged()
                    }
                })
                recyclerViewBasket?.adapter = basketAdapter
                recyclerViewBasket?.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            }
        }
    }
}