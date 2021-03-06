package com.massivecraft.massivebooks.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivebooks.Lang;
import com.massivecraft.massivebooks.Perm;
import com.massivecraft.massivebooks.entity.MBook;
import com.massivecraft.massivebooks.entity.MBookColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.arg.ARPlayer;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.InventoryUtil;

public class CmdBookGive extends MassiveBooksCommand
{
	public CmdBookGive()
	{
		this.addAliases("give");
		
		this.addOptionalArg("player", "you");
		this.addOptionalArg("amount", "1");
		this.addOptionalArg("title", "*bookandquill*");
		this.setErrorOnToManyArgs(false);
		
		this.addRequirements(ReqHasPerm.get(Perm.GIVE.node));
	}
	
	@Override
	public void perform() throws MassiveException
	{
		// What player should we give to?
		if (me == null && !this.argIsSet(0))
		{
			msg("<b>The player argument is required from console.");
			return;
		}
		Player player = this.arg(0, ARPlayer.get(), me);
		
		// How many? or perhaps ensure the player has at least one?
		Integer amount = 1;
		boolean ensure = false;
		if (this.argIsSet(1) && this.arg(1).toLowerCase().startsWith("e"))
		{
			ensure = true;
		}
		else
		{
			amount = this.arg(1, ARInteger.get(), 1);
			if (amount <= 0)
			{
				sendMessage(Lang.AMOUNT_MUST_BE_POSITIVE);
				return;
			}
		}
		
		// What items should we give?
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (!this.argIsSet(2))
		{
			items.add(new ItemStack(Material.BOOK_AND_QUILL));
		}
		else if (this.argConcatFrom(2).toLowerCase().equals("all"))
		{
			for (MBook mbook : MBookColl.get().getAll())
			{
				items.add(mbook.getItem());
			}
		}
		else
		{
			MBook mbook = this.argConcatFrom(2, ARMBook.get());
			items.add(mbook.getItem());
		}
		
		// Now for each item ...
		for (ItemStack item : items)
		{
			PlayerInventory inventory = player.getInventory();
			if (ensure && inventory.containsAtLeast(item, 1))
			{
				sender.sendMessage(Lang.getAlreadyHave(Mixin.getDisplayName(player, sender), item));
				player.sendMessage(Lang.getAlreadyHave("You", item));
				continue;
			}
			
			if (InventoryUtil.roomLeft(inventory, item, amount) < amount)
			{
				sender.sendMessage(Lang.getNotEnoughRoomFor(amount, item));
				player.sendMessage(Lang.getNotEnoughRoomFor(amount, item));
				continue;
			}
			
			InventoryUtil.addItemTimes(inventory, item, amount);
			
			sender.sendMessage(Lang.getGave("You", Mixin.getDisplayName(player, sender), amount, item));
			player.sendMessage(Lang.getGave(Mixin.getDisplayName(sender, player), "you", amount, item));
		}
	}
	
	public void give(Player player, int amount, MBook mbook)
	{
		
	}
}
