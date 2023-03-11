package com.example.lightswitch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lightswitch.ui.theme.LightSwitchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightSwitchTheme {
                val lightOn = remember {
                    mutableStateOf(true)
                }
                val topPadding by animateDpAsState(
                    targetValue = if (lightOn.value) 20.dp else 0.dp,
                    animationSpec = tween(durationMillis = 300)
                )
                val bottomPadding by animateDpAsState(
                    targetValue = if (lightOn.value) 0.dp else 20.dp,
                    animationSpec = tween(durationMillis = 300)
                )
                val lightPosition by animateFloatAsState(
                    targetValue = if (lightOn.value) -.6f else -.8f,
                    animationSpec = tween(durationMillis = 300)
                )

                val gradient = listOf(
                    Color(0xFF696969),
                    Color(0xFF484848),
                    Color(0xFF3D3D3D)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0XFF383838)),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .size(300.dp)
                            .padding(24.dp),
                        shape = RoundedCornerShape(40.dp),
                        color = Color(0XFF4E4E4E),
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(30.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    lightOn.value = !lightOn.value
                                },
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0XFF2D2D2D),
                            border = BorderStroke(width = 2.dp, color = Color.Black),
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Crossfade(
                                    targetState = lightOn.value,
                                    animationSpec = tween(durationMillis = 300)
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                top = topPadding,
                                                start = 2.dp,
                                                end = 2.dp,
                                                bottom = bottomPadding
                                            )
                                            .advancedShadow(
                                                color = Color(0xFF696969),
                                                shadowBlurRadius = 15.dp,
                                                cornersRadius = 20.dp
                                            )
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = if (it) gradient else gradient.reversed(),
                                                    start = Offset(50.0f, 0.0f),
                                                    end = Offset(50.0f, 400.0f),
                                                ),
                                                shape = RoundedCornerShape(20.dp),
                                            ),
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(20.dp),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = Color(0xFF6F6F6F)
                                        )
                                    ) {

                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .align(
                                            BiasAlignment(
                                                .8f,
                                                lightPosition
                                            )
                                        )
                                        .advancedShadow(
                                            color = if (lightOn.value) Color(0xFF36EA69) else Color(
                                                0xFFE84A36
                                            ),
                                            offsetY = 0.dp,
                                            shadowBlurRadius = 20.dp
                                        )
                                        .clip(CircleShape)
                                        .background(
                                            color = if (lightOn.value) Color(0xFF36EA69) else Color(
                                                0xFFE84A36
                                            ),
                                            shape = CircleShape
                                        ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}