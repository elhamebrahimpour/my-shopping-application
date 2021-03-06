package com.elham.shoppingproject.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daasuu.cat.CountAnimationTextView
import com.elham.shoppingproject.adapter.BannerSliderAdapter
import com.elham.shoppingproject.adapter.ProductAdapter
import com.elham.shoppingproject.adapter.ProductBestSaleAdapter
import com.elham.shoppingproject.database.Database
import com.elham.shoppingproject.databinding.FragmentHomeBinding
import com.elham.shoppingproject.model.Product
import com.elham.shoppingproject.service.AddProduct
import com.elham.shoppingproject.service.ImageLoadingService
import com.elham.shoppingproject.service.OnRecyclerViewItemClicked
import ss.com.bannerslider.Slider
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var productDatabase: Database
    var executor: Executor?=null
    private lateinit var countAnimationTextView: CountAnimationTextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //------------initialize the slider:
        Slider.init(ImageLoadingService(this))
        binding.sliderMain.setAdapter(BannerSliderAdapter())

        countAnimationTextView = binding.txtCountAnimation
        productDatabase= Database.getInstance(context?.applicationContext)
        executor= Executors.newSingleThreadExecutor()
        recyclerViewSetup()
    }
    //------------setup recyclerView:
    private fun recyclerViewSetup(){
        //updateCounter()
        val addProduct: AddProduct =(object :AddProduct{
            override fun addNewProduct() {
                updateCounter()
                Toast.makeText(context,"???? ?????? ???????? ?????? ?????????? ????!", Toast.LENGTH_SHORT).show()
            }
        })
        val productAdapter=
            ProductAdapter(getAllData(),requireContext().applicationContext,addProduct)
        binding.recyclerViewSeasonBestSale.adapter =productAdapter
        binding.recyclerViewSeasonBestSale.layoutManager = GridLayoutManager(context?.applicationContext,2)
        productAdapter.setRecyclerViewItemClicked(object : OnRecyclerViewItemClicked {
            override fun onProductClicked(position: Int, product: Product) {
                val detailsIntent=Intent(activity,DetailsActivity::class.java)
                detailsIntent.putExtra("keyProduct",product)
                activity?.startActivity(detailsIntent)
            }
        })
        //-----------recyclerView BestSale
        val bestSaleAdapter= context?.applicationContext?.let {
            ProductBestSaleAdapter(getBestSaleProducts(),
                it,addProduct)
        }
        binding.recyclerViewBestSale.adapter = bestSaleAdapter
        binding.recyclerViewBestSale.layoutManager = LinearLayoutManager(
            context?.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        bestSaleAdapter?.setRecyclerViewItemClicked(object :OnRecyclerViewItemClicked{
            override fun onProductClicked(position: Int, product: Product) {
                val detailsIntent=Intent(activity,DetailsActivity::class.java)
                detailsIntent.putExtra("keyProduct",product)
                activity?.startActivity(detailsIntent)
            }
        })
    }

    //------------getAllSeasonProducts from list:
    private fun getAllData():MutableList<Product>  {
        val productList: MutableList<Product> = mutableListOf()
        val product= Product("shoes"
            ,"https://images-dynamic-arcteryx.imgix.net/F21/1350x1710/Konseal-AR-Shoe-W-Whiskey-Jack-Infinity.jpg?auto=format%2Ccompress&q=75&ixlib=react-9.0.2&w=688"
            ,9000.000)
        val product1= Product("sport"
            ,"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBYWFRgVFhYZGRgZGBoYGBgaGhgYHBgaGBgaHBgYGRgcIS4lHB4rHxgYJjgmKy8xNTU1GiQ7QDs0Py40NTQBDAwMEA8QGhISGjQhISExMTQ0NDQ0NDE0NDQ0NDQ0NDQ0NDQ0NDE0NDQ0NDQ0NDQ0MTQ0NDQ0NTE0NDQ0NDQ0Mf/AABEIAOIA3wMBIgACEQEDEQH/xAAcAAAABwEBAAAAAAAAAAAAAAAAAQIDBAUGBwj/xABIEAACAAMEBQYLBgUDAwUAAAABAgADEQQSITEFBiJBUQdhcYGRoRMUMkJSkpOx0dLwI1NUYnLBFzOCouGywvFDc+IVFiQ0RP/EABkBAAMBAQEAAAAAAAAAAAAAAAABAgMEBf/EACARAQEBAQACAgMBAQAAAAAAAAABAhESIQMxIkFRE2H/2gAMAwEAAhEDEQA/AOhztIzB53cvwjJ6xa1WqUDcm0/oQ+9Y0Nojn+uJwPRG3jOMdW/1SzOUrSQOFoHspPyQQ5S9J/iR7KT8kZFxiemFLLjBpGv/AIlaS/ED2cn5IJ+UnSVMLSPZSfkjINAGMKqjU/xM0n+JHspPyQP4m6T/ABI9lJ+SMeYEUTX/AMTdJ/iR7KT8kD+JulPxI9lJ+SMfFno3QNpn/wAuS7D0iLq+s2fVB052r3+JulPxI9lJ+SHrPyi6VdgqTyzNgqrJlMx6FCVMTdE8mrGhtEwKPQTE9Bc4dgjoegNA2ezCkqWFJzbNm/U5xMT5RXhf2qtAnTEwhrRahKXO4suQznpNy6v93QIodatbNJWW0vKFo2PKlky5OKN5ONzEjFTzqY6bSMtygaB8Zs99BWZKq68WSm2nTgCOdab4nVvPTX4/GXljAnlG0l+JHs5XyQzN5StJg/8A2R7KT8kZwiI1oGERnV6v5cSZ9Rqhym6T/Ej2Un5IP+Jmk/xI9lJ+SMbBxu5Otj/EvSf4keyk/JA/iXpT8SPZSfkjHXoFYB1sf4maT/Ej2Un5IA5TNJ/iR7KT8kY6sGDAOtiOUvSf4keyk/JD0rlI0kf/ANA9lJ+SMTEmRDgdj1O1ntc9JjzZl66yhdhFphU5KOaC1g1otcvyJtP6JZ96xUcnh+ymD84P9sDWgYRpMzxZW3yLsmudvbOcPZyx/tidL1ttZzm/2J8sZSwS6iJ6JHBrV79u/OZz6dDtJjnut5zjoNqjnetxxMej+q87V9xgGGJg71BAY4w25jnbQlnglMJYwFgVKnaM0a09yiEXqXgOOIB98bTRvJtWhnTDT0Uz7TlGa1OtFy1yicmYof61IH912O22cxlrWpeNcZlil0XqbZZRBWUCwyZ9o98aFJYAoBhDgWFQuKN3YUgpCiYKsJR4GFCGZcOiLl6ixyDlB0Atnnq6EBJ5chaUuMt28P0ktUcMRuEYm0SyKiOu8qyqbKhPlLNW70lXDD1a9kcpV72f+eb9/rJzMvuC/Lfq/StgQ/OszLjmOPDp4QxGjAIEGBB0gAUgAQIEACJVn4xFiZZxVYcDoPJ3N2Zq/oP+oQ9rO4in1GnFZjr6Uv8A0sPjDesdv26Rp38WVn5pGjqUiYGFYo7JbABlEtbcI8/Wfb0M306da3wjm+t82pIEbK12kkRkNMWNnJNI79akjz/C2sA5xgRoG0IeEENDxh5RvMVnGUwYUxoxoaA+h4PKK8Ko7NOKOjjzHV/VYH9o79ZJlQDxjir6KPCOt6BesmXXO4tekKAe+M96l5xeJZ3q7V4cVoYVYUBE9Xw6YAEJDQYaAHFh8iI6tDt+KzU6jm3K9ahdkSt5Z39VQo/1t2RzVW+uP1SL/X/SnjFtmEGqS/sk4bFb59cv1ARnAY3zPTDV7UuXMP19fXvQ9lVsRh7obDc4+vow4HPD3RRNTqnqGlslO/jQVlN24qFihzUsWK1BGOz24ERR6y6rWiwvSat5GNEmrW4/Aflb8p56VGMaXkptDC2snmvJe8OdGQqera9Yx2K0WVJqNLmKrq4usrCoIh+M4Hl2AI65prkmltVrLNKH0JlXToDjaXrvRy7SmjZlnmNJnIUdMxmKHJlIwZSMiIngRYnaOcAkNkadXPECH5ABw35iANnq9Luz1I3qw/tr+0VmsaHwhhGr1qZZiVOVfcYf0sb71itX8USflKg2dGpEhVaHLNLoIdCxx6+3bn6dMezCK+02fmize1CIk2aDG+s9jHOpKo50qGvAc0Wr0ghSOb/PXXR544qGl03QnwNd0W7KIIXRBfj2c3hViyc0arQ8uktaZivvMVfhFiTonT9mvmSZqh60oagVyu3js1rurBnGpfZa3mz00CPxh4GEqsFSkO+klVgVhF6DrC6qQsGKTXDTgs1md67bbEscXIwPQBUno54uS9Kk5DfHEdctPG12glcZaVSWvEVxbpYjspGmJ2s965FEiFjTrJz6+c1i2s1ny2iBw8pabwyHBq1xr3YUYsdnAHvPE83MMu074sJZEdMjmtQrdo67tJShrsiprTElCcTQYlTtL+YbUVwi5nuKEAEk5AbiMQw4EZ13RF0bo17VaFkygC7kVNNlaAX3NMlBqe4ZgQUdbvkg0WS820kEKq+BTgxYh3OWNLiZHzjHVREDQ+jEs8hLPL8lFu1ObGtWducsST0xOEOEXejA8regBNs3jSD7SR5RGbSidoH9JN4cBe4xvIj2+UrynRvJZHU1yoykHuMFh9eYRBq9DWG1NQOiLbVuSrWmUHAZb2IOIOycxEGk6CYPNRVrXfTEZb+EaObow1yjTIUQURVX9IA90NvMWJ3NWemmLmfbPpo48IJrAeEaG8sCqxhcabzeTL3uMRZk1wc4tnUcIjNZ6x1+NcSCs94UJjROWyCFNZxBw+mJdTnDE9G3RPRKQTrWH4kpZl+OfWlzfc189seNWPdHTtJNckzH9BHYdIUkd8cqETZw46HqVrtcuyLS2xkjnEpwDHenP5vRl1NaEVG+PNX10RttTNeHs4EqaGeUAbtKXkH5akAr+UkU3HcY1nv01zrn2694MQky4xNl5TrOzUeVMRCaBwVcf1KKEdAvGJGsuvkiVLpZ3WbMYbNK3EqPKeuZHoZ1zpGf+a/PiFyj6xiWhsqHbcfaEeZLPm9Le7pEcysMupvcagdHnH9u3hCJ015rlnYs7klmOJJzZjFnIlUGVMMBwAy+umNcZ5GO9dKwApu+vrugg/1/z9CEO1ThEWdPpspUsTQkY4k0CoBmfoY5aILuu7iXLBeY5CKq51OSj83HhzUw7hqVqslhk0NGnOAZr8OCIfQXvOPADHapWJLBLE90D2lwQCxAWWu9ZdAS7ZhmAA3A0xNs+nrS5wdUB3LLHvctBIfW/pAjJ6r26e85ldy6BKklUFDWgIKKM8RjnRuEayGQRn9dtKCz2Ke9aN4MonO8zZWnQWr0KYvJz0FBn9d8cS5S9ZRaZos8s1lSWN5hiHm0IJB3qoJUHeSxypCt9BiVwwi41Xl1tMsDie5TFQBXpi71RP8A8mX0t/pMKfZ10JrOeMF4vEpjAiuF1G8WMH4sYlAwoNBwGy0JJg7kC5FEAmQReFBIF2AiawKwCsCkA6p9abSi2aYrMoZ0KopIBYkgG6N9KxzKNrr7ZgPBzSc6y7tDXe16vAZdY6sYQP8AI+EZ37XCaweVeeCqOmDMSYKentg6mCWDgByW90gjd9U6DjFi9rQgbVMMQQajq39UVcKBioD860Eii1A3k5n4Dm/4jS6raOSWotM1Tj/JTKoyL8RXIHcAzb0MU+gNHCdM2x9mgvzMxUbkBGRahFcwLzebG2lhJqvNdkCIwQoS6Oodao8uilRgpVQ2zRabwYcK+jM15kxwzIbpwVhS6KZIoBqKcOeuNcb3R1lLkS0AvEY50Vd5J4DvOG+MzowNOniXIVjXybxxCjAu5GCjeeFaCuFeraI0alnS4pvMcXc5sf2Ubhu6SSavspD2j7EklLidLMc2bIsewCm4ADdEiY9BBM9BHP8AlA108WBkSTW0MMWzElSMG/URio3eUd1S8gQOUfXIpeskhts4TnGaAjyFO5yMz5owzJpyoQbMSSSSSSSSTUknMknMwBGdvVADF7qtTxmUec9t0xRGLHV+03J6Mcg4r1mn7w59lXWGMCFFIF2NEipBiDCQarACCsHSF0giIAIQREHBiAjZEJpD0AQDjnev1sV5qS1atxWv0yDsRh0gKOisZUc0TNKspnzbhN3wj0rQ1F874hk8QYyt7VworWCCwA0GBCMBChBAQqkAHSDAJIABJJAAGJJOQA3kkgQkxodWLHS9aXGzLBuk733kdAOB9IgjyTFBdaNsdxBIRWcS6TrVcNC20quFbIAAhATRaKWqt5qN6xaQ8NOWzWRHoxCohvXhWjCWL6q6ICLxRiyqy1BAEDSFqazyke/KDuBPkzJE4+FUuCKTJbCjSrqFCoFAQanaIjZagasmQhtM8VtE0E7WLS0Y3iCT57HFuzcasLnVXQC2SUFqGmsAZj8T6C7wg7898XZaIlrtaoKmKmfp28NgG9xIwHPTf9dBq2ZnSk7eGda9MTUXwdmS/OYYE0CSgcmeuZ4LvzOFAeaLqJa5jF5joGYlmZmZ2JJqSTTEx0ezy64nPMk5k7yTFkqxza3rV9OifHmfbmkrkzmHO0L1If3aHJ3JoQNmfU86UHcY6eiwq5Ctv9Hjn+OJ6Q1JtcrEIJi8UNT6poYpJMlkmIHVkIYYOCpzHGPQ1yEWnR0uajI6K4IODAGKmrPtOsT9KEQV2FJwhUdLnIpBgQsCFAQBGJgGFAQREBEwCIVSBADZiBpy2eCs8x710hCFP52wWnPU16oszGM5RSbslPNLOx6QFC9xfvhW+jjDMn1+8BRUe6CyyhWPVnzxmsmkCDJgx9fXZCAoUIA+vrtgUhwJOjrI02aktc3alcgFALMxPAKGbqjoMi0ybPNkSmMxJUs18IqqT4RCGAcEEMovEuFqbzkilRFTqtZPAyjaGXbmbMpcSboagN3PadQehBufGy05IZZMqUGWYXFVFxXuzGc33k2hKXrzYFWxFVBBIqKkK1N1d0atttz2goFs1ncmWgFFaYTeBu1IGO0aUBJU0qWjcaV0mqYZscl/c8BEay2VbDZElihYDH88xsXPRWvUAIoWqzFmNScSTFSFaE+c7m85qd3AdAg5C5wT0UEk0AxJOQheipyTVLowZakVGVRmIj5bzK/indROshiySIUtKRLltHLmuqpSxIAiOpiSuUWmmnEOS4Q8KlGAmXnpR2HBiO8wlREvSSUmP017RDCiOqfTlvqk0g8YVB3YZGaQRhUCAuEGCg4IwBH0hahKlvNbEIpamVTuWvOaDrjkVttTzXaa5qzGvQNyjgo3COi6524S7My+dN2BzDN26hQdLCOaluOPviNLyD5Bur/MErVh1X390NXoRjpAAgCFiEBRP0Fo8T56S2LBMXmMBiktAWdhwNBQc7LxpA0XoifaWuWeU8w5EqNkEZ3nNFXMZkZjjGrt2ijo+zeAdlNotNHnXTUJLQkJLDb6tUk8VwrQGHIEzR1rV5yuVKSxWVJAVyvkXBLvptIQjCjKGZdnAxbasyxabcJhUXZSB/NNSuxLvMFW8a1YGgwQDdGOslqnSF2ZlEmKbyq1QRUqQ6kUDUxBG5s61EbzkxmL4C0zT95Q/pSWrDvdoqITdO2m/NI81NkdPnHtw/pirtVqSUhd2CqN57gBvPNFLrPpabZxLdbhvs19WzYnHDGtM+6MPprS72l774AYKgrdXo5+eC3h86sdYtZmn1RBclV3+U9Mq8BzRpuTO03pcyX6L3vWA+UxzYmNnyZ2q7aWQ+emHShrTsZuyMfk7c1r8frUdQuQoIYepBgRz8dXSFciJ0tqxEpDyPSLzUaOuIShxgF4IGHUqrS6/aV4qIiCM7yh6auWiSqPjLVi4Bw2it0Nz7LdvPGiTIHiAe2OnF7lzanNUYgQaiCEWk3BXYWDBM8AMkQCCYcJqIz+t2lvASCqmkyYCqcVXz36gaDnI4QW8HGK1p0n4eexBqibCdAO03We4CKiECDrGawKiBAgQAd2N9ye6mraB4zaFJkg0lpkJrAkFmINbgIpTzjvoCDS6k6sNbZ21VZCUM1wM8QRLU18phXEZDHhHc5aKqhEUKqgKqgUCgCgAAyFIeZ0HJahVCIoVVFFVQFUAZAAZCOJa56U8LbJ2OCOZa9EvZPawY9cdujzhpdHSfNR/KWY4bpDnHrz64ekje0HKsanQWmmsNkmB8WtIDSZe+4AVaa581G2Qu83CRhStJoyyJLQWq0reQkiRJOBtLrne4SVNLx3+SM4qtI2t50x5kxrzuaschwAUblAoABkBE9PgrfbXnOXmMWY9gHoqNwiMxpnADQTYwrTHhD1ktby3V5bFHU1VhmMKb+YnthiDpCDtGo+nzaZH2jL4VGKuAQCw3Nd3VB6KgxqCI84I1CCMxkd46DE1dM2gCgnzqf9x/miLj36aT5HoKkJd1XFmAHEkD3x59fSk45zZp6Zjn3mGJk5m8olv1En3w5gf6f8dxt+t1jlVvTlYjC7L2zXhs4DrpGB1g5QZs0FJA8Eh86tXPWMF6q9MYokwm7DmYm6tPK1TUkmpqScSa5kmOr6r2zwtmUk1Zdg9Xk91I5Ioja8n+kLrtKJwcVX9S/4r2RplFbtjAV4MiAqbzFpNmCaFVgitcuuAG5s9URnY0VFLMeAAqTHI9N6Ta0zmmtgDgi+gg8lf3POTGo180tSlmQ8GmHvRP8Acf6YxERqqgVghArAEQYxFzqxq/Mts4Sk2VFGmTCCVRCc+F440XfQ8CYLVnVydbZlyUKKKeEmMDcQHjxamSjE82cdz0FoaVZZKyJQooxZjS87HN3IzPNuFAIrM6D2itHS7PKWTJW6iDAZkk5sx3sTmYlOaQTPTAZw2TGiSr0YrXwaPQeGtElXnNgiqzI0wrhtFT5AyLHLLE4Q/rhrhLsq3Fo81hsoDSgOTuRiq97bqDajjekLfMnO0yYxZ2zOQAGSqBgqjIAZRGrDh3SukHtDmZMIrQKABdREGCoi+aoGQ68SSTEvQiDiDHWCrABgGABBgwVYKADMGDCawkuOIgByDrCks7tkjnhRGNewQ8NGz/uJvs3+WGDAMGIeNgnfczfZv8sGlgnHKTNPRLc/7YAbUxMsE1kdXSt5SCKZ1GQiTYNVbdNIuWab0stwdNXpHT9TOT1bOyz7SVeYDVEXFEIyapG23VQd8VIFxbNHMiLMBqpAqCKFajf1xDvRqNMmsluah7xGXOOEXE1HKnOsRdK25ZEl5rZKuXpMTRV6yQIeL1FLtTFnodk20mKjBguyyFwbpJIpQjeM4X6Dg9onM7M7mrMSzHiTn1Q1WOu608myTm8NYmSWx8qU1QhPFCoN3ooR0RS2HkotBP29olIK5IGmMRvzCgd8RZVOeV+EbrVLk8m2ik203pMnAqpFJkwVyAOKrTzjjiKDfHRNBao2Sxm+iXnoB4WaQzCmNUFLqY8BXni6a0iuALHjuhzP9LpFgsUuTLWXLRUloNlRh0seLHMnMw40yuWAhp33uQOG7HgBxjN6xa5WezVUm8+5FoX6xknS1D+VorshNHMmKgqxpn3ZnoHHIb45xrhygULSbKQWBIabgyqR6G52/N5I3BsCMdrDrZaLUSGa5LP/AE1JoeF9s3PTsjcoils0l3N1EZzwRWY9igxNvT4KZMZmLMxZmNWZiWZicyScSeeExpLDqNb5uIs7IPSmFUHYTe7o0ej+SmYaGfaEQb1lqXPrNQDsMT403OIEdStPJOpH2VpavB0BHapFIhWfkonltu0Iq8UVmJ6jQDtMPxoc6iXYNFzp9fAynemZRSQP6so7Ronk+sUmhZDObDamG8K8QvkjsjTS5YQXUUKNwAAHdDmf6XXG9H8mlsfFykofmN9vVXDvjS2Lkus6/wA2dMc8FuoP3PfG+cjeYIOOeHyDrO2TUawJlZg54uzP3MSIvLPoyUgASTLSgpsoow7IlrMHow4s7miuDpCIeAHUIcCn6EKE0cD3Qrwo4HshEF2AOrsg/CCDDQ+qFeMAVhYWFCkLpcE8kMpVhgRSMhMlFHKkeSSOnnjZgxkdITb0xyN7YdAw/aCCqxEOF0mh+jBpMINQSCMiDQ9oMMvMqaK1ePwhlp7CopzDPP4QRK/s2kSwwz33plOy8KEf1RL8YfeMOZ0p23ows21ugwqzY4GgA4VMUVv0m4W7RWvCrH82eB4CA+usqa4hOepdTXn2bxpCkLE0Lqn6Rj1M3wEcc0PrbarOafzJedw1ovOjgVXvHNvjY2HX6zTBRyUO8OLtP6vJPb1QSwNhbNFy5ilWaYK5skxkYjhfG0BzVpGeXk90cD/Kduma/wCxiXI0pJfyJ647r1O4xLSY5yevZD5KDVl1WsMvyLJKqN7i+e16xcSluCiBUHBQFHdENC29jDgEASb3PClMRwwg2nqM2A6SBAEtTDlYo30/Zk8u0SlOVDMSteitYana0WZc5y14A4nornAF8xEVOmtOSbMl+a6oNwObHgBmeqKHSevMtUIkqzviFBBVQdxYnd0VMc4t9itFomGbOJdm3nBQK5KPNHNCtDT2/lOSpEqU7D0mIWvViYjSOUo125RA5jWM2NCMTgvHHoxh5NXiaYZ9Aie6Dd2DlCs70DMVP5hTvjV6M0vKmi8jgjiMY4z/AO2zmcuOETdFaOn2Zg6TCuRK7RQ13EVFemHLf2HbFmDiO2FBxxEc5TTlpGLKlN5qw+OEODWCfjVEFKHGYBUE0BAKY14RXQ6KCOIhxSOIjn0vTlpP/TGOAq9D2XIcXTFpw+zXEb3/APCF0+ugXhxEJa0qN8YIaYtNDsJUYkeEJI6RcwiM862zK3nWUn5FqxG8FmrTqoYB1r9LabRAUDC+VqFrioOF9uA4cYorOQo9x6YqpGiAm3tsWNGZiWLV3tU164mTLMQQAxGG73Yw4RF0UN1VB3VFA3QaQ0tnAoSBfOdN3XTKkKki5W7tGuLGprXMimA6Bwx4wKITdBKk1JIGJp5WHHLE8eyQi2uwkqStQSK3hvpxG8c0V8qwKAyOajyiSGJ31wIyzpFo8wKt2rEVU1pQgUJIOAH/ACOaIr2sqTsstQd4xJxyJONK4ZQdHFd/6dKN1sdo0G0UJ6hmOaIp0DLKMwRiwPkkVrlWnb9GLUW80qoPEbNDierh3wwbVMWpDUqaEKMa5EVyrllCtHFemrSLeJqlFJrtDHnIwHbATRjgKFmOAd4au7IkkAGsSJcxycyopVd9d1SFPTnCy0wqEZxjWuAJbGvVuFIXQZs1jcEL4SeLwxBLKekVOXRDo0eSDSY4pUEeEcmnHEjuhKlkcgsBQnFsDdpmcMciMcu6EJRmG2u5sC15kN2goSQa1qAD3QwS9ml1oXZgnlMXahN2ooxNDjQYdEOLo9HqSgVxhzHdUEip/wAw/KcB7jEsrEm6wepatcCRjnviTZbC7PQ4EKTcBDY44lami5bsOyAGLPouWFBKqK0Awpuz58eaFeKA7KLQ4m9dBoFzF5RhwGBziwk2OZeoWWigtS8KEEYMxBwFKCgqK9kJCUmLLaYTs1FTUgkgDAUFN1TjgesBp7PjculheA8m8FPHm/zDkmy1wFRvNaMteA4GH7wuiikuAPLehU412DkQe0MMYkyJgW6puGoqVoHK7sK1zIbHqhhEFiIGyAa1vHgQd1K1NN0OPYwxWqX61XFl2SeY5jvhxJ5G05JIICsFQgqWwICDAYnMDI4CDksL1WRi7A7NcBUnfW6ffjARFm0UUGFxVpgoqTznr4bqmHUs5LkUN0LmAACccK1PNuhXjV5K0ujfdps0NTe315gDvrCbPPvUKqHUjF1YDf5QIxrQ9o4QzG6oVADijCoBod4ptZUOO6GvF6BrzZUvLTAb1CkmmPTwyhbSL2BGBqRRirVFMS6GtKHAZYNxh159yikFr16mN80A8k0zNMcTB0gopFSCu8VowyO0LpwHPzw2swim0cErdFDmRhgN37xI2cAzE3MFLNU0IBKrU1OK5flhqYak1vZAkrRTnnVvf050xfQfkODUZmmOBOeIN7IdA5oWZigYORdpU4gVPGvEUxhFnQOuw+QxKGpqd9Ma7jXHfCbQ4zozYNhdWtdkXaDPyB04YwA87io2gAcAKVDdanmMGjDyiDQigJwFOBrkcu2ISAABkG07VYi7WppUmhwpjhXPjElrS90gTbhJGdGG6l5GFASoGIGcB8dH8Ul+gnqj4QQscv7tPVX4QIEYrFLskv7tPVHwgPYpf3aeqvwgQIAPxKV92nqr8IPxSX6CeqPhAgQAXiUv7tPVX4QPEpX3aeqvwgQIAHiUr7tPVX4Qk2GVUHwaV43V+ECBAC/E5f3aeqPhCRY5eP2aeqvwgQIARKsEr7pPUX4Q74pL9BPVHwgQIALxOX92nqr8IPxOX92nqr8IECAB4pL9BPVHwhKWSX6CZeiPhBwIAPxSX6CeqPhA8Ul+gnqj4QIEADxSX6CeqPhB+LJ6C+qIECAEmzp6C9gg1skv0F9UfCCgQARs6egvYIV4pL9BfVECBAA8Ul+gvqiEzLJL9BPVHwg4EAf/2Q=="
            ,8000.000)
        val product2= Product("?????? ?????????? ??????????"
            ,"data:image/jpeg;base64,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"
            ,6000.000)
        val product3= Product("?????? ???????????? ??????????"
            ,"data:image/jpeg;base64,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"
            ,6000.000)
        productList.add(product)
        productList.add(product1)
        productList.add(product2)
        productList.add(product3)
        return productList
    }

    //------------getBestSaleProducts from list:
    private fun getBestSaleProducts():MutableList<Product>  {
        val productList: MutableList<Product> = mutableListOf()
        val product=Product(
            "?????????????? ????????????","https://dkstatics-public.digikala.com/digikala-products/117528170.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/quality,q_90",
            600.000).apply {
                bestSalePrice = 540.00
        }
        val product1=Product("?????????? ?????????? ??????????","https://dkstatics-public.digikala.com/digikala-products/abd18a697e583ef421806e063af67e465c630bbb_1607771340.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/quality,q_90",
            1100.00).apply {
                bestSalePrice = 995.00
        }
        val product2=Product("???? ?????????? ??????????","https://dkstatics-public.digikala.com/digikala-products/df46c623f82f170aa57784cf1b30b031805f3344_1612420472.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/quality,q_90",
            900.000).apply {
            bestSalePrice = 840.00
        }
        val product3=Product("?????????? ?????????? ??????????","https://dkstatics-public.digikala.com/digikala-products/116687498.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/quality,q_90",
            650.000).apply {
            bestSalePrice = 610.00
        }
        productList.add(product)
        productList.add(product1)
        productList.add(product2)
        productList.add(product3)
        return productList
    }

    //------------counter
    @SuppressLint("SetTextI18n")
    fun updateCounter() {
        executor!!.execute {
            val sum = productDatabase.productDao().getSum()
            requireActivity().runOnUiThread {
                updateCounter()
                countAnimationTextView.text = sum.toString()+""
            }
        }
    }
    override fun onResume() {
        super.onResume()
        updateCounter()
    }
}
