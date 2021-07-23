package com.growingrubies.vegpatch.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

//@BindingAdapter("android:mapImages")
//fun mapImages(plantList: ArrayList<String>, imgView: ImageView) {
//    for (i in plantList) {
//        val drawable = when (i) {
//            "Potatoes" -> "@drawable/_01_potatos"
//            else -> "@drawable/_01_potatos"
//        }
//        imgView.srcCompat = drawable
//    }
//
//    //Return list of strings pointing to drawables or single one???
//}
//object BindingAdapters {
//    @Suppress("UNCHECKED_CAST")
//    @BindingAdapter("android:liveData")
//    @JvmStatic
//    fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
//        items?.value?.let { itemList ->
//            (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
//                clear()
//                addData(itemList)
//            }
//        }
//    }
//}
