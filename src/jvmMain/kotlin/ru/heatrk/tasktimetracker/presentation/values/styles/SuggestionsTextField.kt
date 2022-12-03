package ru.heatrk.tasktimetracker.presentation.values.styles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

@Composable
fun SuggestionsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    suggestions: List<String> = emptyList(),
    onDismissRequest: () -> Unit,
    onSuggestionsClick: (Int) -> Unit
) {
    Box(modifier = modifier) {
        var textFieldWidth by remember { mutableStateOf(0) }

        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned { textFieldWidth = it.size.width }
                .onFocusChanged {
                    if (it.isFocused) onValueChange(value)
                    else onDismissRequest()
                }
        )

        DropdownMenu(
            expanded = suggestions.isNotEmpty(),
            onDismissRequest = onDismissRequest,
            focusable = false,
            modifier = Modifier.width(with(LocalDensity.current) { textFieldWidth.toDp() })
        ) {
            suggestions.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = { onSuggestionsClick(index) }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}