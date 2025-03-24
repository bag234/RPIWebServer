package org.bag.WebServer;

import org.bag.WebServer.Annotation.WebServerConfigFile;

@WebServerConfigFile
public class Index {

	public static void main(String[] args) throws Exception {
		ServerFactory.runWebServer(Index.class).run();
	}

}
