package cn.chengzhiya.shulkerboxstack.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class FastUse implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (!event.getPlayer().isSneaking()) {
            return;
        }
        if (!player.getInventory().getItemInMainHand().getType().toString().contains("SHULKER_BOX")) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getAmount() != 1) {
            return;
        }

        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        BlockStateMeta blockMate = (BlockStateMeta) meta;
        org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) blockMate.getBlockState();

        Inventory inv = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, ChatColor("&b潜影盒快捷使用"));
        inv.setContents(box.getInventory().getContents());
        player.openInventory(inv);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor("&b潜影盒快捷使用"))) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        ItemStack clickItem = event.getCurrentItem();

        if (!player.getInventory().getItemInMainHand().getType().toString().contains("SHULKER_BOX")) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        if (event.getClick() == ClickType.NUMBER_KEY) {
            clickItem = player.getInventory().getItem(event.getHotbarButton());
        }

        if (clickItem != null) {
            if (clickItem.getType().toString().contains("SHULKER_BOX")) {
                event.setCancelled(true);
                return;
            }
        }

        updateShulker(player, event.getInventory());
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(ChatColor("&b潜影盒快捷使用"))) {
            return;
        }
        Player player = (Player) event.getPlayer();

        if (!player.getInventory().getItemInMainHand().getType().toString().contains("SHULKER_BOX")) {
            return;
        }

        updateShulker(player, event.getInventory());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getOpenInventory().getOriginalTitle().equals(ChatColor("&b潜影盒快捷使用"))) {
            return;
        }
        if (event.getItemDrop().getType().toString().contains("SHULKER_BOX")) {
            event.setCancelled(true);
        }
    }

    private void updateShulker(Player player, Inventory inventory) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() instanceof BlockStateMeta) {
            BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
            org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) blockStateMeta.getBlockState();

            box.getInventory().setContents(inventory.getContents());

            blockStateMeta.setBlockState(box);
            item.setItemMeta(blockStateMeta);
            player.getInventory().setItemInMainHand(item);
        } else {
            player.sendMessage("Invalid");
        }
    }
}
