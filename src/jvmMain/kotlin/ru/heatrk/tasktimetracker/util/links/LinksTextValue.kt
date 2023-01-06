package ru.heatrk.tasktimetracker.util.links

sealed interface LinksTextValue {
    data class Link(
        val text: String,
        val tag: String,
        val annotation: String,
        val onClick: (() -> Unit)? = null
    ): LinksTextValue

    data class Text(val text: String): LinksTextValue
}