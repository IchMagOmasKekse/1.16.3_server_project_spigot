package me.ichmagomaskekse.de;

import java.util.Random;

public class Code {
	
	public static final String seperator = "-";
	public static final Random random = new Random();
	
	/* Generiert einen zufälligen Code, welcher beliebig anpassbar ist */
	public static String getRandomCode(int blocks, int blocksize) {
		if(blocks < 1) blocks = 1;
		if(blocksize < 2) blocksize = 2;
		
		String link = "";
		
		for(int i = 0; i != blocks; i++) {
			for(int ii = 0; ii != blocksize; ii++) {
				if(random.nextBoolean()) {
					//Buchstabe
					if(random.nextBoolean()) {
						//Großbuchstabe
						switch(random.nextInt(26)) {
						case  0: link = link + "A"; break;
						case  1: link = link + "B"; break;
						case  2: link = link + "C"; break;
						case  3: link = link + "D"; break;
						case  4: link = link + "E"; break;
						case  5: link = link + "F"; break;
						case  6: link = link + "G"; break;
						case  7: link = link + "H"; break;
						case  8: link = link + "I"; break;
						case  9: link = link + "J"; break;
						case 10: link = link + "K"; break;
						case 11: link = link + "L"; break;
						case 12: link = link + "M"; break;
						case 13: link = link + "N"; break;
						case 14: link = link + "O"; break;
						case 15: link = link + "P"; break;
						case 16: link = link + "Q"; break;
						case 17: link = link + "R"; break;
						case 18: link = link + "S"; break;
						case 19: link = link + "T"; break;
						case 20: link = link + "U"; break;
						case 21: link = link + "V"; break;
						case 22: link = link + "W"; break;
						case 23: link = link + "X"; break;
						case 24: link = link + "Y"; break;
						case 25: link = link + "Z"; break;
						}
					}else {
						switch(random.nextInt(26)) {
						case  0: link = link + "a"; break;
						case  1: link = link + "b"; break;
						case  2: link = link + "c"; break;
						case  3: link = link + "d"; break;
						case  4: link = link + "e"; break;
						case  5: link = link + "f"; break;
						case  6: link = link + "g"; break;
						case  7: link = link + "h"; break;
						case  8: link = link + "i"; break;
						case  9: link = link + "j"; break;
						case 10: link = link + "k"; break;
						case 11: link = link + "l"; break;
						case 12: link = link + "m"; break;
						case 13: link = link + "n"; break;
						case 14: link = link + "o"; break;
						case 15: link = link + "p"; break;
						case 16: link = link + "q"; break;
						case 17: link = link + "r"; break;
						case 18: link = link + "s"; break;
						case 19: link = link + "t"; break;
						case 20: link = link + "u"; break;
						case 21: link = link + "v"; break;
						case 22: link = link + "w"; break;
						case 23: link = link + "x"; break;
						case 24: link = link + "y"; break;
						case 25: link = link + "z"; break;
						}
						//Kleinbuchstabe
						
					}
				}else {
					//Zahl
					link = link + ""+random.nextInt(10);
				}
			}
			
			if((i == (blocks - 1)) == false) link = link+seperator;
		}
		return link;
	}
	
}
