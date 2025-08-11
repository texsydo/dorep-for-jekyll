// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-jekyll

package software.math.tsd.jekyll

import com.mathswe.kt.`$`

object Error {
    var failed: Boolean = false
        private set

    fun handle(errorMsg: String): (String) -> Unit = { cause ->
        print("$errorMsg: $cause")
    }

    fun print(errorMsg: String) {
        failed = true
        println("❌ $errorMsg")
    }
}

val handleError: (String) -> (String) -> Unit = { Error::handle `$` it }
val printError: (String) -> Unit = { Error::print `$` it }
