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
    private var canEdit: Boolean = true

    override fun init() {
        val centerX = width / 2
        val values = CompatServerConfig.currentValues()
        canEdit = canEditLocally()

        exitsButton = addRenderableWidget(
            CycleButton.onOffBuilder(values.exits)
                .create(
                    centerX - 150,
                    50,
                    300,
                    20,
                    Component.translatable("config.hamsterscreatecompat.exits")
                ) { _, _ -> }
        )
        exitsButton.active = canEdit

        rpmBox = EditBox(
            font,
            centerX - 150,
            94,
            300,
            20,
            Component.translatable("config.hamsterscreatecompat.rpm")
        )
        rpmBox.setMaxLength(4)
        rpmBox.value = values.rpm.toString()
        rpmBox.setEditable(canEdit)
        rpmBox.active = canEdit
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
        stressBox.value = values.stress.toString()
        stressBox.setEditable(canEdit)
        stressBox.active = canEdit
        addRenderableWidget(stressBox)

        addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE) {
                save()
                minecraft!!.setScreen(parent)
            }.bounds(centerX - 100, height - 28, 200, 20).build()
        )
    }

    private fun canEditLocally(): Boolean {
        val client = minecraft ?: return true
        return client.level == null || client.hasSingleplayerServer()
    }

    private fun save() {
        if (!canEdit) {
            return
        }
        val client = minecraft
        val persistWorld = client != null && client.hasSingleplayerServer()
        val persistDefaults = client == null || client.level == null
        CompatServerConfig.saveValues(
            CompatServerConfig.Values(
                exitsButton.value,
                rpmBox.value.toIntOrNull() ?: CompatServerConfig.generatedRpm(),
                stressBox.value.toDoubleOrNull() ?: CompatServerConfig.stressCapacityPerRpm()
            ),
            persistWorld = persistWorld,
            persistDefaults = persistDefaults
        )
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
        if (!canEdit) {
            graphics.drawCenteredString(
                font,
                Component.translatable("config.hamsterscreatecompat.remote"),
                width / 2,
                height - 48,
                0xFF5555
            )
        }
        super.render(graphics, mouseX, mouseY, partialTick)
    }

    override fun onClose() {
        minecraft!!.setScreen(parent)
    }
}
