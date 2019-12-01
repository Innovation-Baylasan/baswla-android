package org.baylasan.sudanmap.layers

/**
 * Selectable represents a item that can be toggled
 * @Param isSelected indicates whether its selected or not, True if selected and false otherwise
 * @Param item generic object of the selected item
 *
 */
data class Selectable<Item>(var isSelected: Boolean = false, val item: Item) {
    fun toggle() {
        isSelected = !isSelected
    }
}

