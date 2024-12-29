package cn.chengzhiya.shulkerboxstack.listener;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public final class Stack implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (!event.getCurrentItem().getType().toString().contains("SHULKER_BOX") ||
                !event.getCursor().getType().toString().contains("SHULKER_BOX")
        ) {
            return;
        }

        if (!NBT.readNbt(event.getCurrentItem()).equals(NBT.readNbt(event.getCursor()))) {
            return;
        }

        event.setCancelled(true);

        int amount = event.getCursor().getAmount() + event.getCurrentItem().getAmount();
        if (amount <= 64) {
            event.getCurrentItem().setAmount(amount);
            event.setCursor(new ItemStack(Material.AIR));
        } else {
            event.getCurrentItem().setAmount(64);
            ItemStack currentItem = event.getCursor();
            currentItem.setAmount(amount - 64);
            event.setCursor(currentItem);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        ItemStack item = event.getItem();

        if (!item.toString().contains("SHULKER_BOX")) {
            return;
        }

        for (int i = 0; i < event.getDestination().getContents().length; i++) {
            ItemStack currentItem = event.getDestination().getContents()[i];

            if (currentItem == null) {
                continue;
            }

            if (currentItem.getAmount() >= 64) {
                continue;
            }

            if (!currentItem.getType().toString().contains("SHULKER_BOX")) {
                continue;
            }

            if (!NBT.readNbt(item).equals(NBT.readNbt(currentItem))) {
                continue;
            }

            currentItem.setAmount(item.getAmount() + currentItem.getAmount());
            event.setItem(new ItemStack(Material.AIR));
            break;
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        ItemStack item = event.getItem().getItemStack();

        for (int i = 0; i < event.getInventory().getContents().length; i++) {
            ItemStack currentItem = event.getInventory().getContents()[i];

            if (currentItem == null) {
                continue;
            }

            if (currentItem.getAmount() >= 64) {
                continue;
            }

            if (!currentItem.getType().toString().contains("SHULKER_BOX")) {
                continue;
            }

            if (!NBT.readNbt(item).equals(NBT.readNbt(currentItem))) {
                continue;
            }

            event.setCancelled(true);

            currentItem.setAmount(item.getAmount() + currentItem.getAmount());
            event.getInventory().setItem(i, currentItem);
            event.getItem().remove();
            return;
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (event.isCancelled()) {
                return;
            }

            ItemStack item = event.getItem().getItemStack();


            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                ItemStack currentItem = player.getInventory().getContents()[i];

                if (currentItem == null) {
                    continue;
                }

                if (currentItem.getAmount() >= 64) {
                    continue;
                }

                if (!currentItem.getType().toString().contains("SHULKER_BOX")) {
                    continue;
                }

                if (!NBT.readNbt(item).equals(NBT.readNbt(currentItem))) {
                    continue;
                }

                event.setCancelled(true);

                int amount = item.getAmount() + currentItem.getAmount();
                if (amount <= 64) {
                    currentItem.setAmount(amount);
                    player.getInventory().setItem(i, currentItem);
                    event.getItem().remove();
                } else {
                    currentItem.setAmount(64);
                    item.setAmount(amount - 64);
                    event.getItem().setItemStack(item);
                }
                return;
            }
        }
    }
}
