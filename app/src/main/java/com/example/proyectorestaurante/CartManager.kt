package com.example.proyectorestaurante

object CartManager {

    private val items = mutableListOf<ProductoUI>()

    fun getItems(): List<ProductoUI> = items

    fun add(producto: ProductoUI) {
        val idx = items.indexOfFirst { it.id == producto.id }

        if (idx >= 0) {
            items[idx].cantidad += producto.cantidad
        } else {
            items.add(producto)
        }
    }

    fun increase(id: String) {
        items.find { it.id == id }?.let { it.cantidad += 1 }
    }

    fun decrease(id: String) {
        val item = items.find { it.id == id } ?: return
        item.cantidad -= 1
        if (item.cantidad <= 0) items.remove(item)
    }

    fun total(): Double {
        return items.sumOf { it.precio * it.cantidad }
    }

    fun clear() {
        items.clear()
    }
    fun countTotalItems(): Int {
        return items.sumOf { it.cantidad }
    }
    fun remove(id: String) {
        val idx = items.indexOfFirst { it.id == id }
        if (idx >= 0) items.removeAt(idx)
    }
}