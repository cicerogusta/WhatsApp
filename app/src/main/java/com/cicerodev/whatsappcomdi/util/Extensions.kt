package com.cicerodev.whatsappcomdi.util

import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.toast(msg: String?){
    Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
}

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
