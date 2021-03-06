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
package com.naver.android.svc.sample.dialog.action

import com.naver.android.svc.core.views.ActionViews
import com.naver.android.svc.sample.R
import kotlinx.android.synthetic.main.dialog_sample_action.view.*

/**
 * @author bs.nam@navercorp.com
 */
class SampleActionViews : ActionViews<SampleActionViewsAction>() {

    override val layoutResId = R.layout.dialog_sample_action

    override fun onCreated() {
        rootView.btn_click.setOnClickListener {
            viewsAction.onClickButton()
        }
    }
}
