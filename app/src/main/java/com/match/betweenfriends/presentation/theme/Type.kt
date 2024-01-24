package com.match.betweenfriends.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.match.betweenfriends.R

val InterFamily = FontFamily(
    listOf(
        Font(R.font.inter_regular),
        Font(R.font.inter_medium, weight = FontWeight.Medium),
        Font(R.font.inter_bold, weight = FontWeight.Bold)
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        color = MainWhite,
        lineHeight = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        color = MainWhite,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        color = MainWhite,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        textAlign = TextAlign.Center,
        color = MainRed,
        lineHeight = 55.sp,
        drawStyle = Stroke(
            miter = 10f,
            width = 5f,
            join = StrokeJoin.Round
        )
    ),
    labelSmall = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        color = MainWhite,
        lineHeight = 19.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = MainWhite,
        lineHeight = 38.sp,
        textAlign = TextAlign.Center
    )
)
