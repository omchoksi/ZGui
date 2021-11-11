package me.d3v1s0m.zgui.configuration;

import org.bukkit.Material;

/**
 * List of supported material for {@link io.github.znetworkw.znpcservers.npc.NPCType}.
 */
public enum TypeMaterialList {
    DEFAULT(Material.WRITABLE_BOOK),
    ID(Material.ENCHANTED_BOOK),
    PLAYER(Material.PLAYER_HEAD),
    ARMOR_STAND(Material.ARMOR_STAND),
    CREEPER(Material.CREEPER_SPAWN_EGG),
    BAT(Material.BAT_SPAWN_EGG),
    BLAZE(Material.BLAZE_SPAWN_EGG),
    CAVE_SPIDER(Material.CAVE_SPIDER_SPAWN_EGG),
    COW(Material.COW_SPAWN_EGG),
    CHICKEN(Material.CHICKEN_SPAWN_EGG),
    ENDER_DRAGON(Material.DRAGON_HEAD),
    ENDERMAN(Material.ENDERMAN_SPAWN_EGG),
    ENDERMITE(Material.ENDERMITE_SPAWN_EGG),
    GHAST(Material.GHAST_SPAWN_EGG),
    IRON_GOLEM(Material.IRON_BLOCK),
    GIANT(Material.ZOMBIE_HEAD),
    GUARDIAN(Material.GUARDIAN_SPAWN_EGG),
    HORSE(Material.HORSE_SPAWN_EGG),
    LLAMA(Material.LLAMA_SPAWN_EGG),
    MAGMA_CUBE(Material.MAGMA_CUBE_SPAWN_EGG),
    MUSHROOM_COW(Material.MUSHROOM_STEW),
    OCELOT(Material.OCELOT_SPAWN_EGG),
    PARROT(Material.PARROT_SPAWN_EGG),
    PIG(Material.PIG_SPAWN_EGG),
    PANDA(Material.PANDA_SPAWN_EGG),
    RABBIT(Material.RABBIT_SPAWN_EGG),
    POLAR_BEAR(Material.POLAR_BEAR_SPAWN_EGG),
    SHEEP(Material.SHEEP_SPAWN_EGG),
    SILVERFISH(Material.SILVERFISH_SPAWN_EGG),
    SNOWMAN(Material.SNOW_BLOCK),
    SKELETON(Material.SKELETON_SPAWN_EGG),
    SHULKER(Material.SHULKER_SPAWN_EGG),
    SLIME(Material.SLIME_SPAWN_EGG),
    SPIDER(Material.SPIDER_SPAWN_EGG),
    SQUID(Material.SQUID_SPAWN_EGG),
    VILLAGER(Material.VILLAGER_SPAWN_EGG),
    WITCH(Material.WITCH_SPAWN_EGG),
    WITHER(Material.WITHER_ROSE),
    ZOMBIE(Material.ZOMBIE_SPAWN_EGG),
    WOLF(Material.WOLF_SPAWN_EGG),
    FOX(Material.FOX_SPAWN_EGG),
    AXOLOTL(Material.BUCKET),
    GOAT(Material.WHITE_WOOL);


    private final Material material;

    TypeMaterialList(Material material) {
        this.material = material;
    }


    public Material getMaterial() {
        return material;
    }

}
