package com.nifer

import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class FlyingItems {

    private var armorstand: ArmorStand? = null
    var location: Location? = null
    var text: String? = null
    private var h: Boolean? = false
    var itemStack: ItemStack? = null
    private var height = -1.3

    fun remove() {
        this.location = null
        this.armorstand!!.remove()
        this.armorstand!!.passenger.remove()
        this.armorstand = null
        this.h = false
        this.height = 0.0
        this.text = null
        this.itemStack = null
    }

    fun teleport(location: Location) {
        if (this.location != null) {
            armorstand!!.teleport(location)
            this.location = location
        }
    }

    fun spawn() {
        if ((!h!!)!!) {
            this.location!!.y = this.location!!.y + this.height
            h = true
        }
        armorstand = this.location!!.world.spawn(this.location, ArmorStand::class.java)
        armorstand!!.setGravity(false)
        armorstand!!.isVisible = false
        val i = this.location!!.world.dropItem(this.location, this.itemStack)
        i.pickupDelay = 2147483647

        i.isCustomNameVisible = true

        object : BukkitRunnable() {
            override fun run() {
                if(text == null){
                    cancel()
                    remove()
                    return
                }

                i.customName = text
            }
        }.runTaskTimer(Main.instance, 0, 20)

        armorstand!!.passenger = i
    }


    fun getHeight(): Double {
        return this.height
    }

    fun setHeight(height: Double) {
        this.height = height - 1.3
        if (this.location != null) {
            this.location!!.y = this.location!!.y + this.height
            h = true
        }
    }
}