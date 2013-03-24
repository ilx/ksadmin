package net.ilx.keystore.commands;


abstract class AbstractKeystoreCommand extends AbstractCommand {

	protected KeyStoreWrapper keystore;

	protected AbstractKeystoreCommand(KeyStoreWrapper p_keystore) {
		this.keystore = p_keystore;
	}

}