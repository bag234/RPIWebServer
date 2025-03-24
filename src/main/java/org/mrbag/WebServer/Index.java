package org.mrbag.WebServer;

import org.mrbag.WebServer.Annotation.WebServerConfigFile;

@WebServerConfigFile
public class Index {

	public static void main(String[] args) throws Exception {
		ServerFactory.runWebServer(Index.class).run();
	}

}
