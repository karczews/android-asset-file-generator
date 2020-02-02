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

package com.github.utilx.assetsjournalist

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register


/**
 * Registers a [Task] with the given [name] and [type], configures it with the given [configuration] action,
 * and adds it to this project tasks container.
 */
inline fun <reified type : Task> Project.registerTask(name: String, noinline configuration: type.() -> Unit) =
    tasks.register(name, type::class, configuration)

val Project.androidAssetsJournalist: AssetFileGeneratorConfig
    get() = extensions.getByType()