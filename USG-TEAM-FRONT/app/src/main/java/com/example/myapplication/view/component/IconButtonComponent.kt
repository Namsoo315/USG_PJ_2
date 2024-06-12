package com.example.myapplication.view.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

object IconButtonComponent {
    @Composable
    fun IconButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: @Composable () -> Unit
    ) {
//        val combinedModifier = Modifier
//            .clickable(
//                enabled = enabled,
//                interactionSource = interactionSource,
//                indication = null,
//                onClick = onClick
//            )
//            .then(modifier)
        content()
    }
}