package com.elfennani.aniwatch.ui.screens.status.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun RangeTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    max: Int = 100,
    min: Int = 0,
    increment:Int = 1,
) {
    val textFieldStyle = AppTheme.typography.titleLarge
        .copy(
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            color = AppTheme.colorScheme.onBackground
        )

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
        verticalAlignment = Alignment.Bottom
    ) {
        BasicTextField(
            modifier = modifier.weight(1f),
            value = value,
            onValueChange = {
                if (it.isEmpty() || it.toIntOrNull() in 0..max)
                    onValueChange(it)
            },
            textStyle = textFieldStyle,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            BorderStroke(
                                1.dp,
                                AppTheme.colorScheme.onBackground.copy(alpha = 0.12f)
                            ), shape = RoundedCornerShape(4.dp)
                        )
                        .padding(AppTheme.sizes.small)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "0",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .matchParentSize()
                                .alpha(0.16f),
                            style = textFieldStyle
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val newValue = (value.toIntOrNull() ?: 0) - increment
                                onValueChange(newValue.toString())
                            },
                            enabled = (value.toIntOrNull() ?: 0) > min
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            it()
                        }
                        IconButton(
                            onClick = {
                                val newValue = (value.toIntOrNull() ?: 0) + increment
                                onValueChange(newValue.toString())
                            },
                            enabled = (value.toIntOrNull() ?: 0) < max,
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        )

        Text(
            text = "/ $max",
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colorScheme.onSecondary,
            softWrap = false
        )
    }
}