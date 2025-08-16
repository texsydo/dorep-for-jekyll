// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.left
import arrow.core.right
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.stream.Stream
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.name

enum class AppInstallation {
    Dev,
    Prod,
}

fun getRootAbsPath() = Path("").absolute()

fun resolveAppInstallation(): AppInstallation {
    val rootPath = getRootAbsPath()

    return if (rootPath.name == "bin") AppInstallation.Prod
    else AppInstallation.Dev
}

fun copyDirectory(
    sourceDir: Path,
    targetDir: Path,
    predicate: (Path) -> Boolean = { true },
): Either<String, Unit> {
    val renameIfHiddenFile: (Path) -> Path = {
        if (it.name.startsWith("-."))
            it.parent.resolve(it.name.removePrefix("-"))
        else it
    }

    val copyFileFromWalk: (Stream<Path>) -> Right<Unit> = { stream ->
        stream
            .filter(predicate)
            .forEach { sourcePath ->
                val relativePath = sourceDir.relativize(sourcePath)
                val targetPath = targetDir.resolve(relativePath)

                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath)
                }
                else {
                    val targetFilePath = renameIfHiddenFile(targetPath)

                    Files.copy(
                        sourcePath,
                        targetFilePath,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
        Right(Unit)
    }

    return try {
        Files.walk(sourceDir).use { copyFileFromWalk(it) }
    }
    catch (e: IOException) {
        e.printStackTrace()
        Left("Fail to copy directory $sourceDir into $targetDir: ${e.message}")
    }
}

fun deleteDirectoryRecursively(dir: Path) {
    if (Files.exists(dir)) {
        Files.walk(dir)
            .sorted(Comparator.reverseOrder()) // delete children before parents
            .forEach { path ->
                Files.delete(path)
            }
    }
}

fun runCommand(
    command: String,
    workingDir: Path? = null,
): Either<String, String> {
    val processBuilder = getProcessBuilder(command)

    processBuilder.redirectErrorStream(false)

    if (workingDir != null) {
        processBuilder.directory(workingDir.toFile())
    }

    return try {
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val stdErrReader =
            BufferedReader(InputStreamReader(process.errorStream))
        val output = StringBuilder()
        val stdErr = StringBuilder()
        var line: String?

        // Capture standard output
        while (reader.readLine().also { line = it } != null) {
            output.append(line).append("\n")
        }

        // Capture standard error
        while (stdErrReader.readLine().also { line = it } != null) {
            stdErr.append(line).append("\n")
        }
        val exitCode = process.waitFor()
        if (exitCode == 0) {
            output.toString().right()
        }
        else {
            "Command failed with exit code $exitCode, and error message $stdErr"
                .left()
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        e.message.orEmpty().left()
    }
}

fun getProcessBuilder(command: String): ProcessBuilder {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    return if (os.contains("win")) ProcessBuilder(
        *"cmd /c $command"
            .split("\\s+".toRegex())
            .toTypedArray()
    )
    else ProcessBuilder("sh", "-c", command)
}
