package com.elham.shoppingproject.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.elham.shoppingproject.R
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.databinding.ActivityDetailsBinding
import com.elham.shoppingproject.model.Product
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    private var hoverMarker: ImageView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this,"sk.eyJ1IjoiZWxoYW1lYiIsImEiOiJja3drcjVmbjExdWlkMm9tanV6eHNubnd2In0.DsYkONHYwRK0L9qn72NAYQ")
        binding= ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
       // binding.MapViewDetail.onCreate(savedInstanceState)
        //------------mapBox initialization
        binding.MapViewDetail.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                goToCurrentLocation(mapboxMap,38.02619407288767, 46.365125074888596)
                showRedMarker()
            }
        }

        detailsLoading()
    }

    //-----------details of a product
    @SuppressLint("SetTextI18n")
    private fun detailsLoading(){
        val data = intent?.extras?.get("keyProduct") as Product
        binding.txtTitle.text =  "نام محصول: " + data.title
                Glide.with(this).load(data.imageUrl).into(binding.imgProductDetail)
        binding.txtPrice.text = "قیمت: " + data.seasonSalePrice.toString()

        binding.txtBestSalePrice.text = data.bestSalePrice.toString()

    }

    //-----------redMarker
    private fun showRedMarker(){
        hoverMarker= ImageView(this)
        hoverMarker!!.setBackgroundResource(R.drawable.ic_location_on)
        val imageLocation= FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER)
        hoverMarker!!.layoutParams=imageLocation
        binding.MapViewDetail.addView(hoverMarker)
    }
    //-----------current Location
    private fun goToCurrentLocation(mapboxMap: MapboxMap, latitude:Double, longitude:Double){
        val cameraPosition= CameraPosition.Builder().target(LatLng(latitude,longitude))
            .zoom(14.0).tilt(30.0).build()
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),4000)
    }
}