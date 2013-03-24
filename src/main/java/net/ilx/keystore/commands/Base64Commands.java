package net.ilx.keystore.commands;

import org.apache.commons.codec.binary.Base64;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

public class Base64Commands implements CommandMarker {

	@CliCommand(value="base64 encode", help="base64 encode string using provided encoding (default is UTF-8)")
	public String encode(@CliOption(key="value", mandatory=true)
						 final String value,
						 @CliOption(key="encoding", specifiedDefaultValue="UTF-8", unspecifiedDefaultValue="UTF-8")
						 final String encoding
						 )
	{
		AbstractCommand command = new AbstractCommand() {
			@Override
			protected String doExecute() throws Throwable {
				return Base64.encodeBase64URLSafeString(value.getBytes(encoding));
			}
		};
		return command.execute();
	}

	@CliCommand(value="base64 decode", help="base64 decode string using provided encoding (default is UTF-8)")
	public String decode(@CliOption(key="value", mandatory=true)
						 final String value,
						 @CliOption(key="encoding", specifiedDefaultValue="UTF-8", unspecifiedDefaultValue="UTF-8")
						 final String encoding
						 )
	{
		AbstractCommand command = new AbstractCommand() {
			@Override
			protected String doExecute() throws Throwable {
				return new String(Base64.decodeBase64(value), encoding);
			}
		};
		return command.execute();
	}

}
