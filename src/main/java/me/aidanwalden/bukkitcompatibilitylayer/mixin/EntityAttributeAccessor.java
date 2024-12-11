package me.aidanwalden.bukkitcompatibilitylayer.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityAttribute.class)
public interface EntityAttributeAccessor {
    @Accessor
    double getFallback();
}
