package com.nims.bookmark.ext

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nims.bookmark.library.ARTProgressDialogFragment

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int) {
    setSupportActionBar(findViewById(toolbarId))
}

fun AppCompatActivity.replaceTitle(@StringRes stringId: Int) {
    supportActionBar?.run { this.title = getString(stringId) }
}

fun AppCompatActivity.replaceTitle(string: String) {
    supportActionBar?.run { this.title = string }
}

fun AppCompatActivity.replaceLogo(@DrawableRes drawableId: Int) {
    supportActionBar?.run { this.setLogo(drawableId) }
}

fun AppCompatActivity.addBack() {
    supportActionBar?.run { this.setDisplayHomeAsUpEnabled(true) }
}

fun AppCompatActivity.showProgress() {
    if (supportFragmentManager.findFragmentByTag(ARTProgressDialogFragment.TAG) == null) {
        val progressDialog = ARTProgressDialogFragment.newInstance()
        runOnUiThread {
            progressDialog.show(supportFragmentManager, ARTProgressDialogFragment.TAG)
        }
    }
}

fun AppCompatActivity.hideProgress() {
    val progressDialog =
        supportFragmentManager.findFragmentByTag(ARTProgressDialogFragment.TAG) as? ARTProgressDialogFragment
    progressDialog?.dismissAllowingStateLoss()
}

fun Fragment.showProgress() {
    if (childFragmentManager.findFragmentByTag(ARTProgressDialogFragment.TAG) == null) {
        val progressDialog = ARTProgressDialogFragment.newInstance()
        activity?.runOnUiThread {
            progressDialog.show(childFragmentManager, ARTProgressDialogFragment.TAG)
        }
    }
}

fun Fragment.hideProgress() {
    val progressDialog =
        childFragmentManager.findFragmentByTag(ARTProgressDialogFragment.TAG) as? ARTProgressDialogFragment
    progressDialog?.dismissAllowingStateLoss()
}