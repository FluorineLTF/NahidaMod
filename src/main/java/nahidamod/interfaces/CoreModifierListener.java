package nahidamod.interfaces;

public interface CoreModifierListener {
    default int modifyDendroCoreSpawnCount(int baseCount) {
        return baseCount;
    }

    default boolean shouldConvertToBountifulCore() {
        return false;
    }
}
