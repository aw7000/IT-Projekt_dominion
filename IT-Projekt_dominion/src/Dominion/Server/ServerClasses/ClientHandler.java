package Dominion.Server.ServerClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import Dominion.appClasses.ChatMessageLobby;
import Dominion.appClasses.GameObject;
import Dominion.appClasses.NewGameParty;
import Dominion.appClasses.Player;
import Dominion.appClasses.StartInformation;
import Dominion.appClasses.UpdateLobby;

/**
 * @author Joel Henz: 
 * the ClientHandler is a server-side Runnable.
 * Each connected client gets an ClientHandler thread. Each ClientHandler reads messages from his client and will send them to ALL clients by iterating through the ObjectOutputStream ArrayList.
 */
public class ClientHandler implements Runnable {
	Socket s; 
	ArrayList <ObjectOutputStream> list;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	ServiceLocatorServer sl;
	
	public ClientHandler(Socket s, ArrayList <ObjectOutputStream> list, ObjectOutputStream out){ 
		this.s = s;
		this.list = list;
		this.out = out;
		sl = ServiceLocatorServer.getServiceLocator();
		try {
			this.in = new ObjectInputStream(s.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		GameObject obj;
		
		try {
			while ((obj = (GameObject) this.in.readObject()) !=null){
				sendToAllClients (obj);			
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ID for the serialized object is set here
	private void sendToAllClients(GameObject obj) throws IOException {
				
		Iterator<ObjectOutputStream> iter = this.list.iterator();
		
		switch (obj.getType()) {
		 case ChatMessageLobby:		 
			//sending the chat messages to all clients
				while (iter.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iter.next();
					current.writeObject(obj);
					current.flush();			
				} 
			 break;
		 
		 case NewGameParty:
			 NewGameParty game = (NewGameParty) obj;
			 
			 GamePartyOnServer newGameOnServer = new GamePartyOnServer (game);
			 Player current_player = new Player (game.getCreator(), this.out);			 
			 newGameOnServer.addPlayer(current_player);
			 
			 sl.addNewGame(newGameOnServer);
			 //Player creator
			 
			//sending the chat messages to all clients
				while (iter.hasNext()){
					ObjectOutputStream current = (ObjectOutputStream) iter.next();
					current.writeObject(obj);
					current.flush();
				}

			 break;
			 
		 case UpdateLobby:
			 UpdateLobby toUpdate = new UpdateLobby ();
			 
			 if(!sl.getGameListFromServer().isEmpty()){
				 ArrayList <NewGameParty> gamePartyListClient = new ArrayList <NewGameParty>();
				 Iterator <GamePartyOnServer> iter2 = sl.getGameListFromServer().iterator();
				 
				 while (iter2.hasNext()){
					 gamePartyListClient.add(iter2.next().getGameParty());
				 }
				 
				 toUpdate.setListOfOpenGames(gamePartyListClient);
				 
				 this.out.writeObject(toUpdate);
				 this.out.flush();
		
				 
			 }
			 break;
			 
		 case StartInformation:
			 StartInformation start = (StartInformation) obj;
			 
			 String username = start.getUsername();
			 
			 Player newPlayer = new Player (username, this.out);
			 
			 sl.addConnectedPlayer(newPlayer);
			 
			 /**Iterator<Player> iter2 = sl.getConnectedPlayers().iterator();
			 
			 while(iter2.hasNext()){
				 System.out.println(iter2.next().getUsername());
			 }*/

			 break;
			 
		 default:
		 }
	
	}
	
}
