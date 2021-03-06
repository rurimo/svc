/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naver.android.svc.core.screen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

/**
 * you should set dialogListener after you create dialog instance.
 * if your dialog has no interaction set Unit.INSTANCE at "dialogListener" field
 * @author bs.nam@navercorp.com 2017. 11. 22..
 */
@Suppress("PropertyName", "unused")
abstract class SvcDialogFragment<DL : Any> : SafeDialogFragment(), LifecycleOwner, Screen {

    val CLASS_SIMPLE_NAME = javaClass.simpleName
    var TAG: String = CLASS_SIMPLE_NAME

    override val baseViews by lazy { createViews() }
    override val baseControlTower by lazy { createControlTower() }

    override val hostActivity: FragmentActivity?
        get() = activity

    override val screenFragmentManager: FragmentManager?
        get() = fragmentManager

    lateinit var dialogListener: DL

    open val isFullScreenSupport = false
    open val dialogBackgroundColor = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::dialogListener.isInitialized) {
            dismissAllowingStateLoss()
            return
        }
        if (isFullScreenSupport) {
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (isFullScreenSupport) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(dialogBackgroundColor))
        }

        baseViews.rootView.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return baseViews.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initialize SVC
        initializeSVC(this, baseViews, baseControlTower)
        lifecycle.addObserver(baseViews)
        lifecycle.addObserver(baseControlTower)
        baseViews.changeIsFirstOnCreateFalse()
        baseControlTower.changeIsFirstOnCreateFalse()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(baseControlTower)
        lifecycle.removeObserver(baseViews)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        baseViews.rootView = LayoutInflater.from(context).inflate(baseViews.layoutResId, null) as ViewGroup
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                if (!onBackPressed()) {
                    dismissAllowingStateLoss()
                }
                true
            } else {
                false
            }
        }
        return dialog
    }

    open fun onBackPressed(): Boolean {
        if (baseControlTower.onBackPressed() || baseViews.onBackPressed()) {
            return true
        }
        return false
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override val isActive: Boolean
        get() = hostActivity != null && context != null && isAdded && !isRemoving && !isDetached
}
