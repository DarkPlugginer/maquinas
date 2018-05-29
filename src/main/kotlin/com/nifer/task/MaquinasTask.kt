package com.nifer.task

import com.nifer.FlyingItems
import com.nifer.Var
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class MaquinasTask(private var maquina: Maquina, private var loc: Location, private var uniqueId: UUID, val items: FlyingItems, var drops: Int) : BukkitRunnable() {

    init {
        items.itemStack = ItemStack(maquina.itemStack.type)
        items.location = loc.clone().add(0.0, 1.0, 0.0)
        items.text = "."
        items.spawn()
    }

    override fun run() {
        drops--

        if(drops == 0) {
            cancel()
            return
        }

        items.text = "Restante: $drops §a" + if (drops > 1) "Vezes" else "Vez"

        for (value in Maquina.values()) {
            if (value == maquina) {
                loc.world.dropItem(loc, value.itemStack)
                break
            }
        }
    }

    override fun cancel() {
        System.out.println("Task cancelled: $taskId")

        for (value in Var.task.values) {
            for (hashMap in value) {
                if (hashMap.containsKey(this.loc)) {
                    val maquinasTask = hashMap[loc]

                    if (value.size == 1) {
                        value.clear()
                        Var.task.remove(uniqueId)

                        Bukkit.getPlayer(uniqueId).sendMessage("§fTodas as suas máquinas estão sem §ccombustível§f!")
                    } else {
                        Bukkit.getPlayer(uniqueId).sendMessage("Seu gerador de: §c§o" + maquinasTask!!.maquina.nome.toUpperCase() + " §fFicou sem combustível!")
                        value.remove(hashMap)
                    }

                    break
                }
            }
        }

        items.text = null

        super.cancel()
    }

    private fun getCenter(loc: Location): Location {
        return Location(loc.world,
                getRelativeCoord(loc.blockX),
                getRelativeCoord((loc.blockY)),
                getRelativeCoord(loc.blockZ))
    }

    private fun getRelativeCoord(i: Int): Double {
        var d = i.toDouble()
        d = if (d < 0) d - 0.5 else d + 0.5
        return d
    }
}
