package net.acoyt.allure.impl.index;

import net.acoyt.acornlib.api.template.RegistrantBase;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.ramifications.ChainingRamification;
import net.acoyt.allure.impl.ramifications.Ramification;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

/**
 * @author AcoYT
 */
public interface AllureRamifications {
    Registrant RAMIFICATIONS = new Registrant(Allure.MOD_ID);

    Ramification CHAINING = RAMIFICATIONS.register("chaining", new ChainingRamification(0.5F, 7.5));

    static void init() {}

    class Registrant extends RegistrantBase<Ramification> {
        public Registrant(String modId) {
            super(modId, AllureRegistries.RAMIFICATIONS);
        }

        public void registerLang(HolderLookup.Provider provider, FabricLanguageProvider.TranslationBuilder builder) {
            //
        }
    }
}
