package com.growingrubies.vegpatch.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.growingrubies.vegpatch.R
import timber.log.Timber

@BindingAdapter("mapImages")
fun mapImages(imgView: ImageView, plantName: String) {
    Timber.i("mapImages called. plantName: $plantName")
    when (plantName) {
        "Potato" -> imgView.setImageResource(R.drawable._01_potatos)
        "Tomato" -> imgView.setImageResource(R.drawable._00_pumpkin)
        else -> imgView.setImageResource(R.drawable._99_basil)
    }
}

//TODO: Tidy up here...
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
