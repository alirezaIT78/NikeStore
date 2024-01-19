package com.example.nikeprojectfinaltest2.feucher.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.data.Banner
import com.example.nikeprojectfinaltest2.service.ImageLoadingService
import com.example.nikeprojectfinaltest2.view.NikeImageView
import org.koin.android.ext.android.inject
val BannerNumberLiveData=MutableLiveData<Int>()
class FragmentBanner:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imageService:ImageLoadingService by inject()
        val imageView=inflater.inflate(R.layout.fragment_banner,container,false) as NikeImageView
        val banner= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable("data", Banner::class.java)?:throw IllegalStateException("Banner is null")
        } else {
            requireArguments().getParcelable<Banner>("data")?:throw IllegalStateException("Banner is null Version < TIRAMISU")
        }
        imageView.setOnClickListener({
            BannerNumberLiveData.value=banner.id
        })
        imageService.load(imageView,banner.image)
        return imageView

    }
    companion object
    fun newInstance(banner:Banner):FragmentBanner
    {
        val fragmentBanner=FragmentBanner()
        val bundle=Bundle()
        bundle.putParcelable("data",banner)
        fragmentBanner.arguments=bundle
        return fragmentBanner
    }
}