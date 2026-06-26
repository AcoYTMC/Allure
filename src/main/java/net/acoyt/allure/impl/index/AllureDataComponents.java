package net.acoyt.allure.impl.index;

import net.acoyt.acornlib.api.registrants.DataComponentTypeRegistrant;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.component.AllureComponent;
import net.minecraft.core.component.DataComponentType;

/**
 * @author AcoYT
 */
public interface AllureDataComponents {
    DataComponentTypeRegistrant COMPONENTS = new DataComponentTypeRegistrant(Allure.MOD_ID);

    DataComponentType<AllureComponent> ALLURES = COMPONENTS.register("allures", AllureComponent.CODEC, AllureComponent.STREAM_CODEC);

    static void init() {}
}
