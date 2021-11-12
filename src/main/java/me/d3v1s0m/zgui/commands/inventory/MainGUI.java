package me.d3v1s0m.zgui.commands.inventory;

import com.google.common.primitives.Ints;
import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCModel;
import io.github.znetworkw.znpcservers.npc.NPCSkin;
import io.github.znetworkw.znpcservers.npc.NPCType;
import io.github.znetworkw.znpcservers.user.EventService;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventory;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryPage;
import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackBuilder;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import me.d3v1s0m.zgui.configuration.TypeMaterialList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainGUI extends ZInventory {
    /**
     * Creates a new inventory for the given player.
     *
     * @param player The player to create the inventory for.
     */
    public MainGUI(Player player) {
        super(player);
        setCurrentPage(new MainPage(this));
    }

    /**
     * Callback interface for setting the skin of a npc.
     */
    interface SkinFunction {
        /** Sets the new skin of the given npc. */
        void apply(Player sender, NPC npc, String skin);
    }

    private static final SkinFunction DO_APPLY_SKIN =
            (sender, npc, skin) -> NPCSkin.forName(skin, (skinValues, throwable) -> {
                if (throwable != null) {
                    Configuration.MESSAGES.sendMessage(sender, ConfigurationValue.CANT_GET_SKIN, skin);
                    return;
                }
                npc.changeSkin(NPCSkin.forValues(skinValues));
                Configuration.MESSAGES.sendMessage(sender, ConfigurationValue.SUCCESS);
            });

    /**
     * Default/main page for gui.
     */
    static class MainPage extends ZInventoryPage {

        /**
         * Main GUI page.
         *
         * @param inventory    The inventory.
         */
        public MainPage(ZInventory inventory) {
            super(inventory, "Main GUI", 3);
        }

        @Override
        public void update() {
            addItem(ItemStackBuilder.forMaterial(Material.GREEN_CONCRETE)
                            .setName(ChatColor.GREEN + "Create NPC")
                            .setLore(ChatColor.GRAY + "Click to open Create GUI")
                            .build(),
                    1,
                    clickEvent -> {
                        if (clickEvent.getClick() == ClickType.LEFT || clickEvent.getClick() == ClickType.RIGHT) {
                            new CreatePage(getInventory()).openInventory();
                        }
                    });

            addItem(ItemStackBuilder.forMaterial(Material.RED_CONCRETE)
                            .setName(ChatColor.RED + "Delete NPC")
                            .setLore(ChatColor.GRAY + "Click to open Delete GUI")
                            .build(),
                    3,
                    clickEvent -> {
                        if (clickEvent.getClick() == ClickType.LEFT || clickEvent.getClick() == ClickType.RIGHT) {
                            new DeletePage(getInventory()).openInventory();
                        }
                    });

            addItem(ItemStackBuilder.forMaterial(Material.BARRIER)
                            .setName(ChatColor.DARK_RED + "Close")
                            .setLore(ChatColor.GRAY + "Click to close GUI")
                            .build(),
                    22,
                    clickEvent -> getInventory().getPlayer().closeInventory());
        }
    }

    static class CreatePage extends ZInventoryPage {

        /**
         * Create NPC Page.
         *
         * @param zInventory    The inventory.
         */
        public CreatePage(ZInventory zInventory) {
            super(zInventory, "Create NPC", 3);
        }

        Integer id = 0;
        NPCType npcType = null;
        String name = "NPC";

        @Override
        public void update() {

            addItem(ItemStackBuilder.forMaterial(TypeMaterialList.valueOf(id == 0 ? "DEFAULT" : "ID").getMaterial())
                            .setName("set ID")
                            .setLore(ChatColor.GRAY + "Currently: " + (id == 0 ? "none" : id))
                            .build(),
                    11,
                    clickEvent -> {
                        Utils.sendTitle(getPlayer(), "&6&lCHANGE ID", "&7Enter the new id...");
                        EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class)
                                .addConsumer(event -> {
                                    //noinspection UnstableApiUsage
                                    Integer input_id = Ints.tryParse(event.getMessage());
                                    if (input_id == null) {
                                        getPlayer().sendMessage(ChatColor.RED + "Invalid Input");
                                        return;
                                    }
                                    boolean foundNPC = ConfigurationConstants.NPC_LIST.stream().anyMatch(npc -> npc.getId() == input_id);
                                    if (foundNPC) {
                                        getPlayer().sendMessage(ChatColor.RED + "A NPC with that id already exists");
                                        return;
                                    }
                                    id = input_id;
                                    getPlayer().sendMessage(ChatColor.GREEN + "Id set to " + id);
                                })
                                .addConsumer(event -> openInventory());
                    });

            addItem(ItemStackBuilder.forMaterial(TypeMaterialList.valueOf(npcType == null ? "DEFAULT" : npcType.name()).getMaterial())
                            .setName("set Type")
                            .setLore(ChatColor.GRAY + "Currently: " + (npcType == null ? "none" : npcType.name()))
                            .build(),
                    13,
                    clickEvent -> {
                        Utils.sendTitle(getPlayer(), "&6&lCHANGE TYPE", "&7Enter the new type...");
                        EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class)
                                .addConsumer(event -> {
                                    NPCType input_npcType;
                                    try {
                                        input_npcType = NPCType.valueOf(event.getMessage().toUpperCase());
                                    } catch (IllegalArgumentException exception) {
                                        getPlayer().sendMessage(ChatColor.RED + "Invalid NPC Type");
                                        return;
                                    }
                                    npcType = input_npcType;
                                    getPlayer().sendMessage(ChatColor.GREEN + "Type set to " + npcType.name());
                                })
                                .addConsumer(event -> openInventory());
                    });

            ItemStack itemStack = ItemStackBuilder.forMaterial(Material.NAME_TAG)
                    .setName("Set Name")
                    .setLore(ChatColor.GRAY + "Currently: " + name)
                    .build();
            if (!name.equals("NPC")) {
                itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                itemStack.setItemMeta(itemMeta);
            }

            addItem(itemStack,
                    15,
                    clickEvent -> {
                        Utils.sendTitle(getPlayer(), "&6&lCHANGE NAME", "&7Enter the new name...");
                        EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class)
                                .addConsumer(event -> {
                                    if (event.getMessage().length() < 3 || event.getMessage().length() > 16) {
                                        getPlayer().sendMessage(ChatColor.RED + "Invalid NPC name");
                                        return;
                                    }

                                    name = event.getMessage();
                                    getPlayer().sendMessage(ChatColor.GREEN + "Name set to " + name);
                                })
                                .addConsumer(event -> openInventory());
                    });

            addItem(ItemStackBuilder.forMaterial(Material.GREEN_CONCRETE)
                            .setName(ChatColor.GREEN + "Create NPC")
                            .setLore(ChatColor.GRAY + "Click to create NPC with",
                                    ChatColor.GRAY + "ID: " + (id == 0 ? "none" : id),
                                    ChatColor.GRAY + "NPC Type: " + (npcType == null ? "none" : npcType.name()),
                                    ChatColor.GRAY + "Name: " + name)
                            .build(),
                    26,
                    clickEvent -> {
                        if (id == 0) {
                            getPlayer().sendMessage(ChatColor.RED + "Cannot create NPC with that id");
                            getPlayer().closeInventory();
                            return;
                        } else if (npcType == null) {
                            getPlayer().sendMessage(ChatColor.RED + "Cannot create NPC with that NPC Type");
                            getPlayer().closeInventory();
                            return;
                        }
                        NPC created_npc = ServersNPC.createNPC(id, npcType, getPlayer().getLocation(), name);
                        if (npcType == NPCType.PLAYER) {
                            DO_APPLY_SKIN.apply(getPlayer(), created_npc, name);
                        }
                        getPlayer().closeInventory();
                        ZLocation npc_loc = created_npc.getNpcPojo().getLocation();
                        getPlayer().sendMessage(ChatColor.GREEN + "NPC create with ID: " + (id == 0 ? "none" : id) +
                                ", NPC Type: " + (npcType == null ? "none" : npcType.name()) + ", Name: " + name +
                                " at X: " + npc_loc.getX() +
                                " Y: " + npc_loc.getY() +
                                " Z: " + npc_loc.getZ());
                    });

            addItem(ItemStackBuilder.forMaterial(Material.BARRIER)
                            .setName(ChatColor.DARK_RED + "Close")
                            .setLore(ChatColor.GRAY + "Click to close GUI")
                            .build(),
                    22,
                    clickEvent -> getPlayer().closeInventory());
        }
    }

    static class DeletePage extends ZInventoryPage {

        int pageID = 1;

        /**
         * Delete NPC Page.
         *
         * @param zInventory    The inventory.
         */

        public DeletePage(ZInventory zInventory) {
            super(zInventory, "Delete NPC", 6);
        }

        @Override
        public void update() {
            List<NPCModel> npcModelList = ConfigurationConstants.NPC_LIST;
            if (pageID > 1) {
                addItem(ItemStackBuilder.forMaterial(Material.ARROW)
                                .setName(ChatColor.GRAY + "Previous page")
                                .build(),
                        getRows() - 6,
                        clickEvent -> {
                            pageID -= 1;
                            openInventory();
                        });
            }
            if (npcModelList.size() > 45 * pageID) {
                addItem(ItemStackBuilder.forMaterial(Material.ARROW)
                                .setName(ChatColor.GRAY + "Next page")
                                .build(),
                        getRows() - 4,
                        clickEvent -> {
                            pageID += 1;
                            openInventory();
                        });
            }
            int slots = 45 * pageID;
            int min = Math.min(slots, npcModelList.size());
            ArrayList<String> lore = new ArrayList<>();
            for (int i = slots - 45; i < min; i++) {
                NPCModel npcModel = npcModelList.get(i);
                List<String> npcHoloLines = npcModel.getHologramLines();
                Collections.reverse(npcHoloLines);
                lore.clear();
                lore.add(ChatColor.GRAY + "NPC Type: " + npcModel.getNpcType().name());
                lore.add(ChatColor.GRAY + "Location: " + npcModel.getLocation().getWorldName()
                        + ", X: " + npcModel.getLocation().getX()
                        + ", Y: " + npcModel.getLocation().getY()
                        + ", Z: " + npcModel.getLocation().getZ());
                lore.add(ChatColor.GRAY + "Name:");
                npcHoloLines.forEach(line -> lore.add("     " + ChatColor.GRAY + line.replace(ConfigurationConstants.SPACE_SYMBOL, " ")));
                addItem(ItemStackBuilder
                                .forMaterial(TypeMaterialList.valueOf(npcModel.getNpcType() == null ? "DEFAULT" : npcModel.getNpcType().name()).getMaterial())
                                .setName(ChatColor.GOLD + "ID :" + npcModel.getId())
                                .setLore(lore)
                                .build(),
                        i - (45 * (pageID - 1)),
                        clickEvent -> new ConfirmDeletePage(getInventory(), npcModel).openInventory());
            }
            addItem(ItemStackBuilder.forMaterial(Material.BARRIER)
                            .setName(ChatColor.DARK_RED + "Close")
                            .setLore(ChatColor.GRAY + "Click to close GUI")
                            .build(),
                    getRows() - 5,
                    clickEvent -> getPlayer().closeInventory());
        }

    }

    static class ConfirmDeletePage extends ZInventoryPage {

        NPCModel npcModel;

        /**
         * Confirm Delete NPC Page.
         *
         * @param zInventory    The inventory.
         */
        public ConfirmDeletePage(ZInventory zInventory, NPCModel npcModel) {
            super(zInventory, "Confirm Delete NPC?", 3);
            this.npcModel = npcModel;
        }

        @Override
        public void update() {
            ArrayList<String> lore = new ArrayList<>();
            List<String> npcHoloLines = npcModel.getHologramLines();
            lore.add(ChatColor.GRAY + "NPC Type: " + npcModel.getNpcType().name());
            lore.add(ChatColor.GRAY + "Location: " + npcModel.getLocation().getWorldName()
                    + ", X: " + npcModel.getLocation().getX()
                    + ", Y: " + npcModel.getLocation().getY()
                    + ", Z: " + npcModel.getLocation().getZ());
            lore.add(ChatColor.GRAY + "Name:");
            Collections.reverse(npcHoloLines);
            npcHoloLines.forEach(line -> lore.add("     " + ChatColor.GRAY + line.replace(ConfigurationConstants.SPACE_SYMBOL, " ")));
            addItem(ItemStackBuilder.forMaterial(TypeMaterialList.valueOf(npcModel.getNpcType().name()).getMaterial())
                            .setName(ChatColor.GOLD + "ID :" + npcModel.getId())
                            .setLore(lore)
                            .build(),
                    (getRows() - 1) / 2,
                    clickEvent -> {});
            addItem(ItemStackBuilder.forMaterial(Material.RED_CONCRETE)
                            .setName(ChatColor.DARK_RED + "Delete NPC?")
                            .setLore(ChatColor.RED + "WARNING: this action is irreversible")
                            .build(),
                    ((getRows() - 1) / 2) - 2,
                    clickEvent -> {
                        if (NPC.find(npcModel.getId()) == null) {
                            getPlayer().sendMessage(ChatColor.YELLOW + "Error deleting npc.");
                            return;
                        }
                        ServersNPC.deleteNPC(npcModel.getId());
                        getPlayer().closeInventory();
                        getPlayer().sendMessage(ChatColor.RED + "NPC Deleted");
                    });
            addItem(ItemStackBuilder.forMaterial(Material.GREEN_CONCRETE)
                            .setName(ChatColor.DARK_GREEN + "Cancel")
                            .build(),
                    ((getRows() - 1) / 2) + 2,
                    clickEvent -> {
                        getPlayer().closeInventory();
                        getPlayer().sendMessage(ChatColor.GREEN + "Cancelled.");
            });
        }
    }
}
