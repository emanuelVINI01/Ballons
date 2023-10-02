package com.emanuelvini.balloons.effect

import com.emanuelvini.balloons.Balloons
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.RegisteredListener
import java.util.function.Consumer
import java.util.function.Predicate


abstract class Effect(
    val type: EffectType,
    val plugin: Balloons
) {

    val listeners = HashMap<String, MutableList<RegisteredListener>>()

    open fun setup() {}

    inline fun <reified T> registerListener(executor: Consumer<T>) {
        val listenerReg = RegisteredListener(
            object : Listener {},
            { _: Listener?, event: Event ->
                if (event is T)
                    executor.accept(event)


            },
            EventPriority.NORMAL,
            plugin,
            false
        )
        for (handlerList in HandlerList.getHandlerLists()) {
            handlerList.register(listenerReg)
        }
    }

    fun runAfter(time : Double, r : Runnable) {
        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, r, (20L * time).toLong())
    }

    inline fun <reified T> registerWithFilterListener(executor: Consumer<T>, filter: Predicate<T>, player: Player) {
        val listenerReg = RegisteredListener(
            object : Listener {},
            { _: Listener?, event: Event ->
                if (event is T)
                    if (filter.test(event)) {
                        executor.accept(event)

                    }
            },
            EventPriority.NORMAL,
            plugin,
            false
        )
        if (listeners[player.name] == null) {
            listeners[player.name] = mutableListOf()
        }
        listeners[player.name]?.add(listenerReg)
        for (handlerList in HandlerList.getHandlerLists()) {
            handlerList.register(listenerReg)
        }
    }

    inline fun <reified T> registerListener(player: Player, executor: Consumer<T>) {
        val listenerReg = RegisteredListener(
            object : Listener {},
            EventExecutor { _: Listener?, event: Event ->
                if (event.javaClass != T::class.java) return@EventExecutor
                executor.accept(event as T)
            },
            EventPriority.NORMAL,
            plugin,
            false
        )
        if (listeners[player.name] == null) {
            listeners[player.name] = mutableListOf()
        }
        listeners[player.name]?.add(listenerReg)
        for (handlerList in HandlerList.getHandlerLists()) {
            handlerList.register(listenerReg)
        }
    }

    private fun unregisterListeners(player: Player) {
        val l = listeners[player.name]
        if (l != null) {
            HandlerList.getHandlerLists().forEach {
                l.forEach { l ->
                    it.unregister(l)
                }
            }
            listeners.remove(player.name)
        }
    }

    abstract fun execute(player: Player)

    fun isActive(player: Player): Boolean {
        return plugin.balloonManager.getActiveBalloons(
            player
        ).any { it.effects.contains(this) }
    }

    open fun onEquipped(player: Player) {}

    open fun onUnequipped(player: Player) {
        unregisterListeners(player)
    }

}