package me.aidanwalden.bukkitcompatibilitylayer.util;

import net.minecraft.util.StringIdentifiable;

public enum FlagOperationOption implements StringIdentifiable {
    SET(0, "set"),
    CLEAR(1, "clear");

    public static final StringIdentifiable.EnumCodec<FlagOperationOption> CODEC = StringIdentifiable.createCodec(FlagOperationOption::values);

    private final int id;
    private final String name;

    FlagOperationOption(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static FlagOperationOption byId(int id) {
        for (FlagOperationOption option : values()) {
            if (option.getId() == id) {
                return option;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static FlagOperationOption byName(String name) {
        return CODEC.byId(name);
    }

    @Override
    public String asString() {
        return this.name;
    }
}
