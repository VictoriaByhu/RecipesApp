package com.victoria.recipes.ui

import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

fun Modifier.selectableSegment(selected: Boolean, onClick: () -> Unit): Modifier =
    selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
