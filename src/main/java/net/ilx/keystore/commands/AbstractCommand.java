package net.ilx.keystore.commands;

import java.io.PrintWriter;
import java.io.StringWriter;

abstract class AbstractCommand {

	protected abstract String doExecute() throws Throwable;

	public String execute() {
		String status = "command invoked";
		try {
			status = doExecute();
		} catch (Throwable t) {
			status = reportError("Invocation of command failed", t);
		}
		return status;
	}

	private String reportError(String msg, Throwable t) {
		StringWriter sw = new StringWriter();
		sw.append(msg);
		sw.append("\n");
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);

		return sw.toString();
	}

}