package net.danh.storage.Listeners;

import net.danh.storage.Action.Deposit;
import net.danh.storage.Action.Sell;
import net.danh.storage.Action.Withdraw;
import net.danh.storage.GUI.PersonalStorage;
import net.danh.storage.Storage;
import net.danh.storage.Utils.File;
import net.danh.storage.Utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class Chat implements Listener {
    public static HashMap<Player, String> chat_deposit = new HashMap<>();
    public static HashMap<Player, String> chat_withdraw = new HashMap<>();
    public static HashMap<Player, String> chat_sell = new HashMap<>();
    public static HashMap<Player, Integer> chat_return_page = new HashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(@NotNull AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = ChatColor.stripColor(e.getMessage());
        if (chat_deposit.containsKey(p) && chat_deposit.get(p) != null) {
            if (Number.getInteger(message) > 0) {
                new Deposit(p, chat_deposit.get(p), (long) Number.getInteger(message)).doAction();
                int returnPage = chat_return_page.getOrDefault(p, PersonalStorage.getPlayerCurrentPage(p));
                Bukkit.getScheduler().runTask(Storage.getStorage(), () -> p.openInventory(new PersonalStorage(p, returnPage).getInventory()));
            } else {
                p.sendMessage(net.danh.storage.Utils.Chat.colorize(Objects.requireNonNull(File.getMessage().getString("user.unknown_number")).replace("<number>", message)));
            }
            chat_deposit.remove(p);
            chat_return_page.remove(p);
            e.setCancelled(true);
        }
        if (chat_withdraw.containsKey(p) && chat_withdraw.get(p) != null) {
            if (Number.getInteger(message) > 0) {
                new Withdraw(p, chat_withdraw.get(p), Number.getInteger(message)).doAction();
                int returnPage = chat_return_page.getOrDefault(p, PersonalStorage.getPlayerCurrentPage(p));
                Bukkit.getScheduler().runTask(Storage.getStorage(), () -> p.openInventory(new PersonalStorage(p, returnPage).getInventory()));
            } else {
                p.sendMessage(net.danh.storage.Utils.Chat.colorize(Objects.requireNonNull(File.getMessage().getString("user.unknown_number")).replace("<number>", message)));
            }
            chat_withdraw.remove(p);
            chat_return_page.remove(p);
            e.setCancelled(true);
        }
        if (chat_sell.containsKey(p) && chat_sell.get(p) != null) {
            if (Number.getInteger(message) > 0) {
                new Sell(p, chat_sell.get(p), Number.getInteger(message)).doAction();
                int returnPage = chat_return_page.getOrDefault(p, PersonalStorage.getPlayerCurrentPage(p));
                Bukkit.getScheduler().runTask(Storage.getStorage(), () -> p.openInventory(new PersonalStorage(p, returnPage).getInventory()));
            } else {
                p.sendMessage(net.danh.storage.Utils.Chat.colorize(Objects.requireNonNull(File.getMessage().getString("user.unknown_number")).replace("<number>", message)));
            }
            chat_sell.remove(p);
            chat_return_page.remove(p);
            e.setCancelled(true);
        }
    }
}
