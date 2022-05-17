/*

* Copyright (c) Joan-Manuel Marques 2013. All rights reserved.
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
*
* This file is part of the practical assignment of Distributed Systems course.
*
* This code is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
*/

package udp.servidor;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.library.api.LSimLogger;

/**
 * @author Joan-Manuel Marques
 * @author Christian Gutiérrez Antolín 
 */

public class RemoteMapUDPservidor {
	
	public RemoteMapUDPservidor(int server_port, Map<String, String> map){
		LSimLogger.log(Level.INFO, "Inici RemoteMapUDPservidor ");
		LSimLogger.log(Level.INFO, "server_port: " + server_port);
		LSimLogger.log(Level.INFO, "map: " + map);

		/* TODO: implementació de la part servidor UDP / implementación de la parte servidor UDP */	
		DatagramSocket socket = null;
	
		try {
			socket = new DatagramSocket(server_port);
			
			byte [] bufferMessage = new byte[256];
			int bufferLength = bufferMessage.length;
			
			while(true) { 
				/* Observation for teacher:
				 * I noticed that we have imported Timer class, I was researching, 
				 * but I couldn't learn how to close the socket after time with this 
				 * class, so I decided to use socket.setSoTimeout to get to close the
				 * socket if there is no activity for 60 seconds.
				 */
				// If sever doesn't receive any packet, it will be closed
				socket.setSoTimeout(60000);
				// Create the package in which we will save the information 
				DatagramPacket packetReceived = new DatagramPacket(bufferMessage, bufferLength);
				// Save information in the packageReceived
				socket.receive(packetReceived);
				LSimLogger.log(Level.TRACE, "Socket received");
				// Get information we recieved
				String infoReceived = new String(packetReceived.getData(), 0, packetReceived.getLength());
				// Get the info we need to send 
				String infoToSend = map.get(infoReceived);
				// Create the packet to be sent
				DatagramPacket packetToSend = new DatagramPacket(infoToSend.getBytes(), infoToSend.length(), packetReceived.getAddress(), packetReceived.getPort());
				// Send the packet 
				socket.send(packetToSend);
				LSimLogger.log(Level.TRACE, "Socket sent");
			}
		} catch (SocketException e) {
			
			LSimLogger.log(Level.ERROR, "SocketException - SocketException - during execution service UDP: " + e.getMessage());
		} catch (IOException e) {
			if(e.getMessage().contains("Receive timed out")) {
				socket.close();
				LSimLogger.log(Level.TRACE, "Socket Closed");
			}
			else LSimLogger.log(Level.ERROR, "IOException - RemoteMapUDPservidor - during execution service UDP: " + e.getMessage());
		}
	}
}
