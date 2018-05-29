package com.nifer.task

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class Maquina constructor(val itemStack: ItemStack, val tick: Int, val combustivelTime: Int, val nome: String, val price: Double, val bloco: Material) {

    NORMAL(ItemStack(Material.getMaterial(336), 32), 10, 2, "tijolos", 100.000, Material.BRICK),
    EMPRESARIAL(ItemStack(Material.IRON_INGOT, 64), 60, 3, "ferro", 170.000, Material.IRON_BLOCK),
    LORD(ItemStack(Material.GOLD_INGOT, 6), 60, 5, "ouro", 400.000, Material.GOLD_BLOCK),
    REI(ItemStack(Material.EYE_OF_ENDER, 32), 45, 5, "olho do fim", 700.000, Material.EMERALD_BLOCK),
    NIFER(ItemStack(Material.SLIME_BALL, 5), 60, 6, "slime", 1.700000, Material.SLIME_BLOCK);
}