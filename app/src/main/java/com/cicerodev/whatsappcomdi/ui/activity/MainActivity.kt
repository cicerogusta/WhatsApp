package com.cicerodev.whatsappcomdi.ui.activity

import androidx.activity.viewModels
import com.cicerodev.whatsappcomdi.databinding.ActivityMainBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {
    override val viewModel: MainActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}



