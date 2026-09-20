package com.hamsterscreate.compat.client

import com.hamsterscreate.compat.config.CompatServerConfig
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.CycleButton
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class CompatConfigScreen(private val parent: Screen) : Screen(
    Component.translatable("config.hamsterscreatecompat.title")
) {
    private lateinit var exitsButton: CycleButton<Boolean>
    private lateinit var rpmBox: EditBox
    private lateinit var stressBox: EditBox

    override fun init() {
        val centerX = width / 2
        exitsButton = addRenderableWidget(
            CycleButton.onOffBuilder(CompatServerConfig.hamsterExitsWheelOnItsOwn())
                .create(
                    centerX - 150,
                    50,
                    300,
                    20,
                    Component.translatable("config.hamsterscreatecompat.exits")
                ) { _, _ -> }
        )

        rpmBox = EditBox(
            font,
            centerX - 150,
            94,
            300,
            20,
            Component.translatable("config.hamsterscreatecompat.rpm")
        )
        rpmBox.setMaxLength(4)
        rpmBox.value = CompatServerConfig.generatedRpm().toString()
        addRenderableWidget(rpmBox)

        stressBox = EditBox(
            font,
            centerX - 150,
            138,
            300,
            20,
            Component.translatable("config.hamsterscreatecompat.stress")
        )
        stressBox.setMaxLength(12)
        stressBox.value = CompatServerConfig.stressCapacityPerRpm().toString()
        addRenderableWidget(stressBox)

        addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE) {
                save()
                minecraft!!.setScreen(parent)
            }.bounds(centerX - 100, height - 28, 200, 20).build()
        )
    }

    private fun save() {
        CompatServerConfig.HAMSTER_EXITS_WHEEL_ON_ITS_OWN.set(exitsButton.value)
        rpmBox.value.toIntOrNull()?.let { value ->
            CompatServerConfig.GENERATED_RPM.set(value.coerceIn(1, 256))
        }
        stressBox.value.toDoubleOrNull()?.let { value ->
            CompatServerConfig.STRESS_CAPACITY_PER_RPM.set(value.coerceIn(0.0, 16384.0))
        }
        CompatServerConfig.SPEC.save()
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(graphics)
        graphics.drawCenteredString(font, title, width / 2, 20, 0xFFFFFF)
        graphics.drawString(
            font,
            Component.translatable("config.hamsterscreatecompat.rpm"),
            width / 2 - 150,
            82,
            0xA0A0A0,
            false
        )
        graphics.drawString(
            font,
            Component.translatable("config.hamsterscreatecompat.stress"),
            width / 2 - 150,
            126,
            0xA0A0A0,
            false
        )
        super.render(graphics, mouseX, mouseY, partialTick)
    }

    override fun onClose() {
        minecraft!!.setScreen(parent)
    }
}
