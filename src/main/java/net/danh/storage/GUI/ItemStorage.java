package net.danh.storage.GUI;

import net.danh.storage.Action.Deposit;
import net.danh.storage.Action.Sell;
import net.danh.storage.Action.Withdraw;
import net.danh.storage.GUI.manager.IGUI;
import net.danh.storage.GUI.manager.InteractiveItem;
import net.danh.storage.Manager.ItemManager;
import net.danh.storage.Storage;
import net.danh.storage.Utils.Chat;
import net.danh.storage.Utils.File;
import net.danh.storage.Utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemStorage implements IGUI {

    private final Player p;

    private final String material;
    private final FileConfiguration config;
    private final int returnPage;

    public ItemStorage(Player p, String material) {
        this(p, material, 0);
    }

    public ItemStorage(Player p, String material, int returnPage) {
        this.p = p;
        this.material = material;
        this.returnPage = returnPage;
        config = File.getItemStorage();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(p, config.getInt("size") * 9, Chat.colorizewp(Objects.requireNonNull(config.getString("title")).replace("#player#", p.getName()).replace("#material#", Objects.requireNonNull(File.getConfig().getString("items." + material, material.split(";")[0])))));
        for (String item_tag : Objects.requireNonNull(config.getConfigurationSection("items")).getKeys(false)) {
            String slot = Objects.requireNonNull(config.getString("items." + item_tag + ".slot")).replace(" ", "");
            if (slot.contains(",")) {
                for (String slot_string : slot.split(",")) {
                    InteractiveItem item = new InteractiveItem(ItemManager.getItemConfig(p, material, Objects.requireNonNull(config.getConfigurationSection("items." + item_tag))), Number.getInteger(slot_string));
                    String type_left = config.getString("items." + item_tag + ".action.left.type");
                    String action_left = config.getString("items." + item_tag + ".action.left.action");
                    String type_right = config.getString("items." + item_tag + ".action.right.type");
                    String action_right = config.getString("items." + item_tag + ".action.right.action");
                    if (type_left != null && action_left != null) {
                        item.onLeftClick(player -> {
                            if (action_left.equalsIgnoreCase("deposit")) {
                                if (type_left.equalsIgnoreCase("chat")) {
                                    net.danh.storage.Listeners.Chat.chat_deposit.put(p, material);
                                    net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.deposit.chat_number")));
                                    p.closeInventory();
                                } else if (type_left.equalsIgnoreCase("all")) {
                                    new Deposit(p, material, -1L).doAction();
                                    p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                                }
                            }
                            if (action_left.equalsIgnoreCase("withdraw")) {
                                if (type_left.equalsIgnoreCase("chat")) {
                                    net.danh.storage.Listeners.Chat.chat_withdraw.put(p, material);
                                    net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.withdraw.chat_number")));
                                    p.closeInventory();
                                } else if (type_left.equalsIgnoreCase("all")) {
                                    new Withdraw(p, material, -1).doAction();
                                    p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                                }
                            }
                            if (action_left.equalsIgnoreCase("sell")) {
                                if (type_left.equalsIgnoreCase("chat")) {
                                    net.danh.storage.Listeners.Chat.chat_sell.put(p, material);
                                    net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.sell.chat_number")));
                                    p.closeInventory();
                                } else if (type_left.equalsIgnoreCase("all")) {
                                    new Sell(p, material, -1).doAction();
                                    p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                                }
                            }
                            if (type_left.equalsIgnoreCase("command")) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Storage.getStorage().getServer().dispatchCommand(p, action_left);
                                    }
                                }.runTask(Storage.getStorage());
                            }
                        });
                    }
                    if (type_right != null && action_right != null) {
                        item.onRightClick(player -> {
                            if (action_right.equalsIgnoreCase("deposit")) {
                                if (type_right.equalsIgnoreCase("chat")) {
                                    net.danh.storage.Listeners.Chat.chat_deposit.put(p, material);
                                    net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.deposit.chat_number")));
                                    p.closeInventory();
                                } else if (type_right.equalsIgnoreCase("all")) {
                                    new Deposit(p, material, -1L).doAction();
                                    p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                                }
                            }
                            if (action_right.equalsIgnoreCase("withdraw")) {
                                if (type_right.equalsIgnoreCase("chat")) {
                                    net.danh.storage.Listeners.Chat.chat_withdraw.put(p, material);
                                    net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.withdraw.chat_number")));
                                    p.closeInventory();
                                } else if (type_right.equalsIgnoreCase("all")) {
                                    new Withdraw(p, material, -1).doAction();
                                    p.openInventory(new PersonalStorage(p).getInventory());
                                }
                            }
                            if (action_right.equalsIgnoreCase("sell")) {
                                if (type_right.equalsIgnoreCase("chat")) {
                                    net.danh.storage.Listeners.Chat.chat_sell.put(p, material);
                                    net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                    p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.sell.chat_number")));
                                    p.closeInventory();
                                } else if (type_right.equalsIgnoreCase("all")) {
                                    new Sell(p, material, -1).doAction();
                                    p.openInventory(new PersonalStorage(p).getInventory());
                                }
                            }
                            if (type_right.equalsIgnoreCase("command")) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Storage.getStorage().getServer().dispatchCommand(p, action_right);
                                    }
                                }.runTask(Storage.getStorage());
                            }
                        });
                    }
                    inventory.setItem(item.getSlot(), item);
                }
            } else {
                InteractiveItem item = new InteractiveItem(ItemManager.getItemConfig(p, material, Objects.requireNonNull(config.getConfigurationSection("items." + item_tag))), Number.getInteger(slot));
                String type_left = config.getString("items." + item_tag + ".action.left.type");
                String action_left = config.getString("items." + item_tag + ".action.left.action");
                String type_right = config.getString("items." + item_tag + ".action.right.type");
                String action_right = config.getString("items." + item_tag + ".action.right.action");
                if (type_left != null && action_left != null) {
                    item.onLeftClick(player -> {
                        if (action_left.equalsIgnoreCase("deposit")) {
                            if (type_left.equalsIgnoreCase("chat")) {
                                net.danh.storage.Listeners.Chat.chat_deposit.put(p, material);
                                net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.deposit.chat_number")));
                                p.closeInventory();
                            } else if (type_left.equalsIgnoreCase("all")) {
                                new Deposit(p, material, -1L).doAction();
                                p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                            }
                        }
                        if (action_left.equalsIgnoreCase("withdraw")) {
                            if (type_left.equalsIgnoreCase("chat")) {
                                net.danh.storage.Listeners.Chat.chat_withdraw.put(p, material);
                                net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.withdraw.chat_number")));
                                p.closeInventory();
                            } else if (type_left.equalsIgnoreCase("all")) {
                                new Withdraw(p, material, -1).doAction();
                                p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                            }
                        }
                        if (action_left.equalsIgnoreCase("sell")) {
                            if (type_left.equalsIgnoreCase("chat")) {
                                net.danh.storage.Listeners.Chat.chat_sell.put(p, material);
                                net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.sell.chat_number")));
                                p.closeInventory();
                            } else if (type_left.equalsIgnoreCase("all")) {
                                new Sell(p, material, -1).doAction();
                                p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                            }
                        }
                        if (type_left.equalsIgnoreCase("command")) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Storage.getStorage().getServer().dispatchCommand(p, action_left);
                                }
                            }.runTask(Storage.getStorage());
                        }
                    });
                }
                if (type_right != null && action_right != null) {
                    item.onRightClick(player -> {
                        if (action_right.equalsIgnoreCase("deposit")) {
                            if (type_right.equalsIgnoreCase("chat")) {
                                net.danh.storage.Listeners.Chat.chat_deposit.put(p, material);
                                net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.deposit.chat_number")));
                                p.closeInventory();
                            } else if (type_right.equalsIgnoreCase("all")) {
                                new Deposit(p, material, -1L).doAction();
                                p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                            }
                        }
                        if (action_right.equalsIgnoreCase("withdraw")) {
                            if (type_right.equalsIgnoreCase("chat")) {
                                net.danh.storage.Listeners.Chat.chat_withdraw.put(p, material);
                                net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.withdraw.chat_number")));
                                p.closeInventory();
                            } else if (type_right.equalsIgnoreCase("all")) {
                                new Withdraw(p, material, -1).doAction();
                                p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                            }
                        }
                        if (action_right.equalsIgnoreCase("sell")) {
                            if (type_right.equalsIgnoreCase("chat")) {
                                net.danh.storage.Listeners.Chat.chat_sell.put(p, material);
                                net.danh.storage.Listeners.Chat.chat_return_page.put(p, returnPage);
                                p.sendMessage(Chat.colorize(File.getMessage().getString("user.action.sell.chat_number")));
                                p.closeInventory();
                            } else if (type_right.equalsIgnoreCase("all")) {
                                new Sell(p, material, -1).doAction();
                                p.openInventory(new PersonalStorage(p, returnPage).getInventory());
                            }
                        }
                        if (type_right.equalsIgnoreCase("command")) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Storage.getStorage().getServer().dispatchCommand(p, action_right);
                                }
                            }.runTask(Storage.getStorage());
                        }
                    });
                }
                inventory.setItem(item.getSlot(), item);
            }
        }
        return inventory;
    }

    public Player getPlayer() {
        return p;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getMaterial() {
        return material;
    }

    public int getReturnPage() {
        return returnPage;
    }
}
