package net.acoyt.allure.impl.cca.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.acoyt.allure.impl.Allure;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
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

    private final List<ChainedEntry> chainedEntries = new ArrayList<>();

    public ChainingComponent(LivingEntity living) {
        this.living = living;
    }

    public void sync() {
        KEY.sync(living);
    }

    public void tick() {
        if (!isValidEntity(living) || !isChained()) return;
        for (ChainedEntry entry : chainedEntries) {
            entry.tick();
        }

        List<ChainedEntry> modified = chainedEntries.stream().filter(entry -> !entry.shouldEnd() && isValidEntity(entry.getChainedToFromLevel(living.level()))).toList();
        if (modified.size() != chainedEntries.size()) {
            Allure.LOGGER.info("Removed {} entries from entity {}", chainedEntries.size() - modified.size(), living.getDisplayName().getString());
            chainedEntries.clear();
            chainedEntries.addAll(modified);
            sync();
        }
    }

    public void readData(ValueInput input) {
        chainedEntries.clear();
        chainedEntries.addAll(input.read("ChainedEntries", ChainedEntry.CODEC.listOf()).orElse(List.of()));
    }

    public void writeData(ValueOutput output) {
        output.store("ChainedEntries", ChainedEntry.CODEC.listOf(), chainedEntries);
    }

    public List<ChainedEntry> getChainedEntries() {
        return chainedEntries;
    }

    public void addChainedEntries(ChainedEntry... chainedEntries) {
        if (!isValidEntity(living)) return;
        Allure.LOGGER.info("Added {} entries to entity {}", chainedEntries.length, living.getDisplayName().getString());
        this.chainedEntries.addAll(Arrays.asList(chainedEntries));
        sync();
    }

    public void addChainedEntries(LivingEntity... entities) {
        addChainedEntries(Arrays.stream(entities).map(ChainedEntry::of).toArray(ChainedEntry[]::new));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isChained() {
        return !getChainedEntries().isEmpty();
    }

    public static boolean isValidEntity(LivingEntity living) {
        return living != null && living.isAlive() && !living.isRemoved();
    }

    public List<LivingEntity> getAllChainedTo() {
        return getChainedEntries().stream()
                .filter(entry -> isValidEntity(entry.getChainedToFromLevel(living.level())))
                .map(entry -> entry.getChainedToFromLevel(living.level()))
                .toList();
    }

    public void clearChains() {
        getAllChainedTo().forEach(entity -> {
            ChainingComponent chaining = KEY.get(entity);
            for (ChainedEntry entry : chaining.getChainedEntries()) {
                List<ChainedEntry> backup = chaining.getChainedEntries();
                if (entry.getChainedToFromLevel(entity.level()) == living) {
                    backup = backup.stream().filter(chainedEntry -> chainedEntry != entry).toList();
                }

                chaining.getChainedEntries().clear();
                chaining.getChainedEntries().addAll(backup);
                chaining.sync();
            }
        });

        getChainedEntries().clear();
        sync();
    }

    public static class ChainedEntry {
        public static final Codec<ChainedEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(ChainedEntry::getColor),
                EntityReference.codec().fieldOf("chainedTo").forGetter(ChainedEntry::getChainedTo),
                Codec.BOOL.fieldOf("source").forGetter(ChainedEntry::isSource),
                Codec.INT.fieldOf("timeLeft").forGetter(ChainedEntry::getTimeLeft)
        ).apply(instance, ChainedEntry::new));

        private final int color;
        private final EntityReference<UniquelyIdentifyable> chainedTo;
        private final boolean source;
        private int timeLeft = DEFAULT_TIME;

        public ChainedEntry(int color, EntityReference<UniquelyIdentifyable> reference, boolean source, int timeLeft) {
            this.color = color;
            this.chainedTo = reference;
            this.source = source;
            this.timeLeft = timeLeft;
        }

        public ChainedEntry(int color, LivingEntity living, boolean source, int timeLeft) {
            this(color, EntityReference.of(living), source, timeLeft);
        }

        public ChainedEntry(LivingEntity living, boolean source, int timeLeft) {
            this(DyeColor.values()[RandomSource.create().nextInt(DyeColor.values().length)].getTextColor(), EntityReference.of(living), source, timeLeft);
        }

        public static ChainedEntry of(LivingEntity living, boolean source) {
            return new ChainedEntry(living, source, DEFAULT_TIME);
        }

        public static ChainedEntry of(LivingEntity living) {
            return of(living, false);
        }

        public void tick() {
            if (timeLeft > 0) {
                timeLeft--;
                if (shouldEnd()) {
                    Allure.LOGGER.info("Ticked entry");
                }
            }
        }

        public boolean shouldEnd() {
            return timeLeft == 0;
        }

        public int getColor() {
            return color;
        }

        public EntityReference<UniquelyIdentifyable> getChainedTo() {
            return chainedTo;
        }

        public boolean isSource() {
            return source;
        }

        public int getTimeLeft() {
            return timeLeft;
        }

        public void setTimeLeft(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        @Nullable
        public LivingEntity getChainedToFromLevel(Level level) {
            if (level == null) return null;
            return chainedTo.getEntity(level::getEntityInAnyDimension, UniquelyIdentifyable.class) instanceof LivingEntity living ? living : null;
        }
    }
}
