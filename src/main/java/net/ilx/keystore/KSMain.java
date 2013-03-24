package net.ilx.keystore;


import java.io.IOException;

import org.springframework.shell.Bootstrap;

public class KSMain extends Bootstrap {

	public KSMain(String applicationContextLocation) throws IOException {
		// This program uses jline. Start it with -Djline.terminal=jline.UnsupportedTerminal if you would like to debug it.
		// Install eclipse plugin ansiconsole (http://www.mihai-nita.net/eclipse/) to display ANSI color in eclipse console
		super(applicationContextLocation);
	}


}
