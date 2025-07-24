package net.danh.storage.CMD;

import net.danh.storage.API.CMDBase;
import net.danh.storage.GUI.PersonalStorage;
import net.danh.storage.Manager.ItemManager;
import net.danh.storage.Manager.MineManager;
import net.danh.storage.Storage;
import net.danh.storage.Utils.Chat;
import net.danh.storage.Utils.File;
import net.danh.storage.Utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StorageCMD extends CMDBase {
    public StorageCMD(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSender c, String[] args) {
        if (args.length == 0) {
            if (c instanceof Player) {
                try {
                    Player player = (Player) c;
                    int currentPage = PersonalStorage.getPlayerCurrentPage(player);
                    player.openInventory(new PersonalStorage(player, currentPage).getInventory());
                } catch (IndexOutOfBoundsException e) {
                    c.sendMessage(Chat.colorize(File.getMessage().getString("admin.not_enough_slot")));
                }
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (c.hasPermission("storage.admin")) {
                    File.getMessage().getStringList("admin.help").forEach(s -> c.sendMessage(Chat.colorize(s)));
                }
                File.getMessage().getStringList("user.help").forEach(s -> c.sendMessage(Chat.colorize(s)));
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (c instanceof Player) {
                    Player p = (Player) c;
                    if (p.hasPermission("storage.toggle")) {
                        MineManager.toggle.replace(p, !MineManager.toggle.get(p));
                        p.sendMessage(Chat.colorize(Objects.requireNonNull(File.getMessage().getString("user.status.toggle")).replace("#status#", ItemManager.getStatus(p))));
                    }
                }
            }
        }
        if (c.hasPermission("storage.admin") || c.hasPermission("storage.admin.reload")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    File.reloadFiles();
                    MineManager.loadBlocks();
                    for (Player p : Storage.getStorage().getServer().getOnlinePlayers()) {
                        MineManager.convertOfflineData(p);
                        MineManager.loadPlayerData(p);
                    }
                    c.sendMessage(Chat.colorize(File.getMessage().getString("admin.reload")));
                }
            }
        }
        if (c.hasPermission("storage.admin") || c.hasPermission("storage.admin.max")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("max")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null) {
                        int amount = Number.getInteger(args[2]);
                        if (amount > 0) {
                            MineManager.playermaxdata.put(p, amount);
                            c.sendMessage(Chat.colorize(Objects.requireNonNull(File.getMessage().getString("admin.set_max_storage")).replace("#player#", p.getName()).replace("#amount#", String.valueOf(amount))));
                        }
                    }
                }
            }
        }
        if (c.hasPermission("storage.admin") || c.hasPermission("storage.admin.add") || c.hasPermission("storage.admin.remove") || c.hasPermission("storage.admin.set")) {
            if (args.length == 4) {
                if (MineManager.getPluginBlocks().contains(args[1])) {
                    Player p = Bukkit.getPlayer(args[2]);
                    if (p != null) {
                        int number = Number.getInteger(args[3]);
                        if (number >= 0) {
                            if (args[0].equalsIgnoreCase("add")) {
                                if (c.hasPermission("storage.admin") || c.hasPermission("storage.admin.add")) {
                                    if (MineManager.addBlockAmount(p, args[1], number)) {
                                        c.sendMessage(Chat.colorize(File.getMessage().getString("admin.add_material_amount")).replace("#amount#", args[3]).replace("#material#", args[1]).replace("#player#", p.getName()));
                                        p.sendMessage(Chat.colorize(File.getMessage().getString("user.add_material_amount")).replace("#amount#", args[3]).replace("#material#", args[1]).replace("#player#", c.getName()));
                                    }
                                }
                            }
                            if (args[0].equalsIgnoreCase("remove")) {
                                if (c.hasPermission("storage.admin") || c.hasPermission("storage.admin.remove")) {
                                    if (MineManager.removeBlockAmount(p, args[1], number)) {
                                        c.sendMessage(Chat.colorize(File.getMessage().getString("admin.remove_material_amount")).replace("#amount#", args[3]).replace("#material#", args[1]).replace("#player#", p.getName()));
                                        p.sendMessage(Chat.colorize(File.getMessage().getString("user.remove_material_amount")).replace("#amount#", args[3]).replace("#material#", args[1]).replace("#player#", c.getName()));
                                    }
                                }
                            }
                            if (args[0].equalsIgnoreCase("set")) {
                                if (c.hasPermission("storage.admin") || c.hasPermission("storage.admin.set")) {
                                    MineManager.setBlock(p, args[1], number);
                                    c.sendMessage(Chat.colorize(File.getMessage().getString("admin.set_material_amount")).replace("#amount#", args[3]).replace("#material#", args[1]).replace("#player#", p.getName()));
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.set_material_amount")).replace("#amount#", args[3]).replace("#material#", args[1]).replace("#player#", c.getName()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(@NotNull CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        List<String> adminPerms = Arrays.asList("storage.admin.add", "storage.admin.remove", "storage.admin.set", "storage.admin.reload", "storage.admin.max");
        if (args.length == 1) {
            commands.add("help");
            if (sender.hasPermission("storage.toggle")) {
                commands.add("toggle");
            }
            if (sender.hasPermission("storage.admin")) {
                commands.add("add");
                commands.add("remove");
                commands.add("set");
                commands.add("reload");
                commands.add("max");
            } else {
                if (sender.hasPermission("storage.admin.add")) commands.add("add");
                if (sender.hasPermission("storage.admin.remove")) commands.add("remove");
                if (sender.hasPermission("storage.admin.set")) commands.add("set");
                if (sender.hasPermission("storage.admin.reload")) commands.add("reload");
                if (sender.hasPermission("storage.admin.max")) commands.add("max");
            }

            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (sender.hasPermission("storage.admin") || sender.hasPermission("storage.admin.add") || sender.hasPermission("storage.admin.remove") || sender.hasPermission("storage.admin.set")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("set")) {
                    if (commands.addAll(MineManager.getPluginBlocks())) {
                        StringUtil.copyPartialMatches(args[1], commands, completions);
                    }
                }
            }
            if (sender.hasPermission("storage.admin") || sender.hasPermission("storage.admin.max")) {
                if (args[0].equalsIgnoreCase("max")) {
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> commands.add(player.getName()));
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                }
            }
        }
        if (args.length == 3) {
            if (sender.hasPermission("storage.admin") || sender.hasPermission("storage.admin.add") || sender.hasPermission("storage.admin.remove") || sender.hasPermission("storage.admin.set")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("set")) {
                    if (MineManager.getPluginBlocks().contains(args[1])) {
                        Bukkit.getServer().getOnlinePlayers().forEach(player -> commands.add(player.getName()));
                        StringUtil.copyPartialMatches(args[2], commands, completions);
                    }
                }
            }
            if (sender.hasPermission("storage.admin") || sender.hasPermission("storage.admin.max")) {
                if (args[0].equalsIgnoreCase("max")) {
                    StringUtil.copyPartialMatches(args[2], Collections.singleton("<number>"), completions);
                }
            }
        }
        if (args.length == 4) {
            if (sender.hasPermission("storage.admin") || sender.hasPermission("storage.admin.add") || sender.hasPermission("storage.admin.remove") || sender.hasPermission("storage.admin.set")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("set")) {
                    if (MineManager.getPluginBlocks().contains(args[1])) {
                        StringUtil.copyPartialMatches(args[3], Collections.singleton("<number>"), completions);
                    }
                }
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
