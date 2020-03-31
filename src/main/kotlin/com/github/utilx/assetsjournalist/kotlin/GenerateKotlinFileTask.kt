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

package com.github.utilx.assetsjournalist.kotlin

import com.github.utilx.assetsjournalist.common.FileConstantsFactory
import com.github.utilx.assetsjournalist.common.buildStringTransformerUsing
import com.github.utilx.assetsjournalist.common.listAssets
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property


open class GenerateKotlinFileTask @javax.inject.Inject constructor(objects: ObjectFactory) : DefaultTask() {

    @get:OutputDirectory
    val outputSrcDir = objects.directoryProperty()

    @get:Input
    var className = objects.property<String>().value("AssetFileKt")

    @get:Input
    var packageName = objects.property<String>().value("")

    @get:Input
    var constNamePrefix = objects.property<String>().value("")

    @get:Input
    var constValuePrefix = objects.property<String>().value("")

    @get:Input
    var constValueReplacementExpressions = objects.listProperty<Map<String, String>>()

    @get:InputFiles
    val assetFiles = objects.fileCollection()

    @TaskAction
    fun generateKotlinFile() {
        val fileConstantsFactory = FileConstantsFactory(
            constValuePrefix = constValuePrefix.get(),
            constValueTransformer = buildStringTransformerUsing(
                constValueReplacementExpressions.get()
            ),
            constNamePrefix = constNamePrefix.get()
        )

        val properties = assetFiles.listAssets(project)
            .asSequence()
            .map(fileConstantsFactory::toConstNameValuePair)
            // remove duplicate entries
            .distinct()
            .map {
                PropertySpec.builder(it.name, String::class)
                    .addModifiers(KModifier.CONST)
                    .initializer("\"${it.value}\"")
                    .build()
            }
            .asIterable()

        // create type spec for object and include all properties
        val objectSpec = TypeSpec.objectBuilder(className.get())
            .addProperties(properties)
            .addKdoc(
                "This class is generated using android-assets-journalist gradle plugin. \n" +
                    "Do not modify this class because all changes will be overwritten"
            )
            .build()

        // generating kt file
        FileSpec.builder(packageName.get(), className.get())
            .addType(objectSpec)
            .build()
            .writeTo(outputSrcDir.asFile.get())

        logger.lifecycle("generating asset kotlin file ${packageName.get()}.${className.get()} in ${outputSrcDir.asFile.get()}")
    }

    fun configureUsing(config: KotlinFileConfig) {
        this.constNamePrefix.set(config.constNamePrefix)
        this.constValuePrefix.set(config.constValuePrefix)
        this.packageName.set(config.packageName)
        this.className.set(config.className)

        this.constValueReplacementExpressions.set(config.replaceInAssetsPath)
    }
}
