package ru.heatrk.tasktimetracker.util.links

import kotlinx.collections.immutable.ImmutableList

interface TextToLinkTextConverter {
    suspend fun convert(text: String): ImmutableList<LinksTextValue>
}