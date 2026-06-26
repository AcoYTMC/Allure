package net.acoyt.allure.impl.cca;

import net.acoyt.allure.impl.cca.entity.ChainingComponent;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

/**
 * @author AcoYT
 */
public class AllureComponents implements EntityComponentInitializer {
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(LivingEntity.class, ChainingComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(ChainingComponent::new);
    }
}
