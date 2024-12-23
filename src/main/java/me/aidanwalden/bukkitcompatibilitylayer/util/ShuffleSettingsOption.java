package me.aidanwalden.bukkitcompatibilitylayer.util;

import net.minecraft.util.StringIdentifiable;

public enum ShuffleSettingsOption implements StringIdentifiable {
    SWITCH_SPRINT_AND_CROUCH(0, "switch_sprint_and_crouch"),
    UNBIND_ALL(1, "unbind_all"),
    BLATANT(2, "blatant");

    public static final StringIdentifiable.EnumCodec<ShuffleSettingsOption> CODEC = StringIdentifiable.createCodec(ShuffleSettingsOption::values);

    private final int id;
    private final String name;

    ShuffleSettingsOption(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static ShuffleSettingsOption byId(int id) {
        for (ShuffleSettingsOption option : values()) {
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

    public static ShuffleSettingsOption byName(String name) {
        return CODEC.byId(name);
    }

    @Override
    public String asString() {
        return this.name;
    }
}
