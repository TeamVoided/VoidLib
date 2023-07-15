package org.teamvoided.voidlib.wfc.command

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.wfc.WFCRegistries
import org.teamvoided.voidlib.wfc.wave.WaveFunction
import java.util.concurrent.CompletableFuture

class WaveFunctionArgumentType : IdentifierArgumentType() {
    companion object {
        fun waveFunction(): WaveFunctionArgumentType {
            return WaveFunctionArgumentType()
        }

        fun getWaveFunction(context: CommandContext<ServerCommandSource>, name: String): WaveFunction {
            return WFCRegistries.WAVE_FUNCTION[context.getArgument(name, Identifier::class.java)]!!.createWaveFunction()
        }
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        WFCRegistries.WAVE_FUNCTION.keys.forEach {
            builder.suggest(it.toString())
        }
        return builder.buildFuture()
    }
}