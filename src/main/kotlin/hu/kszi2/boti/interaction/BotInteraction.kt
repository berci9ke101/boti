package hu.kszi2.boti.interaction

import com.jessecorbett.diskord.bot.BotBase
import com.jessecorbett.diskord.bot.EventDispatcherWithContext

fun EventDispatcherWithContext.initBotInteraction(base: BotBase, vararg interactionContexts: BotInteraction) {
    interactionContexts.forEach { it.initBotInteraction(base, this@initBotInteraction) }
}

abstract class BotInteraction {
    abstract fun initBotInteraction(base: BotBase, eventDispatcherWithContext: EventDispatcherWithContext)
}