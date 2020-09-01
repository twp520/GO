package com.colin.go.fragments

import android.view.View
import com.colin.go.BaseFragment
import com.colin.go.R
import com.colin.go.databinding.CompassFragmentBinding
import com.colin.go.viewmodels.CompassViewModel

class CompassFragment : BaseFragment<CompassFragmentBinding>() {

    companion object {
        fun newInstance() = CompassFragment()
    }

    private lateinit var viewModel: CompassViewModel


    override fun getContentLayoutId(): Int {
        return R.layout.compass_fragment
    }

    override fun initView(contentView: View) {
    }


}