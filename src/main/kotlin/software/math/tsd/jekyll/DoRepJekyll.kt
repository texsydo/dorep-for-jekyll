// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll

import com.mathswe.kt.`$`
import software.math.tsd.jekyll.jekyll.JekyllOutput
import software.math.tsd.jekyll.jekyll.copyJekyllRootFiles
import software.math.tsd.jekyll.jekyll.copyUserProject
import software.math.tsd.jekyll.jekyll.jekyllBuild
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.system.exitProcess

data class DoRepOps(
    val src: Path,
    val dst: Path,
)

fun newDoRepOpsOrExit(src: Path, dst: Path, cmd: String): DoRepOps {
    if (!src.exists()) {
        printError `$` "Source path does not exist: $src"
        exitProcess(1)
    }

    if (cmd != "build") {
        printError `$` "Commands supported can only be `build`, passed: $cmd"
        exitProcess(1)
    }

    return DoRepOps(src, dst)
}

fun DoRepOps.init() {
    if (!dst.exists()) {
        println("Creating destination directory: $dst")

        dst.createDirectories()

        println("📂 Create destination directory: ${dst.absolute()}")
    }

    println("✔ Initialize Jekyll build directory at ${dst.absolute()}")
}

fun DoRepOps.clean() {
    deleteDirectoryRecursively(dst)

    println("🗑️ Clean Jekyll build directory at ${dst.absolute()}")
}

fun DoRepOps.build() {
    val output = JekyllOutput(dst)

    output
        .copyJekyllRootFiles()
        .onLeft(handleError `$` "Failed to copy Jekyll root files.")
        .getOrNull() ?: return

    println("📂 Copy Jekyll root files to ${dst.absolute()}")

    output
        .copyUserProject(src)
        .onLeft(handleError `$` "Failed to copy user project.")
        .getOrNull() ?: return

    println("📂 Copy user project to ${dst.absolute()}")

    output
        .jekyllBuild()
        .onLeft(handleError `$` "Failed to build Jekyll static site.")
        .onRight(::println)
        .getOrNull() ?: return

    println("✔ Build Jekyll static site at ${dst.absolute()}")
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printError `$` "DocRepJekyll received no arguments to run."
        exitProcess(1)
    }
    if (args.size < 3) {
        printError `$` "Usage: <cmd> <src_path> <dst_path>"
        exitProcess(1)
    }

    val arg: (Int) -> String = { args.getOrElse(it) { "" } }
    val cmd = arg(0)
    val srcPath = arg(1)
    val dstPath = arg(2)
    val src = Path(srcPath)
    val dst = Path(dstPath)
    val ops = newDoRepOpsOrExit(src, dst, cmd)

    with(ops) {
        init()
        clean()
        build()
    }
}
