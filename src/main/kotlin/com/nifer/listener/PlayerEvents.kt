package com.nifer.listener

import com.nifer.FlyingItems
import com.nifer.Main
import com.nifer.Var
import com.nifer.task.Maquina
import com.nifer.task.MaquinasTask
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList

object PlayerEvents : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

        if (event.inventory.title == "§fMáquinas §e §7Loja") {
            if (event.currentItem == null) return
            if (event.currentItem.type == Material.AIR) return
            if(event.clickedInventory == event.whoClicked.inventory) {
                event.isCancelled = true
                return
            }

            event.isCancelled = true

            val player = event.whoClicked
            if (Var.relacoes.containsKey(event.currentItem.type)) {
                val price = Var.rel[event.currentItem.type.name.toLowerCase()]!!.price

                /*if(event.currentItem.type == Material.SLIME_BLOCK) {
                    price = 1.70000
                } else {
                    price = java.lang.Double.valueOf(event.currentItem.itemMeta.lore[7].replace(">> PREÇO: ", "").replace(",00", ""))
                }*/

                val response: EconomyResponse = Main.econ!!.withdrawPlayer(player.name, price)
                if(response.transactionSuccess()) {
                    player.inventory.addItem(event.currentItem)
                    player.world.playSound(player.location, Sound.LEVEL_UP, 1F, 1F)
                } else {
                    player.sendMessage("§cVocê não possui dinheiro suficiente!")
                    player.world.playSound(player.location, Sound.EXPLODE, 1F, 1F)
                }

            } else {
                if (event.currentItem.type == Material.COAL) {
                    val response: EconomyResponse = Main.econ!!.withdrawPlayer(player.name, 300.000)
                    if(response.transactionSuccess()) {
                        player.inventory.addItem(event.currentItem)
                        player.world.playSound(player.location, Sound.LEVEL_UP, 1F, 1F)
                    } else {
                        player.sendMessage("§cVocê não possui dinheiro suficiente!")
                        player.world.playSound(player.location, Sound.EXPLODE, 1F, 1F)
                    }
                } else if (event.currentItem.type == Material.REDSTONE_BLOCK)
                    player.closeInventory()
            }
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        var player = event.player

        if (event.clickedBlock == null)
            return

        if (player.itemInHand.type == Material.COAL && player.itemInHand.hasItemMeta() && player.itemInHand.itemMeta.displayName == "§8Combustivel para Máquinas") {
            if (Var.rel.containsKey(event.clickedBlock.type.name.toLowerCase())) {
                for (value in Var.task.values) {
                    for (hashMap in value) {
                        if(hashMap.containsKey(event.clickedBlock.location)) {
                            event.isCancelled = true
                            player.sendMessage("§cEste gerador já está funcionando!")
                            return
                        }
                    }
                    break
                }

                if(Var.task.containsKey(player.uniqueId)) {
                    if (Var.task[player.uniqueId]!!.size + 1 == 4) {
                        player.sendMessage("§cVocê já 3 geradores funcionando!")
                        return
                    }
                }

                if (player.itemInHand.amount > 1) {
                    val stack = ItemStack(player.itemInHand.type, (player.itemInHand.amount - 1))
                    val meta = stack.itemMeta
                    meta.displayName = "§8Combustivel para Máquinas"
                    stack.itemMeta = meta
                    player.itemInHand = stack
                } else {
                    player.itemInHand = ItemStack(Material.AIR)
                }

                val maquina = Var.rel[event.clickedBlock.type.name.toLowerCase()]!!

                val bTask: MaquinasTask?
                bTask = MaquinasTask(maquina, getCenter(event.clickedBlock.location.clone()), player.uniqueId, FlyingItems(), (maquina.combustivelTime * 60) / maquina.tick)
                bTask.runTaskTimer(Main.instance, 0, 20 * maquina.tick.toLong())

                val map: HashMap<Location, MaquinasTask?> = HashMap()
                map[event.clickedBlock.location] = bTask

                if(Var.task.containsKey(player.uniqueId)) {
                    val arrayList = Var.task[player.uniqueId]
                    arrayList!!.add(map)
                    Var.task.replace(player.uniqueId, arrayList)
                } else {
                    val list: ArrayList<HashMap<Location, MaquinasTask?>> = ArrayList()
                    list.add(map)
                    Var.task[player.uniqueId] = list
                }


                System.out.println("Task started: " + bTask.taskId)
            }
        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        var player = event.player

        for (value in Var.task.values) {
            for (hashMap in value) {
                if(hashMap.containsKey(event.block.location)) {
                    event.isCancelled = true

                    var task: MaquinasTask? = hashMap[event.block.location]
                    task!!.cancel()
                    player.inventory.addItem(ItemStack(event.block.type))
                    event.block.type = Material.AIR

                    if(value.size == 1)
                        value.clear()
                    else
                        value.remove(hashMap)

                    break
                }
            }
            break
        }

        if (Var.relacoes.containsKey(event.block.type)) {
            event.isCancelled = true
            player.inventory.addItem(ItemStack(event.block.type))
            event.block.type = Material.AIR
        }
    }

    private fun getCenter(loc: Location): Location {
            return Location(loc.world,
                    getRelativeCoord(loc.blockX),
                    getRelativeCoord((loc.blockY)),
                    getRelativeCoord(loc.blockZ))
        }

        private fun getRelativeCoord(i: Int): Double {
            var d = i.toDouble()
            d = if (d < 0) d - .5 else d + .5
            return d
        }
}