package net.ilx.keystore.commands;

import org.apache.commons.codec.binary.Base64;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

public class KeyStoreCommands implements CommandMarker {

	private KeyStoreWrapper workKeystore;

	@CliAvailabilityIndicator({"ks show", "ks save", "ks password add", "ks password get", "ks delete entry"})
	public boolean isKeystoreLoaded() {
		// keystore must be loaded for some commands
		return (null != workKeystore);
	}

	@CliCommand(value = "ks create")
	public String create(@CliOption(key = "file", mandatory = true) final String file, @CliOption(key = "pass", mandatory=true) final String keystorePass) {
		AbstractKeystoreCommand loadCommand = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				keystore = KeyStoreWrapper.create(file, keystorePass);
				return "keystore created";
			}
		};

		return loadCommand.execute();
	}


	@CliCommand(value = "ks load")
	public String load(@CliOption(key = "file", mandatory = true) final String file, @CliOption(key = "pass", mandatory=true) final String keystorePass) {
		AbstractKeystoreCommand loadCommand = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				keystore = KeyStoreWrapper.load(file, keystorePass);
				return "keystore loaded";
			}
		};

		return loadCommand.execute();
	}


	@CliCommand(value = "ks show", help = "show contents of keystore")
	public String show() {

		AbstractKeystoreCommand command = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				return keystore.content();
			}
		};
		return command.execute();
	}

	@CliCommand(value = "ks save", help = "save contents of keystore")
	public String save() {
		AbstractKeystoreCommand command = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				keystore.save();
				return "keystore saved";
			}
		};

		return command.execute();
	}

	@CliCommand(value = "ks password add", help = "Add password to keystore. Please add Base64 encoded values")
	public String addPassword(@CliOption(key="alias", mandatory=true, help="Alias under which secret key will be saved")
			final String keyAlias,
			@CliOption(key="pass", mandatory=true, help="Base64 encoded password.")
			final String base64EncodedPass)
	{

		AbstractKeystoreCommand command = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				String status = "execution failed";
				if (!Base64.isBase64(base64EncodedPass)) {
					status = "Password is not base64 encoded!";
				} else {
					keystore.addPassword(keyAlias, base64EncodedPass);
					status = "added new password under alias '" + keyAlias + "'";
				}
				return status;
			}
		};

		return command.execute();
	}

	@CliCommand(value = "ks password get", help = "Retrieves Base64 encoded password from keystore.")
	public String retrievePassword(@CliOption(key="alias", mandatory=true, help="Alias of the secret key.")
								   final String alias
								   )
	{
		AbstractKeystoreCommand command = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				return keystore.retrievePassword(alias);
			}
		};
		return command.execute();
	}


	@CliCommand(value = "ks delete entry", help = "Deletes entry from keystore.")
	public String deleteEntry(@CliOption(key="alias", mandatory=true)
			final String alias
		)
	{
		AbstractKeystoreCommand command = new AbstractKeystoreCommand(workKeystore) {
			@Override
			protected String doExecute() throws Throwable {
				keystore.deleteEntry(alias);
				return "deleted alias '" + alias + "'";
			}
		};

		return command.execute();
	}

}

