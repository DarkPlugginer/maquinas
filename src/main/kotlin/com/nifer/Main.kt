package com.nifer

import com.nifer.commands.MaquinasCommand
import com.nifer.listener.PlayerEvents
import net.milkbowl.vault.economy.Economy
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    companion object {
        var instance: Main? = null
        var econ: Economy? = null
    }

    override fun onEnable() {
        if(!setupEconomy()) {
            System.out.println("Erro: Vault nao encontrado!")
        }

        Var()
        instance = this
        val craftSever: CraftServer = server as CraftServer
        craftSever.commandMap.register("maquinas", MaquinasCommand("maquinas"))
        server.pluginManager.registerEvents(PlayerEvents, this)

        super.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
    }

    fun setupEconomy() : Boolean {

        if(server.pluginManager.getPlugin("Vault") == null)
            return false

        val rsp = server.servicesManager.getRegistration<Economy>(Economy::class.java) ?: return false
        econ = rsp.provider
        return econ != null
    }
}