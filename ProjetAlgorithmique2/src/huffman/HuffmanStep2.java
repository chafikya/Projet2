package huffman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;





public class HuffmanStep2 {

	public static void main(String[] args) {
		
		
		//String text = "aaabbcdd";
        int combien_caractere=0;
		String fileName = "textesimple.txt";

        // Lecture du fichier
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }
        scanner.close();

        String text = sb.toString();

        // Créer un arbre (feuille) pour chaque caractère de l'alphabet avec la fréquence associée
        HashMap<Character, Integer> freqTable = new HashMap<Character, Integer>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            freqTable.put(ch, freqTable.getOrDefault(ch, 0) + 1);
        }
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        
        for (char ch : freqTable.keySet()) {
            pq.offer(new Node(ch, freqTable.get(ch)));
            
            combien_caractere+=freqTable.get(ch) ;
            
        }
        System.out.println(combien_caractere);
        //System.out.println( pq);

        // Construire l'arbre
        while (pq.size() > 1) {
            Node t1 = pq.poll();
            //System.out.println("1"+ pq);
            Node t2 = pq.poll();
            //System.out.println("2"+ pq);

            Node t = new Node(t1.freq + t2.freq, t1, t2);
            pq.offer(t);
            System.out.println(t);

        }
        Node root = pq.poll();
        

        // Afficher les codes des caractères
        HashMap<Character, String> codes = new HashMap<Character, String>();
        generateCodes(root, "", codes);
        System.out.println("Codes des caractères :");
        
        for (char ch : codes.keySet()) {
            System.out.println(ch + " : " + codes.get(ch));
            
        }
        StringBuilder compressed = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            compressed.append(codes.get(ch));
        }

        // Enregistrement du fichier compressé
        try (FileOutputStream fos = new FileOutputStream("texts_comp.bin")) {
            String compressedStr = compressed.toString();
            while (compressedStr.length() > 0) {
                if (compressedStr.length() < 8) {
                    compressedStr += "00000000".substring(compressedStr.length());
                }
                int b = Integer.parseInt(compressedStr.substring(0, 8), 2);
                fos.write(b);
                compressedStr = compressedStr.substring(8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //taux de compression
        String originalFilePath = fileName;
        String compressedFilePath = "texts_comp.bin";

        // Récupérer la taille du fichier original
        File originalFile = new File(originalFilePath);
        long originalFileSize = originalFile.length();

        // Récupérer la taille du fichier compressé
        File compressedFile = new File(compressedFilePath);
        long compressedFileSize = compressedFile.length();

        // Calculer le taux de compression
        double compressionRatio = 1 - (double)compressedFileSize / originalFileSize;

        System.out.println("Taille du fichier original: " + originalFileSize + " octets");
        System.out.println("Taille du fichier compressé: " + compressedFileSize + " octets");
        System.out.println("Taux de compression: " + compressionRatio);
        //determination de la moyenne de bits d'un caractere
        int s =0;
        for (char ch : codes.keySet()) {
        	s+=codes.get(ch).length();
        	//System.out.println( s);
        }
        System.out.println("ciao" + combien_caractere);
        System.out.println(s/combien_caractere);
        
        	
	}
	 private static void generateCodes(Node node, String code, HashMap<Character, String> codes) {
	        if (node.isLeaf()) {
	            codes.put(node.ch, code);
	            //System.out.println( codes.put(node.ch, code));
	        } else {
	            generateCodes(node.left, code + "0", codes);
	            generateCodes(node.right, code + "1", codes);
	        }
	    }

}

