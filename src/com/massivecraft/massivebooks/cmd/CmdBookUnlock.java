package com.massivecraft.massivebooks.cmd;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivebooks.BookUtil;
import com.massivecraft.massivebooks.Lang;
import com.massivecraft.massivebooks.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

public class CmdBookUnlock extends MassiveBooksCommand
{
	public CmdBookUnlock()
	{
		this.addAliases("unlock");
		
		this.addRequirements(ReqHasPerm.get(Perm.UNLOCK.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform() throws MassiveException
	{
		ItemStack item = this.arg(ARBookInHand.getEither());
		
		if (BookUtil.isUnlocked(item))
		{
			sendMessage(Lang.getSameUnlock(item));
			return;
		}
		
		if (!BookUtil.isAuthorEquals(item, sender) && !Perm.UNLOCK_OTHER.has(sender, true)) return;
		
		ItemStack before = item.clone();
		BookUtil.unlock(item);
		me.setItemInHand(item);
		
		sendMessage(Lang.getAlterUnlock(before));
	}
}
