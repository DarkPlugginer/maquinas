package com.nifer.commands

import com.nifer.task.Maquina
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

class MaquinasCommand constructor(name: String) : BukkitCommand(name) {

    override fun execute(p0: CommandSender?, p1: String?, p2: Array<out String>?): Boolean {
        if (p0 !is Player) {
            p0?.sendMessage("Apenas jogadores!")
            return true
        }

        if (p1?.toLowerCase().equals("maquinas")) {
            val player: Player = p0

            val inventory: Inventory = Bukkit.createInventory(player, 27, "§fMáquinas §e §7Loja")

            for (value in Maquina.values()) {
                inventory.setItem(inventory.firstEmpty(), createItem(
                        "§fMaquina de §c§o${value.nome.toUpperCase()}",
                        Arrays.asList(
                                "",
                                "§fGera: §c" + value.itemStack.type.name.toLowerCase(),
                                "§fDelay: §c" + value.tick + " segundos",
                                "§fDuração do combustível: §c" + value.combustivelTime + " §fminutos",
                                "§fMédia de lucro: §c*****/*****",
                                "",
                                "§fPreço: §c" + value.price * 100
                        ),
                        value.bloco
                ))
            }



            inventory.setItem(8, createItem("§8Combustivel para Máquinas", Arrays.asList("", "§7Usado para abastecer Geradores", "§fPreço: §c300.000"), Material.COAL))
            inventory.setItem(26, createItem("§cSair", Arrays.asList("§fClique para sair"), Material.REDSTONE_BLOCK))

            player.openInventory(inventory)
        }

        return false
    }

    private fun createItem(name: String, desc: List<String>, type: Material): ItemStack {
        var stack = ItemStack(type)
        var meta: ItemMeta? = stack.itemMeta
        meta?.displayName = name
        meta?.lore = desc
        stack.itemMeta = meta
        return stack
    }
}


