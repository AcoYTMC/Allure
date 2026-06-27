package net.acoyt.allure.impl.cca.entity;

import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.component.AllureComponent;
import net.acoyt.allure.impl.ramifications.Ramification;
import net.acoyt.allure.impl.ramifications.SoulHolder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

/**
 * @author AcoYT
 */
@SuppressWarnings("UnstableApiUsage")
public class SoulsComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<SoulsComponent> KEY = ComponentRegistry.getOrCreate(Allure.id("souls"), SoulsComponent.class);
    private final Player player;

    public int souls = 0;
    public int maxSouls = 50;
    public float maxMultiplier = 0.0F;

    public SoulsComponent(Player player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(player);
    }

    public void tick() {
        int clamped = Mth.clamp(souls, 0, getMaxSouls());
        if (clamped != souls) {
            souls = clamped;
            sync();
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor() && !player.getItemBySlot(slot).isEmpty()) {
                ItemStack stack = player.getItemBySlot(slot);
                for (Ramification ramification : AllureComponent.getRamifications(stack)) {
                    if (ramification instanceof SoulHolder holder) {
                        if (holder.maxAmountMultiplier() != maxMultiplier) {
                            maxMultiplier = holder.maxAmountMultiplier();
                            sync();
                        }
                    }
                }
            }
        }
    }

    public void readData(ValueInput input) {
        souls = input.getIntOr("Souls", 0);
        maxSouls = input.getIntOr("MaxSouls", 50);
        maxMultiplier = input.getFloatOr("MaxMultiplier", 0.0F);
    }

    public void writeData(ValueOutput output) {
        output.putInt("Souls", souls);
        output.putInt("MaxSouls", maxSouls);
        output.putFloat("MaxMultiplier", maxMultiplier);
    }

    public int getSouls() {
        return souls;
    }

    public void addSouls(int souls) {
        this.souls = Mth.clamp(this.souls + souls, 0, getMaxSouls());
    }

    public int getMaxSouls() {
        return (int) (maxSouls * maxMultiplier);
    }
}
