package com.hamsterscreate.compat.interaction

import com.hamsterscreate.compat.config.CompatServerConfig
import com.hamsterscreate.compat.logic.WheelInteraction
import com.starfish_studios.hamsters.block.HamsterWheelBlock
import com.starfish_studios.hamsters.entity.Hamster
import com.starfish_studios.hamsters.entity.SeatEntity
import com.starfish_studios.hamsters.item.HamsterItem
import com.starfish_studios.hamsters.registry.HamstersEntityType
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

object HamsterWheelInteractions {
    @JvmStatic
    fun handleUse(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand
    ): InteractionResult? {
        val wheel = state.block as? HamsterWheelBlock ?: return null
        if (!level.mayInteract(player, pos) || !wheel.isMountable(state)) {
            return null
        }
        val stack = player.getItemInHand(hand)
        val occupied = HamsterWheelBlock.isOccupied(level, pos)
        val holdingHamster = stack.item is HamsterItem

        if (WheelInteraction.canInsertFromHand(holdingHamster, occupied)) {
            if (!level.isClientSide) {
                insertHamster(level, pos, player, hand, stack)
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }

        if (WheelInteraction.canExtractToHand(occupied, stack.isEmpty)) {
            if (!level.isClientSide) {
                extractHamster(level, pos, player, hand)
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }

        return null
    }

    @JvmStatic
    fun keepHamsterInWheel(hamster: Hamster) {
        val seated = hamster.isPassenger && hamster.vehicle is SeatEntity
        val next = WheelInteraction.keepRunningTicks(
            CompatServerConfig.hamsterExitsWheelOnItsOwn(),
            seated,
            hamster.waitTimeWhenRunningTicks
        )
        if (next != hamster.waitTimeWhenRunningTicks) {
            hamster.waitTimeWhenRunningTicks = next
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun insertHamster(level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack) {
        val type = HamstersEntityType.HAMSTER.get() as net.minecraft.world.entity.EntityType<Hamster>
        val hamster = type.create(level) ?: return
        if (stack.hasCustomHoverName()) {
            hamster.customName = stack.hoverName
        }
        val tag = stack.tag
        if (tag != null) {
            hamster.load(tag)
        }
        hamster.moveTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, player.yRot, 0.0f)
        hamster.waitTimeWhenRunningTicks = hamster.random.nextInt(300) + 100
        hamster.waitTimeBeforeRunTicks = 0
        level.addFreshEntity(hamster)
        HamsterWheelBlock.sitDown(level, pos, hamster)
        hamster.playSound(SoundEvents.CHICKEN_EGG)
        if (!player.isCreative) {
            player.setItemInHand(hand, ItemStack.EMPTY)
        }
    }

    private fun extractHamster(level: Level, pos: BlockPos, player: Player, hand: InteractionHand) {
        val seats = level.getEntitiesOfClass(SeatEntity::class.java, AABB(pos))
        val seat = seats.firstOrNull() ?: return
        val hamster = seat.firstPassenger as? Hamster ?: return
        val output = hamster.caughtItemStack
        hamster.saveWithoutId(output.orCreateTag)
        hamster.customName?.let { output.hoverName = it }
        hamster.stopRiding()
        hamster.discard()
        seats.forEach { it.discard() }
        if (player.getItemInHand(hand).isEmpty) {
            player.setItemInHand(hand, output)
        } else if (!player.inventory.add(output)) {
            val dropped = ItemEntity(level, player.x, player.y + 0.5, player.z, output)
            dropped.setPickUpDelay(0)
            level.addFreshEntity(dropped)
        }
        level.updateNeighbourForOutputSignal(pos, level.getBlockState(pos).block)
    }
}
