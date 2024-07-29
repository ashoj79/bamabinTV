package com.bamabin.tv_app.data.local

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

class MenuItem {
    val type: MenuIconType
    val title: String
    val isPrimary: Boolean
    private val defaultIcon: ImageVector?
    private val selectedIcon: ImageVector?
    private val defaultId: Int?
    private val selectedId: Int?

    constructor(title: String, defaultIcon: ImageVector, selectedIcon: ImageVector? = null, isPrimary: Boolean = false){
        this.type = MenuIconType.ICON
        this.title = title
        this.isPrimary = isPrimary
        this.defaultIcon = defaultIcon
        this.selectedIcon = selectedIcon
        this.defaultId = null
        this.selectedId = null
    }

    constructor(title: String, defaultId: Int, selectedId: Int? = null, isPrimary: Boolean = false, type: MenuIconType = MenuIconType.SVG){
        this.type = type
        this.title = title
        this.isPrimary = isPrimary
        this.defaultId = defaultId
        this.selectedId = selectedId
        this.defaultIcon = null
        this.selectedIcon = null
    }

    fun getIcon(isSelected: Boolean): ImageVector {
        if (!isSelected) return defaultIcon!!
        return selectedIcon ?: defaultIcon!!
    }

    fun getImage(isSelected: Boolean = false): Int {
        if (!isSelected) return defaultId!!
        return selectedId ?: defaultId!!
    }
}

enum class MenuIconType {
    ICON, IMAGE, SVG
}