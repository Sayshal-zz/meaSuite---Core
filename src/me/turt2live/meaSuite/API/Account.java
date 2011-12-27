package me.turt2live.meaSuite.API;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.register.payment.Method.MethodAccount;
import com.nijikokun.register.payment.Methods;

public class Account {

	private Player			player;
	@SuppressWarnings("unused")
	private Plugin		meaSuite;
	private MeaAPI			api;

	private MethodAccount	account;

	public Account(Player player, Plugin meaSuite, MeaAPI api) {
		this.player = player;
		this.api = api;
		this.meaSuite = meaSuite;

		if (hasAccount()) account = Methods.getMethod().getAccount(this.player.getName());
	}

	public boolean hasAccount() {
		return Methods.getMethod().hasAccount(player.getName());
	}

	public double balance() {
		return account.balance();
	}

	public boolean addFunds(double amount) {
		return account.add(amount);
	}

	public boolean subtractFunds(double amount) {
		return account.subtract(amount);
	}

	public boolean setFunds(double amount) {
		return account.set(amount);
	}

	public boolean transferFunds(double amount, Account to) {
		boolean status = false;
		if (to != null) if (account.hasEnough(amount)) {
			status = account.subtract(amount);
			boolean status2 = to.addFunds(amount);
			if (status && status2) status = true;
			else {
				// revert
				if (status) if (!account.add(amount)) api.log("Cannot revert subtraction from transfer");
				if (status2) if (!to.subtractFunds(amount)) api.log("Cannot revert addition from transfer");
				status = false;
			}
		}
		return status;
	}

	public boolean multiplyFunds(double amount) {
		return account.multiply(amount);
	}

	public boolean divideFunds(double amount) {
		return account.divide(amount);
	}

	public boolean canAfford(double amount) {
		return account.hasEnough(amount);
	}

	public boolean hasMoreThan(double amount) {
		return account.hasOver(amount);
	}

	public boolean hasLessThan(double amount) {
		return account.hasUnder(amount);
	}

	public boolean hasNegative() {
		return account.isNegative();
	}

	public boolean removeAccount() {
		return account.remove();
	}
}
