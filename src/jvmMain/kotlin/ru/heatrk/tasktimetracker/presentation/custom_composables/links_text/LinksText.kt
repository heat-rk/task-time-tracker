package ru.heatrk.tasktimetracker.presentation.custom_composables.links_text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import ru.heatrk.tasktimetracker.presentation.values.styles.ApplicationTheme
import ru.heatrk.tasktimetracker.util.links.LinksTextValue

@Composable
fun LinksText(
    value: List<LinksTextValue>,
    modifier: Modifier = Modifier,
) {
    val annotatedString = createAnnotatedString(value)

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            value.forEach { annotatedStringData ->
                if (annotatedStringData is LinksTextValue.Link) {
                    annotatedString.getStringAnnotations(
                        tag = annotatedStringData.tag,
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        annotatedStringData.onClick?.invoke()
                    }
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun createAnnotatedString(data: List<LinksTextValue>): AnnotatedString {
    return buildAnnotatedString {
        data.forEach { linkTextData ->
            when (linkTextData) {
                is LinksTextValue.Link -> {
                    pushStringAnnotation(
                        tag = linkTextData.tag,
                        annotation = linkTextData.annotation,
                    )

                    withStyle(
                        style = SpanStyle(
                            color = ApplicationTheme.colors.primary,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ) {
                        append(linkTextData.text)
                    }

                    pop()
                }
                is LinksTextValue.Text -> {
                    append(linkTextData.text)
                }
            }
        }
    }
}