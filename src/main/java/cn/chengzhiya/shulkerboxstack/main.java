package cn.chengzhiya.shulkerboxstack;

import cn.chengzhiya.shulkerboxstack.listener.FastUse;
import cn.chengzhiya.shulkerboxstack.listener.Move;
import cn.chengzhiya.shulkerboxstack.listener.Stack;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(new FastUse(), this);
        Bukkit.getPluginManager().registerEvents(new Move(), this);
        Bukkit.getPluginManager().registerEvents(new Stack(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
