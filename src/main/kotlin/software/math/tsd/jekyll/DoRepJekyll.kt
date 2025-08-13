// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll

import com.mathswe.kt.`$`
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.system.exitProcess

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

    if (!src.exists()) {
        printError `$` "Source path does not exist: $srcPath"
        exitProcess(1)
    }

    if (!dst.exists()) {
        println("Creating destination directory: $dstPath")

        dst.createDirectories()
    }

    println("Running $cmd from $srcPath → $dstPath")
}
