package net.acoyt.allure.impl.cca.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.Allure;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.UniquelyIdentifyable;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AcoYT
 */
@SuppressWarnings("UnstableApiUsage")
public class ChainingComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<ChainingComponent> KEY = ComponentRegistry.getOrCreate(Allure.id("chaining"), ChainingComponent.class);
    public static final int DEFAULT_TIME = 100; // 5s
    private final LivingEntity living;

    private List<ChainedEntry> chainedEntries = new ArrayList<>();

    public ChainingComponent(LivingEntity living) {
        this.living = living;
    }

    public void sync() {
        KEY.sync(living);
    }

    public void tick() {
        for (ChainedEntry entry : chainedEntries) {
            entry.tick();
            if (entry.shouldEnd()) {
                chainedEntries.remove(entry);
            }
        }
    }

    public void readData(ValueInput input) {
        chainedEntries = input.read("ChainedEntries", ChainedEntry.CODEC.listOf()).orElse(new ArrayList<>());
    }

    public void writeData(ValueOutput output) {
        output.store("ChainedEntries", ChainedEntry.CODEC.listOf(), chainedEntries);
    }

    public List<ChainedEntry> getChainedEntries() {
        return chainedEntries;
    }

    public void addChainedEntries(ChainedEntry... chainedEntries) {
        this.chainedEntries.addAll(Arrays.asList(chainedEntries));
        sync();
    }

    public void addChainedEntries(LivingEntity... entities) {
        this.chainedEntries.addAll(Arrays.stream(entities).map(ChainedEntry::of).toList());
        sync();
    }

    public static class ChainedEntry {
        public static final Codec<ChainedEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("timeLeft").forGetter(ChainedEntry::getTimeLeft),
                EntityReference.codec().fieldOf("chainedTo").forGetter(ChainedEntry::getChainedTo)
        ).apply(instance, ChainedEntry::new));

        private int timeLeft;
        private EntityReference<UniquelyIdentifyable> chainedTo;

        public ChainedEntry(int timeLeft, EntityReference<UniquelyIdentifyable> reference) {
            this.timeLeft = timeLeft;
            this.chainedTo = reference;
        }

        public ChainedEntry(int timeLeft, LivingEntity living) {
            this(timeLeft, EntityReference.of(living));
        }

        public static ChainedEntry of(LivingEntity living) {
            return new ChainedEntry(DEFAULT_TIME, living);
        }

        public void tick() {
            if (timeLeft > 0) {
                timeLeft--;
            }
        }

        public boolean shouldEnd() {
            return timeLeft == 0;
        }

        public int getTimeLeft() {
            return timeLeft;
        }

        public void setTimeLeft(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        public EntityReference<UniquelyIdentifyable> getChainedTo() {
            return chainedTo;
        }

        @Nullable
        public LivingEntity getChainedToFromLevel(Level level) {
            return chainedTo.getEntity(level::getEntityInAnyDimension, UniquelyIdentifyable.class) instanceof LivingEntity living ? living : null;
        }

        public void setChainedTo(LivingEntity chainedTo) {
            this.chainedTo = EntityReference.of(chainedTo);
        }
    }
}
