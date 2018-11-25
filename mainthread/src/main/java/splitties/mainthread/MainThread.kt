/*
 * Copyright (c) 2017. Louis Cognault Ayeva Derman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")

package splitties.mainthread

import android.os.Looper

/** This main looper cache avoids synchronization overhead when accessed repeatedly. */
@JvmField
val mainLooper: Looper = Looper.getMainLooper()!!
@JvmField
val mainThread: Thread = mainLooper.thread

val isMainThread inline get() = mainThread === Thread.currentThread()

/**
 * Passes if run on the [mainThread] (aka. UI thread), throws an [IllegalStateException] otherwise.
 */
inline fun checkMainThread() = check(isMainThread) {
    "This should ONLY be called on the main thread! Current: ${Thread.currentThread()}"
}

/**
 * Passes if not run on the [mainThread] (aka. UI thread), throws an [IllegalStateException]
 * otherwise.
 */
inline fun checkNotMainThread() = check(!isMainThread) {
    "This should NEVER be called on the main thread! Current: ${Thread.currentThread()}"
}
