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

package com.louiscad.splittiessample

import splitties.init.AppCtxInitProvider
import splitties.init.appCtx
import splitties.initprovider.InitProvider

/**
 * Just present here and in the manifest to call [AppCtxInitProvider] implementation
 * for the second process of this app so [appCtx] is initialized.
 * @see AppCtxInitProvider
 */
class SecondProcessInitProvider : InitProvider() {
    override fun onCreate() = true.also { _ -> AppInit }
}
