// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll.jekyll

import arrow.core.Either
import arrow.core.left
import software.math.tsd.jekyll.copyDirectory
import software.math.tsd.jekyll.runCommand
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path

data class JekyllOutput(val dst: Path)

fun JekyllOutput.copyJekyllRootFiles(): Either<String, Unit> = try {
    copyDirectory(
        Path("jekyll"),
        dst,
    )
}
catch (e: IOException) {
    e.printStackTrace()
    e.message.orEmpty().left()
}

fun JekyllOutput.copyUserProject(
    preBuildDir: Path,
): Either<String, Unit> = try {
    copyDirectory(
        preBuildDir,
        dst,
    )
}
catch (e: IOException) {
    e.printStackTrace()
    e.message.orEmpty().left()
}

fun JekyllOutput.jekyllBuild(): Either<String, String> = runCommand(
    "bundle exec jekyll build",
    dst
)
