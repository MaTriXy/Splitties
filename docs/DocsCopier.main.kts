#!/usr/bin/env kotlin

/*
 * Copyright 2021 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Repository("https://repo.maven.apache.org/maven2/")
//@file:Repository("https://oss.sonatype.org/content/repositories/snapshots")
//@file:Repository("file:///Users/louiscad/.m2/repository")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")

import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Paths

val projectDir: File = Paths.get("").toFile().let { currentDir: File ->
    when (val dirName = currentDir.absoluteFile.name) {
        "docs" -> currentDir.absoluteFile.parentFile
        "Splitties" -> currentDir
        else -> currentDir.also {
            System.err.println("Warning: unexpected name for the current dir:")
            System.err.println("dir name: $dirName")
            System.err.println("full path: $currentDir")
        }
    }
}

val docsDir = projectDir.resolve("docs")

//language=RegExp
val linksRegex = """\[(?<text>.+)\]\((?<url>[^ ]+)(?: \"(?<title>.+)\")?\)""".toRegex()
val gitMainBranchUrl = "https://github.com/LouisCAD/Splitties/tree/main"

suspend fun readTextWithAdaptationForMkDocs(sourceFile: File): String = Dispatchers.Default {
    Dispatchers.IO {
        sourceFile.readText()
    }.replace(linksRegex) withRelativeLinksTranslated@{ matchResult ->
        val groups = matchResult.groups
        val urlGroup = groups[2]!!
        val url = urlGroup.value.trim()
        if ("://" in url || url.startsWith('#')) {
            return@withRelativeLinksTranslated matchResult.value
        }
        if (url.substringBeforeLast('#').endsWith(".md")) {
            return@withRelativeLinksTranslated matchResult.value
        }
        if ('.' !in url.substringAfterLast('/', ".")) {
            return@withRelativeLinksTranslated matchResult.value
        }
        val sourceDir = sourceFile.parentFile ?: projectDir
        val newPath = sourceDir.relativeTo(projectDir).resolve(url).normalize()
        val replacement = "$gitMainBranchUrl/$newPath"
        val offset = -matchResult.range.first
        val rangeToReplaceInMatch = (urlGroup.range.first + offset)..(urlGroup.range.last + offset)
        matchResult.value.replaceRange(rangeToReplaceInMatch, replacement)
    }
}

val namesOfTopLevelMarkdownFiles = listOf(
    "README.md", "CHANGELOG.md", "Comparison_with_anko.md"
)

runBlocking(Dispatchers.Default) {
    namesOfTopLevelMarkdownFiles.forEach { fileName ->
        launch {
            docsDir.resolve(fileName).writeText(
                readTextWithAdaptationForMkDocs(projectDir.resolve(fileName))
            )
        }
    }
    projectDir.resolve("modules").let readmeFilesAdaptationAndCopy@{ modulesDir ->
        val dirsWithReadme: List<File> = Dispatchers.IO {
            modulesDir.listFiles { file ->
                file.isDirectory && file.resolve("README.md").exists()
            }!!.asList()
        }
        val destinationDir = docsDir.resolve("modules")
        dirsWithReadme.forEach { dir ->
            Dispatchers.IO {
                dir.list()
            }?.forEach { fileName ->
                if (fileName.endsWith(".md")) launch {
                    val targetFile = destinationDir.resolve(dir.name).resolve(fileName)
                    targetFile.writeText(
                        readTextWithAdaptationForMkDocs(sourceFile = dir.resolve(fileName))
                    )
                }
            }
        }
    }
}
