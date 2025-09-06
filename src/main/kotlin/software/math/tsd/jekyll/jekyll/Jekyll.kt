// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll.jekyll

import arrow.core.Either
import arrow.core.left
import software.math.tsd.jekyll.*
import java.io.IOException
import java.nio.file.Path

data class JekyllOutput(val dst: Path)

fun JekyllOutput.copyJekyllRootFiles(): Either<String, Unit> = try {
    val jekyllPath = resolveJekyllDirectory()

    copyDirectory(
        jekyllPath,
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
    "jekyll build",
    dst,
    getEnvVars(),
)

private fun resolveJekyllDirectory() = when (resolveAppInstallation()) {
    // Root project
    AppInstallation.Dev  -> getRootAbsPath().resolve("jekyll")

    // dorep-for-jekyll/bin, files are in dorep-for-jekyll/jekyll
    AppInstallation.Prod -> getRootAbsPath().parent.resolve("jekyll")
}

private fun getEnvVars(): Map<String, String> {
    val userHome = System.getProperty("user.home")

    return mapOf(
        "PATH" to "$userHome/gems/bin:${System.getenv("PATH")}",
        "GEM_HOME" to "$userHome/gems",
        "PAGES_REPO_NWO" to "texsydo/dorep-for-jekyll"
    )
}
