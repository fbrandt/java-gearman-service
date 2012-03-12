/*
 * Copyright (C) 2012 by Isaiah van der Elst <isaiah.v@comcast.net>
 * Use and distribution licensed under the BSD license.  See
 * the COPYING file in the parent directory for full text.
 */

package org.gearman;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Specifies what to do when a job server unexpectedly disconnects or a service cannot connect
 * to a job server.
 * 
 * @author isaiah
 *
 */
public interface GearmanLostConnectionPolicy {
	
	/**
	 * Defines why a method is being called
	 * @author isaiah
	 *
	 */
	public static enum Grounds implements Serializable {
		
		/**
		 * The server in question unexpectedly disconnected.
		 */
		UNEXPECTED_DISCONNECT,
		
		/**
		 * The gearman service failed to connect to a server registed with the service  
		 */
		FAILED_CONNECTION,
		
		/** 
		 * The connection was closed due to a server failing to respond
		 */
		RESPONSE_TIMEOUT,
	};
	
	/**
	 * Called when a gearman service fails to connect to a remote job server or is unexpectedly
	 * disconnected.<br>
	 * <br>
	 * If null is returned or some runtime exception is thrown, the default policy will be taken.
	 * The default policy is normally to reconnect after a period of time.
	 * 
	 * @param adrs
	 * 		The address of the server in question 
	 * @param service
	 * 		The gearman service calling this method
	 * @param grounds
	 * 		The grounds for calling this method
	 * @return
	 * 		An {@link Action} telling the gearman service what actions to take
	 */
	public GearmanLostConnectionAction lostRemoteServer(InetSocketAddress adrs, GearmanServerPool service, Grounds grounds);
	
	/**
	 * Called when a gearman service fails to connect to a local job server or is unexpectedly
	 * disconnected.<br>
	 * <br>
	 * Servers running in the local address space only cause connection failures if it's been
	 * shutdown. Reconnecting to a shutdown local job server is not an option. Therefore
	 * they're always removed from the service. This method notifies the user that the server is
	 * being removed from the service.
	 * 
	 * @param server
	 * 		The local gearman server in question
	 * @param service
	 * 		The gearman service calling this method
	 * @param grounds
	 * 		The grounds for calling this method
	 */
	public void lostLocalServer(GearmanServer server, GearmanServerPool service, Grounds grounds);
}
