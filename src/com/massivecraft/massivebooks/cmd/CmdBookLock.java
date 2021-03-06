package com.massivecraft.massivebooks.cmd;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivebooks.BookUtil;
import com.massivecraft.massivebooks.Lang;
import com.massivecraft.massivebooks.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

public class CmdBookLock extends MassiveBooksCommand
{
	public CmdBookLock()
	{
		this.addAliases("lock");
		
		this.addRequirements(ReqHasPerm.get(Perm.LOCK.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform() throws MassiveException
	{
		ItemStack item = this.arg(ARBookInHand.getEither());
		
		if (BookUtil.isLocked(item))
		{
			sendMessage(Lang.getSameLock(item));
			return;
		}
		
		if (!BookUtil.isAuthorEquals(item, sender) && !Perm.LOCK_OTHER.has(sender, true)) return;
		
		ItemStack before = item.clone();
		BookUtil.lock(item);
		me.setItemInHand(item);
		
		sendMessage(Lang.getAlterLock(before));
	}
}
