// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll.fs

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import kotlin.io.path.name

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
