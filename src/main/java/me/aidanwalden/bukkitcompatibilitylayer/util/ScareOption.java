package me.aidanwalden.bukkitcompatibilitylayer.util;

import net.minecraft.util.StringIdentifiable;

public enum ScareOption implements StringIdentifiable {
    CREEPER(0, "creeper"),
    STALKER(1, "stalker");

    public static final StringIdentifiable.EnumCodec<ScareOption> CODEC = StringIdentifiable.createCodec(ScareOption::values);

    private final int id;
    private final String name;

    ScareOption(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ScareOption byId(int id) {
        for (ScareOption option : values()) {
            if (option.getId() == id) {
                return option;
            }
        }
        throw new IllegalArgumentException("No ScareOption found for id: " + id);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static ScareOption byName(String name) {
        return CODEC.byId(name);
    }

    @Override
    public String asString() {
        return this.name;
    }
}
