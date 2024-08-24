package com.bamabin.tv_app.ui.widgeta

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.bamabin.tv_app.R


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MoreCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        colors = CardDefaults.colors(containerColor = Color.Transparent),
        border = CardDefaults.border(focusedBorder = Border.None),
        scale = CardDefaults.scale(focusedScale = 1.25f),
        modifier = modifier
            .padding(end = 16.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .width(120.dp)
            .height(250.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(color = Color(0xFF5F5E5E), shape = RoundedCornerShape(8.dp))
                .border(
                    width = if (isFocused) 2.dp else 0.dp,
                    color = if (isFocused) Color.White else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .background(
                        color = Color(0xFF2A94FF),
                        shape = CircleShape
                    )
                    .padding(all = 16.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "مشاهده بیشتر",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = TextUnit(
                    12f,
                    TextUnitType.Sp
                ),
                color = if (isFocused) Color.White else Color.White.copy(alpha = 0.6f)
            ),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}