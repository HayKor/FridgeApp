package com.haykor.fridge.core.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun FridgeAppSwipeableCard(
    onItemDelete: () -> Unit,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    backgroundColor: Color = Color.Red,
    dismissThreshold: Dp = 100.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f
    )

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val offsetX = remember { Animatable(0f) }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clip(MaterialTheme.shapes.medium)
                .background(backgroundColor)
        ) {
            Icon(
                Icons.Outlined.Delete,
                null,
                modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        Card(
            colors = colors,
            modifier = Modifier
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Press -> isPressed = true
                                PointerEventType.Release -> isPressed = false
                            }
                        }
                    }
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val targetValue =
                                (offsetX.value + delta / density.density).coerceAtMost(0f)
                            offsetX.snapTo(targetValue)
                        }
                    },
                    onDragStopped = { velocity ->
                        if (abs(offsetX.value) > with(density) { dismissThreshold.toPx() }) {
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = -2000f,
                                    animationSpec = tween(300)
                                )
                                onItemDelete()
                            }
                        } else {
                            scope.launch { offsetX.animateTo(0f, animationSpec = tween(200)) }
                        }
                    }
                )
        ) {
            content()
        }
    }
}