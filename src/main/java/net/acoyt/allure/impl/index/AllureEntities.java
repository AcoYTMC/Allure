package net.acoyt.allure.impl.index;

import net.acoyt.acornlib.api.registrants.EntityTypeRegistrant;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.entity.SoulEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * @author AcoYT
 */
public interface AllureEntities {
    EntityTypeRegistrant ENTITIES = new EntityTypeRegistrant(Allure.MOD_ID);

    EntityType<SoulEntity> SOUL = ENTITIES.register("soul",
            EntityType.Builder.<SoulEntity>of(SoulEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(6)
                    .updateInterval(20));

    static void init() {}
}
