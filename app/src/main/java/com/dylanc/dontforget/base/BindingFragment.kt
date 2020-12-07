package com.dylanc.dontforget.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BindingFragment<T : ViewBinding> : Fragment() {

  protected lateinit var binding: T

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = onCreateBinding(inflater, container, savedInstanceState)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    onBindingCreated(binding,savedInstanceState)
  }

  abstract fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): T

  abstract fun onBindingCreated(binding: T, savedInstanceState: Bundle?)

}