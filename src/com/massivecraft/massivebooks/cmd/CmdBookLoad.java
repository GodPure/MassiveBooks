package com.massivecraft.massivebooks.cmd;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivebooks.Lang;
import com.massivecraft.massivebooks.Perm;
import com.massivecraft.massivebooks.entity.MBook;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

public class CmdBookLoad extends MassiveBooksCommand
{
	public CmdBookLoad()
	{
		this.addAliases("load");
		
		this.addRequiredArg("title");
		this.setErrorOnToManyArgs(false);
		
		this.addRequirements(ReqHasPerm.get(Perm.LOAD.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform() throws MassiveException
	{
		MBook mbook = this.argConcatFrom(0, ARMBook.get());
		ItemStack old = this.arg(ARBookInHand.getEither());
		
		ItemStack target = mbook.getItem();
		target.setAmount(old.getAmount());
		
		me.setItemInHand(target);
		
		sendMessage(Lang.getSuccessLoad(target));
	}
}
