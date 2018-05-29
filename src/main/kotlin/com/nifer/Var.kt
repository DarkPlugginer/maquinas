package com.nifer

import com.nifer.task.Maquina
import com.nifer.task.MaquinasTask
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Var {

    companion object {
        val task: HashMap<UUID, ArrayList<HashMap<Location, MaquinasTask?>>> = HashMap()

        val relacoes: HashMap<Material, Double> = HashMap()
        val rel: HashMap<String, Maquina> = HashMap()
    }

    init {
        for (value in Maquina.values()) {
            relacoes[value.bloco] = value.price
        }

        rel["brick"] = Maquina.NORMAL
        rel["iron_block"] = Maquina.EMPRESARIAL
        rel["gold_block"] = Maquina.LORD
        rel["emerald_block"] = Maquina.REI
        rel["slime_block"] = Maquina.NIFER
    }
}