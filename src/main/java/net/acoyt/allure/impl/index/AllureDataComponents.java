package net.acoyt.allure.impl.index;

import net.acoyt.acornlib.api.registrants.DataComponentTypeRegistrant;
import net.acoyt.allure.impl.Allure;
import net.acoyt.allure.impl.util.AllureEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.List;

/**
 * @author AcoYT
 */
public interface AllureDataComponents {
    DataComponentTypeRegistrant COMPONENTS = new DataComponentTypeRegistrant(Allure.MOD_ID);

    DataComponentType<List<AllureEntry>> ALLURES = COMPONENTS.register("allures", AllureEntry.CODEC.listOf(), AllureEntry.STREAM_CODEC.apply(ByteBufCodecs.list()));

    static void init() {}
}
