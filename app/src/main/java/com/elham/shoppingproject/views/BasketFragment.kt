package com.elham.shoppingproject.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elham.shoppingproject.adapter.BasketAdapter
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.databinding.FragmentBasketBinding
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.OnAdapterUpdate
import com.elham.shoppingproject.service.OnRecyclerViewItemClicked
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basketExhibition()
    }

    //-----------basket list exhibition
    @SuppressLint("NotifyDataSetChanged")
    private fun basketExhibition(){
        productDataBase = Database.getInstance(context?.applicationContext)
        executor = Executors.newSingleThreadExecutor()
        (executor as ExecutorService?)?.execute{
            val productList= productDataBase!!.productDao().getAllProduct() as MutableList<Product>
            activity?.runOnUiThread{
                 val basketAdapter = BasketAdapter(productList, requireContext().applicationContext)
                basketAdapter.setOnAdapterUpdate(object : OnAdapterUpdate {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAdapterUpdate() {
                        basketAdapter.notifyDataSetChanged()
                    }
                })
                binding.recyclerViewBasket.adapter = basketAdapter
                binding.recyclerViewBasket.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                basketAdapter.setRecyclerViewItemClicked(object :OnRecyclerViewItemClicked{
                    override fun onProductClicked(position: Int, product: Product) {
                        val detailsIntent= Intent(activity,DetailsActivity::class.java)
                        detailsIntent.putExtra("keyProduct",product)
                        activity?.startActivity(detailsIntent)
                    }

                })
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.recyclerViewBasket.adapter?.notifyDataSetChanged()
    }
}