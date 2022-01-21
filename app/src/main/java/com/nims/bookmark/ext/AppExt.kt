package com.nims.bookmark.ext

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int) {
    setSupportActionBar(findViewById(toolbarId))
}

fun AppCompatActivity.replaceTitle(@StringRes stringId: Int) {
    supportActionBar?.run { this.title = getString(stringId) }
}

fun AppCompatActivity.replaceLogo(@DrawableRes drawableId: Int) {
    supportActionBar?.run { this.setLogo(drawableId) }
}

fun AppCompatActivity.addBack() {
    supportActionBar?.run { this.setDisplayHomeAsUpEnabled(true) }
}