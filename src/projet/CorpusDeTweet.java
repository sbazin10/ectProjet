package projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class CorpusDeTweet {
	
	
	public ArrayList<LinkedHashMap<ArrayList<Integer>, String>> corpusDeTweet = new ArrayList<LinkedHashMap<ArrayList<Integer>, String>>();
	public HashMap<String, Integer> corpusDeMots;
	public int sizeCorpusDeMot = 0;
	public int id;
	
	public String ponctuation = ",.:;^<>~()[]{}/@#=+-$?!\"-&";
	public int ngramme = 2;
	
	public CorpusDeTweet(){
		corpusDeMots = new HashMap<String, Integer>();
		for(int i=0;i<10;i++){
			corpusDeTweet.add(new LinkedHashMap<ArrayList<Integer>, String>());
		}			
	}
	
	
	
	public void lireCorpusDeTweets(String pathToCorpus) throws IOException{
		LinkedHashMap<ArrayList<Integer>, String> currentMap;
		BufferedReader reader = new BufferedReader(new FileReader(new File(pathToCorpus)));
		String ligne = "";
		int b=0;
		while ((ligne = reader.readLine()) != null) {
			b++;
			
			if(b%200 == 0){
				System.out.println(b);
				System.gc();
			}
			String[] explodedLine = ligne.split(" ");
			String tag = explodedLine[0].split(",")[0];
			tag = tag.substring(1,tag.length());
			

			ArrayList<Integer> motsDuTweet = new ArrayList<Integer>();
			ArrayList<String> mots = new ArrayList<String>();
			for(int i=0;i<explodedLine.length;i++){
				explodedLine[i] = traiterMot(explodedLine[i]);
				if(!explodedLine[i].equals("")){
					mots.add(explodedLine[i]);
				}
			}
			
			for(int i=1;i<=ngramme;i++){
				traiterTweet(mots,motsDuTweet, i);
			}
			//System.out.println(corpusDeMots.size());
			
			/*for(int i=1;i<explodedLine.length;i++){
				String mot = explodedLine[i];
				
				//mot = traiterMot(mot);
				try{
					ArrayList<String> nmots = new ArrayList<String>();
					for(int j=0;j<ngramme;j++){
						nmots.add(traiterMot(explodedLine[i+j+1]));						
					}
					//bigramme(mot, traiterMot(explodedLine[i+1]), motsDuTweet);
					ngramme(nmots, motsDuTweet, ngramme);
				}
				catch(Exception e){}

				if(!corpusDeMots.containsKey(mot)){
					corpusDeMots.put(mot,id);
					motsDuTweet.add(id);
					id++;
				}
				else{
					motsDuTweet.add(corpusDeMots.get(mot));					
				}							
			}*/
						
			int a = (int) (Math.random()*10);
			currentMap = corpusDeTweet.get(a);
			currentMap.put(motsDuTweet,tag);
		}
		
		for(int i=0;i<10;i++){
			int taille = corpusDeTweet.get(i).size();
			System.out.println("Corpus "+i+" : "+taille+" tweets");
		}
		//faire size
		//size
		//corpusDeMots = null;
		//System.gc();
		
	}
	
	public void traiterTweet(ArrayList<String> mots, ArrayList<Integer> motsDuTweet, int ngramme){
		for(int i=0;i<mots.size();i++){
			try{
				String motAAjouter = "";
				for(int j=0;j<ngramme;j++){
					motAAjouter = motAAjouter +" "+ mots.get(i+j);
				}

				if(!corpusDeMots.containsKey(motAAjouter)){
					corpusDeMots.put(motAAjouter,id);
					motsDuTweet.add(id);
					id++;
				}
				else{
					motsDuTweet.add(corpusDeMots.get(motAAjouter));					
				}	
			}
			catch(Exception e){}
			
		}
		
	}
	
	public String traiterMot(String mot){
		//System.out.print(mot);
		
		mot = supprimerMajuscules(mot);
		mot = supprimerPonctuation(mot);
		
		//System.out.println(" "+mot);
		return mot;
	}
	
	public void bigramme(String mot1, String mot2, ArrayList<Integer> motsDuTweet){
		String s = mot1+" "+mot2;
		//System.out.println(s);
		if(!corpusDeMots.containsKey(s)){
			corpusDeMots.put(s,id);
			motsDuTweet.add(id);
			id++;
		}
		else{
			motsDuTweet.add(corpusDeMots.get(s));					
		}	
		
	}
	
	public void ngramme(ArrayList<String> nmots, ArrayList<Integer> motsDuTweet, int ngramme){		
		for(int i=0;i<nmots.size();i++){
			try{
				String motAAjouter = "";
				for(int j=0;j<ngramme;j++){
					motAAjouter = motAAjouter +" "+ nmots.get(i+j);
				}
				if(!corpusDeMots.containsKey(motAAjouter)){
					corpusDeMots.put(motAAjouter,id);
					motsDuTweet.add(id);
					id++;
				}
				else{
					motsDuTweet.add(corpusDeMots.get(motAAjouter));					
				}	
			}
			catch(Exception e){}
			
		}		
		if(ngramme>2){
			ngramme(nmots, motsDuTweet, ngramme-1);
		}		
	}
	
	public String supprimerMajuscules(String mot){
		mot = mot.toLowerCase();
		return mot;
	}
	
	public String supprimerPonctuation(String mot){
		String result = "";
		for(int i=0;i<mot.length();i++){
			if(!ponctuation.contains(Character.toString(mot.charAt(i)))){
				result=result+Character.toString(mot.charAt(i));
			}			
		}		
		return result;
	}
	
	public LinkedHashMap<ArrayList<Integer>, String> getCorpusEval(int numero){
		return corpusDeTweet.get(numero);
	}
	
	public LinkedHashMap<ArrayList<Integer>, String> getCorpusTrain(int numero){
		LinkedHashMap<ArrayList<Integer>, String> result = new LinkedHashMap<ArrayList<Integer>, String>();
		for(int i =0;i<10;i++){
			if(i!= numero){
				result.putAll(corpusDeTweet.get(i));				
			}					
		}
		return result;
	}
	
	public int getSizeCorpusDeMot(){
		return corpusDeMots.size();
	}	

}