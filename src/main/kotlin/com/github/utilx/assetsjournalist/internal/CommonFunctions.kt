/*
 *  Copyright (c) 2019-present, Android Assets Journalist Contributors.
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is
 *  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 *  the License for the specific language governing permissions and limitations under the License.
 */

package com.github.utilx.assetsjournalist.internal

import com.android.build.gradle.api.AndroidSourceSet

fun AndroidSourceSet.listAssets(): List<String> =
    assets
        .sourceDirectoryTrees
        .flatMap { assetFileTree ->
            val assetBaseDir = assetFileTree.dir
            assetFileTree.asFileTree.files
                .map { it.relativeTo(assetBaseDir) }
                .map { it.invariantSeparatorsPath }
        }
