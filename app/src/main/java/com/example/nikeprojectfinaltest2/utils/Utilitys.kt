package com.example.nikeprojectfinaltest2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

fun convertDpToPixel(dp: Float, context: Context?): Float {
    return if (context != null) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    } else {
        val metrics = Resources.getSystem().displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
fun openUrlInCustomTab (context: Context, url: String) {
    try {
        Log.i("uriBank", "openUrlInCustomTab: "+url)
        Log.i("uriBank", "openUrlInCustomTab: "+url)
        val uri = Uri.parse(url)
        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setStartAnimations(context,android.R.anim.fade_in, android.R.anim.fade_out)
        intentBuilder.setExitAnimations (context, android. R.anim. fade_in,android.R.anim. fade_out)
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        customTabsIntent.launchUrl(context, uri)
    }catch (e:Exception){

    }
}
fun View.implementSpringAnimationTrait() {
    val scaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 0.90f)
    val scaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 0.90f)

    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                scaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                scaleXAnim.start()

                scaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                scaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                scaleYAnim.start()

            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                scaleXAnim.cancel()
                scaleYAnim.cancel()
                val reverseScaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 1f)
                reverseScaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                reverseScaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                reverseScaleXAnim.start()

                val reverseScaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 1f)
                reverseScaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                reverseScaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                reverseScaleYAnim.start()


            }
        }

        false
    }
}
@SuppressLint("SuspiciousIndentation")
fun farsi(num: String): String {
    var j=0
    var lowNum=StringBuilder()
    var highPrice=num.length
    val chars = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val stringBuilder = StringBuilder()
    for (i in 0 until num.length) {
        stringBuilder.append(chars[num[i].code - 48])
        j++
        if (num.length < 6) {
            lowNum = stringBuilder
        } else {
            if (i == 0 && num.length > 6 && highPrice < 8) {
                stringBuilder.append(",")
                j = 0
            }
            if (i == 1 && highPrice == 8 && j == 2) {
                stringBuilder.append(",")
                j = 0
                highPrice--
            }
            if (j == 3 && i < num.length - 1) {
                stringBuilder.append(",")
                j = 0
            }


        }
    }
    if (num.length<6)
        return lowNum.toString()
    else
    return stringBuilder.toString()
}