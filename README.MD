# Intro

This program provides some basic commands for manipulation of java keystores.
At the moment limited modification of secret keys is provided.

Available commands are:
 * ks load         - load keystore from file
 * ks create       - create in memory keystore, initialize it (load it)
 * ks save         - save loaded keystore into a file
 * ks show         - show some info about keystore content
 * ks add password - add password to the keystore
 * ks delete entry - delete entry from keystore

# Development/Debug tips

This program uses jline. Start it with -Djline.terminal=jline.UnsupportedTerminal if you would like to debug it.
Install eclipse plugin ansiconsole (http://www.mihai-nita.net/eclipse/) in order to display ANSI color in eclipse console

# TODO

 * Allow selection of security providers.
 * Allow interactive and masked input of CliOptions that should be masked (passwords).

# Links

 * [PicketBox keystore]{https://github.com/picketbox/picketbox-keystore "PicketBox keystore"}
 * [AnsiConsole plugin]{http://www.mihai-nita.net/eclipse "Ansi Console for Eclipse"}
 * [Java Keystores]{https://docs.jboss.org/author/display/SECURITY/Java+Keystores "Java Keystores"}

