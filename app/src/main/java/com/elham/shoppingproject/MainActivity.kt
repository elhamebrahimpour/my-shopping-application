package com.elham.shoppingproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elham.shoppingproject.adapter.ViewPagerAdapter
import com.elham.shoppingproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //viewPager2:
        binding.viewPagerShop.isUserInputEnabled = false
        binding.viewPagerShop.adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle)
        //bottom navigation view:
        binding.btnNavigation.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.title_home -> binding.viewPagerShop.currentItem = 0
                R.id.title_basket -> binding.viewPagerShop.currentItem = 1
                R.id.title_login -> binding.viewPagerShop.currentItem = 2
            }
            true
        }
        binding.btnNavigation.selectedItemId=R.id.title_home
    }
}