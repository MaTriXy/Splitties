/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */
package splitties.views.dsl.constraintlayout

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.view
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// ConstraintLayout

inline fun Context.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ConstraintLayout.() -> Unit = {}
): ConstraintLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::ConstraintLayout, id, theme, initView)
}

inline fun View.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ConstraintLayout.() -> Unit = {}
): ConstraintLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.constraintLayout(id, theme, initView)
}

inline fun Ui.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ConstraintLayout.() -> Unit = {}
): ConstraintLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return ctx.constraintLayout(id, theme, initView)
}
