package main;

import java.util.*;

import server.Server;
import client.Client;


public class Main
{

	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("To run as a server enter ----> S(erver)\n"
				+ "To run as a client enter ---> C(lient)");
		while (true){
			char answer = Character.toLowerCase(in.nextLine().charAt(0));
			if (answer == 's'){
				new Server();
				break;
			} 
			else if (answer == 'c'){
				new Client();
				break;
			} 
			else{
				System.out.println("Input Error");
			}
		}

		in.close();
	}

}