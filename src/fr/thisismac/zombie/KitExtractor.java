package fr.thisismac.zombie;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import net.ess3.api.IEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;

public class KitExtractor {
    public static ItemStack[] getKit(final String kitname) {
        return KitExtractor.getKit(kitname, Bukkit.getConsoleSender());
    }

    public static ItemStack[] getKit(final String kitname, final CommandSender nameHolder) {
        ItemStack[] iss;
        try {
            final IEssentials ess = JavaPlugin.getPlugin(Essentials.class);
            final List<String> list = Kit.getItems(ess, null, kitname.toLowerCase(), ess.getSettings().getKit(kitname.toLowerCase()));
            iss = KitExtractor.parse(ess, list, nameHolder);
        } catch (final Exception ex) {
            ex.printStackTrace();
            return new ItemStack[0];
        }
        return iss;
    }

    public static ItemStack[] parse(final IEssentials ess, final List<String> items, final CommandSender nameHolder) throws Exception {
        final List<ItemStack> iss = new ArrayList<ItemStack>();
        final IText input = new SimpleTextInput(items);
        final IText output = new KeywordReplacer(input, new CommandSource(nameHolder), ess);
        final boolean allowUnsafe = ess.getSettings().allowUnsafeEnchantments();
        for (final String kitItem : output.getLines()) {
            if (kitItem.startsWith(ess.getSettings().getCurrencySymbol())) {
                final BigDecimal value = new BigDecimal(kitItem.substring(ess.getSettings().getCurrencySymbol().length()).trim());
                iss.add(KitExtractor.fromCurrency(value.doubleValue()));
            } else {
                final String[] parts = kitItem.split(" +");
                final ItemStack parseStack = ess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack.getType() != Material.AIR) {
                    final MetaItemStack metaStack = new MetaItemStack(parseStack);
                    if (parts.length > 2) {
                        metaStack.parseStringMeta(null, allowUnsafe, parts, 2, ess);
                    }
                    iss.add(metaStack.getItemStack());
                }
            }
        }
        return iss.toArray(new ItemStack[iss.size()]);
    }

    public static ItemStack fromCurrency(final double amount) {
        final ItemStack is = new ItemStack(Material.PAPER);
        final ItemMeta meta = is.getItemMeta();
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        meta.setDisplayName(ChatColor.GOLD + nf.format(amount));
        is.setItemMeta(meta);
        return is;
    }
}
