// Copyright (c) 2023-2025 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/texsydo/dorep-for-jekyll

package software.math.tsd.jekyll

import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse

data class Img(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

fun Tag.toHtmlString(indentNumber: Int = 0): String {
    val indent = " ".repeat(indentNumber * 2)
    val attributesString = with(attributes) {
        if (isEmpty()) ""
        else map { it.key.toHtmlString(it.value) }
            .joinToString(" ")
            .let { " $it" }
    }

    val contentString = content.getOrElse { "" }

    if (children.isEmpty()) {
        if (isSingle()) {
            return "$indent<$name$attributesString />"
        }
        return """
            |$indent<$name$attributesString>
            |$indent  $contentString
            |$indent</$name>
        """.trimMargin("|")
    }

    val childrenString =
        children
            .joinToString("\n") {
                it.toHtmlString(indentNumber + 1)
            }
    val contentLine =
        if (contentString.isEmpty()) "" else indent + contentString

    return """
        |$indent<$name$attributesString>
        |$contentLine$childrenString
        |$indent</$name>
    """.trimMargin("|")
}

fun Tag.isSingle() = when (this) {
    is Img -> true
    else   -> false
}

enum class Attribute {
    Class,
    Href,
    Target,
    Src,
    Alt,
    AriaLabel,
}

val Attribute.htmlName: String
    get() = name
        .replace(Regex("([a-z])([A-Z])"), "$1-$2")
        .lowercase()

fun Attribute.toHtmlString(values: List<String>) =
    if (values.isEmpty()) ""
    else "$htmlName=\"${values.joinToString(" ")}\""

interface AttributeList {
    val attributes: Map<Attribute, List<String>>
}

sealed interface Tag : AttributeList {
    val name: String get() = javaClass.simpleName.lowercase()
    val children: List<Tag>
    val content: Option<String>
}
