package com.nunop.rickandmorty.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

abstract class BaseFragment : Fragment() {

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }
}