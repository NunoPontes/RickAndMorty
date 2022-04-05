package com.nunop.rickandmorty.utils.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nunop.rickandmorty.databinding.CustomErrorBinding
import com.nunop.rickandmorty.utils.toVisibilityGone

class CustomError (context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var binding: CustomErrorBinding =
        CustomErrorBinding.inflate(LayoutInflater.from(context), this, true)

    fun showLoading(show: Boolean) {
        binding.ltMorty.visibility = show.toVisibilityGone()
    }

    fun showErrorGeneric(show: Boolean) {
        binding.ltGenericError.visibility = show.toVisibilityGone()
    }

    fun showErrorNoInternet(show: Boolean) {
        binding.ltNoInternet.visibility = show.toVisibilityGone()
    }
}