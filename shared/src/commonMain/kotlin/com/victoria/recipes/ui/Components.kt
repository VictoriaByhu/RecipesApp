package com.victoria.recipes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.victoria.recipes.theme.LocalDarkTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.img_bg_kitchen_dark
import recipesapp.shared.generated.resources.img_bg_kitchen_light
import recipesapp.shared.generated.resources.img_bg_waves_dark
import recipesapp.shared.generated.resources.img_bg_waves_light

enum class BackgroundStyle { KITCHEN, WAVES }

@Composable
fun ScreenBackground(
    style: BackgroundStyle,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val dark = LocalDarkTheme.current
    val image = when (style) {
        BackgroundStyle.KITCHEN -> if (dark) Res.drawable.img_bg_kitchen_dark else Res.drawable.img_bg_kitchen_light
        BackgroundStyle.WAVES -> if (dark) Res.drawable.img_bg_waves_dark else Res.drawable.img_bg_waves_light
    }
    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        content()
    }
}

@Composable
fun SoftCard(
    modifier: Modifier = Modifier,
    contentPadding: Dp = 24.dp,
    spacing: Dp = 12.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.extraLarge
    Column(
        modifier = modifier
            .shadow(
                elevation = 18.dp,
                shape = shape,
                ambientColor = cardShadowColor(),
                spotColor = cardShadowColor(),
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.94f))
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = horizontalAlignment,
    ) {
        content()
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: DrawableResource? = null,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(58.dp)
            .shadow(10.dp, MaterialTheme.shapes.medium, ambientColor = cardShadowColor(), spotColor = cardShadowColor()),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(22.dp),
            )
            return@Button
        }
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        if (trailingIcon != null) {
            Spacer(Modifier.width(10.dp))
            Icon(painterResource(trailingIcon), contentDescription = null, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
fun IconBox(
    icon: DrawableResource,
    tint: Color,
    background: Color,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    iconSize: Dp = 18.dp,
) {
    Box(
        modifier = modifier.size(size).clip(MaterialTheme.shapes.small).background(background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(painterResource(icon), contentDescription = null, tint = tint, modifier = Modifier.size(iconSize))
    }
}

@Composable
fun SegmentedSelector(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .selectableSegment(selected) { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }
    }
}

@Composable
fun cardShadowColor(): Color =
    if (LocalDarkTheme.current) Color.Black.copy(alpha = 0.5f) else Color(0xFF8C4D26).copy(alpha = 0.25f)
