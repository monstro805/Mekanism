package mekanism.client.gui;

import java.util.Arrays;
import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.bar.GuiRateBar;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.tab.GuiMatrixTab;
import mekanism.client.gui.element.tab.GuiMatrixTab.MatrixTab;
import mekanism.common.inventory.container.tile.MatrixStatsContainer;
import mekanism.common.tile.TileEntityInductionCasing;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.TextComponentUtil;
import mekanism.common.util.text.Translation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiMatrixStats extends GuiMekanismTile<TileEntityInductionCasing, MatrixStatsContainer> {

    public GuiMatrixStats(MatrixStatsContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    public void init() {
        super.init();
        ResourceLocation resource = getGuiLocation();
        addButton(new GuiMatrixTab(this, tileEntity, MatrixTab.MAIN, resource));
        addButton(new GuiEnergyGauge(() -> tileEntity, GuiEnergyGauge.Type.STANDARD, this, resource, 6, 13));
        addButton(new GuiRateBar(this, new IBarInfoHandler() {
            @Override
            public ITextComponent getTooltip() {
                return TextComponentUtil.build(Translation.of("gui.mekanism.receiving"), ": ", EnergyDisplay.of(tileEntity.getLastInput()), "/t");
            }

            @Override
            public double getLevel() {
                return tileEntity.structure == null ? 0 : tileEntity.getLastInput() / tileEntity.structure.getTransferCap();
            }
        }, resource, 30, 13));
        addButton(new GuiRateBar(this, new IBarInfoHandler() {
            @Override
            public ITextComponent getTooltip() {
                return TextComponentUtil.build(Translation.of("gui.mekanism.outputting"), ": ", EnergyDisplay.of(tileEntity.getLastOutput()), "/t");
            }

            @Override
            public double getLevel() {
                return tileEntity.structure == null ? 0 : tileEntity.getLastOutput() / tileEntity.structure.getTransferCap();
            }
        }, resource, 38, 13));
        addButton(new GuiEnergyInfo(() -> Arrays.asList(
              TextComponentUtil.build(Translation.of("gui.mekanism.storing"), ": ", EnergyDisplay.of(tileEntity.getEnergy(), tileEntity.getMaxEnergy())),
              TextComponentUtil.build(Translation.of("gui.mekanism.input"), ": ", EnergyDisplay.of(tileEntity.getLastInput()), "/t"),
              TextComponentUtil.build(Translation.of("gui.mekanism.output"), ": ", EnergyDisplay.of(tileEntity.getLastOutput()), "/t")),
              this, resource));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawCenteredText(TextComponentUtil.translate("gui.mekanism.matrixStats"), 0, xSize, 6, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("gui.mekanism.input"), ":"), 53, 26, 0x797979);
        drawString(EnergyDisplay.of(tileEntity.getLastInput(), tileEntity.getTransferCap()).getTextComponent(), 59, 35, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("gui.mekanism.output"), ":"), 53, 46, 0x797979);
        drawString(EnergyDisplay.of(tileEntity.getLastOutput(), tileEntity.getTransferCap()).getTextComponent(), 59, 55, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("gui.mekanism.dimensions"), ":"), 8, 82, 0x797979);
        if (tileEntity.structure != null) {
            drawString(tileEntity.structure.volWidth + " x " + tileEntity.structure.volHeight + " x " + tileEntity.structure.volLength, 14, 91, 0x404040);
        }
        drawString(TextComponentUtil.build(Translation.of("gui.mekanism.constituents"), ":"), 8, 102, 0x797979);
        drawString(TextComponentUtil.build(tileEntity.getCellCount() + " ", Translation.of("gui.mekanism.cells")), 14, 111, 0x404040);
        drawString(TextComponentUtil.build(tileEntity.getProviderCount() + " ", Translation.of("gui.mekanism.providers")), 14, 120, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getGuiLocation() {
        return MekanismUtils.getResource(ResourceType.GUI, "null.png");
    }
}