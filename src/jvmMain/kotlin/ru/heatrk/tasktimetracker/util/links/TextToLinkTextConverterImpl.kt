package ru.heatrk.tasktimetracker.util.links

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class TextToLinkTextConverterImpl(
    private val defaultDispatcher: CoroutineDispatcher,
    private val linkClickHandler: LinkClickHandler
): TextToLinkTextConverter {

    override suspend fun convert(text: String) = withContext(defaultDispatcher) {
        val linksTextValues = mutableListOf<LinksTextValue>()

        val pattern = Pattern.compile(URL_REGEX)
        val matcher = pattern.matcher(text)

        var linkIndex = 0
        var previousIndex = 0

        while (matcher.find()) {
            if (matcher.start() - previousIndex > 0) {
                linksTextValues.add(
                    LinksTextValue.Text(text = text.substring(previousIndex, matcher.start()))
                )
            }

            val link = matcher.group()

            linksTextValues.add(
                LinksTextValue.Link(
                    text = link,
                    tag = (linkIndex++).toString(),
                    annotation = link
                ) {
                    linkClickHandler.onClick(link)
                }
            )

            previousIndex = matcher.end()
        }

        if (text.length - previousIndex > 0) {
            linksTextValues.add(
                LinksTextValue.Text(text = text.substring(previousIndex, text.length))
            )
        }

        linksTextValues.toImmutableList()
    }

    companion object {
        private const val URL_REGEX =
            "https?://(www\\.)?[-a-zA-Z\\d@:%._+~#=]{1,256}\\.[a-zA-Z\\d()]{1,6}\\b([-a-zA-Z\\d()@:%_+.~#?&/=]*)"
    }
}