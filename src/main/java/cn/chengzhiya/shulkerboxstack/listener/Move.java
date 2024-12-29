package cn.chengzhiya.shulkerboxstack.listener;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public final class Move implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        if (!event.getClick().isLeftClick()) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (!event.getCursor().getType().toString().contains("SHULKER_BOX")) {
            return;
        }

        if (item == null) {
            event.setCancelled(true);
            event.setCurrentItem(event.getCursor());
            event.setCursor(new ItemStack(Material.AIR));
            return;
        }

        if (item.getAmount() > 0 && event.getCursor().getAmount() > 0) {
            if (NBT.readNbt(item).equals(NBT.readNbt(event.getCursor()))) {
                return;
            }
        }

        event.setCancelled(true);
        event.setCurrentItem(event.getCursor());
        event.setCursor(item);
    }
}
