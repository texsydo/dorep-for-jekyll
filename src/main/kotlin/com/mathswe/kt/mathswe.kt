// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-jekyll

package com.mathswe.kt

infix fun <X, Y> X.`---`(f: (X) -> Y): Y = f(this)

infix fun <X, Y, Z> ((Y) -> Z).`o`(g: (X) -> Y): (X) -> Z = { this(g(it)) }

infix fun <X, Y> ((X) -> Y).`$`(x: X): Y = this(x)
