package projet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;



public class BayesienNaif {
	
	public int corpusDeMots;
	public LinkedHashMap<ArrayList<Integer>, String> tweetEntrainement;
	public LinkedHashMap<ArrayList<Integer>, String> tweetEvaluation;
	
	
	//Pour savoir le tweet
	public ArrayList<String> azd = new ArrayList<String>();
	
	//Le tableau des betas ?
	LinkedHashMap<double[], Integer> arrayMots = new LinkedHashMap<double[], Integer>();
	
	public int id;
	
	//Nombre de tweet d'entrainement pour chaque tag
	public int nbArabe = 0;
	public int nbChinois = 0;
	public int nbFrancais = 0;
	public int nbAllemand = 0;
	public int nbHindou = 0;
	public int nbItalien = 0;
	public int nbJaponais = 0;
	public int nbCoreen = 0;
	public int nbEspagnol = 0;
	public int nbTelugu = 0;
	public int nbTurque = 0;
	
	//Beta 
	public double[] tabArabe;
	public double[] tabChinois;
	public double[] tabFrancais;
	public double[] tabAllemand;
	public double[] tabHindou;
	public double[] tabItalien;
	public double[] tabJaponais;
	public double[] tabCoreen;
	public double[] tabEspagnol;
	public double[] tabTelugu;
	public double[] tabTurque;
	
	//Précalcul du alpha
	public double[] tabAlpha;
	
	//k
	public double k = 0.35;
	
	public LinkedHashMap<String, Integer> tabConfusionArabe = new LinkedHashMap<String, Integer>();	
	public LinkedHashMap<String, Integer> tabConfusionChinois = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionFrancais = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionAllemand = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionHindou = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionItalien = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionJaponais = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionCoreen = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionEspagnol = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionTelugu = new LinkedHashMap<String, Integer>();
	public LinkedHashMap<String, Integer> tabConfusionTurque = new LinkedHashMap<String, Integer>();
	
	public String pathToCorpus = "/net/k13/u/etudiant/sbazin10/Downloads/twitter/train.txt";
	
	public double bonnesReponses = 0;
	
	
	
	public BayesienNaif(int sizeCorpusDeMots){
		corpusDeMots = sizeCorpusDeMots;
		tweetEntrainement = new LinkedHashMap<ArrayList<Integer>, String>();
		tweetEvaluation = new LinkedHashMap<ArrayList<Integer>, String>();
		id = 0;
		
		init(tabConfusionArabe);
		init(tabConfusionChinois);
		init(tabConfusionFrancais);
		init(tabConfusionAllemand);
		init(tabConfusionHindou);
		init(tabConfusionItalien);
		init(tabConfusionJaponais);
		init(tabConfusionCoreen);
		init(tabConfusionEspagnol);
		init(tabConfusionTelugu);
		init(tabConfusionTurque);
	}
	
	public void init(LinkedHashMap<String, Integer> tab){
		tab.put("ARA", 0);
		tab.put("CHI", 0);		
		tab.put("FRE", 0);
		tab.put("GER", 0);
		tab.put("HIN", 0);
		tab.put("ITA", 0);
		tab.put("JPN", 0);
		tab.put("KOR", 0);
		tab.put("SPA", 0);
		tab.put("TEL", 0);
		tab.put("TUR", 0);
	}
	
	
	public void run(LinkedHashMap<ArrayList<Integer>, String> corpusTrain,
			LinkedHashMap<ArrayList<Integer>, String> corpusEval) {
		tweetEntrainement = corpusTrain;
		tweetEvaluation = corpusEval;
		traiterInfo();
		traiterCorpusEval();
		
	}


	
	
	
	public void traiterInfo(){
		
		int size = corpusDeMots;
		tabArabe = null;
		tabChinois = null;
		tabFrancais = null;
		tabAllemand = null;		
		tabHindou = null;
		tabItalien = null;
		tabJaponais = null;
		tabCoreen = null;
		tabEspagnol = null;
		tabTelugu = null;
		tabTurque = null;
		System.gc();
		tabArabe = new double[size];
		tabChinois = new double[size];
		tabFrancais = new double[size];
		tabAllemand = new double[size];		
		tabHindou = new double[size];
		tabItalien = new double[size];
		tabJaponais = new double[size];
		tabCoreen = new double[size];
		tabEspagnol = new double[size];
		tabTelugu = new double[size];
		tabTurque = new double[size];
		
		
		nbArabe = 0;
		nbChinois = 0;
		nbFrancais = 0;
		nbAllemand = 0;
		nbHindou = 0;
		nbItalien = 0;
		nbJaponais = 0;
		nbCoreen = 0;
		nbEspagnol = 0;
		nbTelugu = 0;
		nbTurque = 0;
		
		tabAlpha = new double[11];
		int i=0;
		
		for (final Entry<ArrayList<Integer>, String> entry : tweetEntrainement.entrySet()) {
			double tab[] = new double[1];
			//Calcul du nombre de tweet de chaque classe, et on affecte a tab le tableau beta correspondant pour ajouter les mots
			if(entry.getValue().contains("ARA")){
				nbArabe++;
				tab = tabArabe;
			}
			else if(entry.getValue().contains("CHI")){
				nbChinois++;
				tab = tabChinois;
			}
			else if(entry.getValue().contains("FRE")){
				nbFrancais++;
				tab = tabFrancais;
			}
			else if(entry.getValue().contains("GER")){
				nbAllemand++;
				tab = tabAllemand;
			}
			else if(entry.getValue().contains("HIN")){
				nbHindou++;
				tab = tabHindou;
			}
			else if(entry.getValue().contains("ITA")){
				nbItalien++;
				tab = tabItalien;
			}
			else if(entry.getValue().contains("JPN")){
				nbJaponais++;
				tab = tabJaponais;
			}
			else if(entry.getValue().contains("KOR")){
				nbCoreen++;
				tab = tabCoreen;
			}
			else if(entry.getValue().contains("SPA")){
				nbEspagnol++;
				tab = tabEspagnol;
			}
			else if(entry.getValue().contains("TEL")){
				nbTelugu++;
				tab = tabTelugu;
			}
			else if(entry.getValue().contains("TUR")){
				nbTurque++;
				tab = tabTurque;
			}
			
			//Remplissage du tableau beta
			for(int s : entry.getKey()){
				tab[s]++;				
			}		
		}
		
		LinkedHashMap<double[], Integer> array = new LinkedHashMap<double[], Integer>();
		array.put(tabArabe,nbArabe);
		array.put(tabChinois, nbChinois);
		array.put(tabFrancais, nbFrancais);
		array.put(tabAllemand, nbAllemand);
		array.put(tabHindou, nbHindou);
		array.put(tabItalien, nbItalien);
		array.put(tabJaponais, nbJaponais);
		array.put(tabCoreen, nbCoreen);
		array.put(tabEspagnol, nbEspagnol);
		array.put(tabTelugu, nbTelugu);		
		array.put(tabTurque, nbTurque);
		
		for (final Entry<double[], Integer> entry : array.entrySet()) {
			for(int j=0;j<entry.getKey().length;j++){	
				double d = entry.getKey()[j];
				if(d<k){
					d=k;
				}
				else if(d>(entry.getValue()-k)){
					d=entry.getValue()-k;
				}
				d = d/entry.getValue();
				entry.getKey()[j] = d;
				tabAlpha[i] = tabAlpha[i]+Math.log(1-d);				
			}
			i++;
		}
		arrayMots = array;
		
		/*System.out.println("Positif : "+nbPositif);
		System.out.println("Negatif : "+nbNegatif);
		System.out.println("Irrelevant : "+nbIrrelevant);
		System.out.println("Neutre : "+nbNeutre);*/		
	}
	
	
	public void traiterCorpusEval(){
		int i=0;
		int correct=0;
		LinkedHashMap<String, Integer> tab = new LinkedHashMap<String, Integer>();
		
		for (final Entry<ArrayList<Integer>, String> entry : tweetEvaluation.entrySet()) {		

			switch(entry.getValue()){
				case "ARA" : tab = tabConfusionArabe;
				break;
				case "CHI" : tab = tabConfusionChinois;
				break;
				case "FRE" : tab = tabConfusionFrancais;
				break;	
				case "GER" : tab = tabConfusionAllemand;
				break;
				case "HIN" : tab = tabConfusionHindou;
				break;	
				case "ITA" : tab = tabConfusionItalien;
				break;	
				case "JPN" : tab = tabConfusionJaponais;
				break;	
				case "KOR" : tab = tabConfusionCoreen;
				break;	
				case "SPA" : tab = tabConfusionEspagnol;
				break;	
				case "TEL" : tab = tabConfusionTelugu;
				break;	
				case "TUR" : tab = tabConfusionTurque;
				break;	
										
			}
						
			int type = traiterTweet(entry.getKey(), entry.getValue());	
			if(type == 0){
				int a = tab.get("ARA");
				a++;
				tab.put("ARA",a);
				if(entry.getValue().contains("ARA")){
					correct++;
				}
			}
			else if(type == 1){
				int a = tab.get("CHI");
				a++;
				tab.put("CHI",a);
				if(entry.getValue().contains("CHI")){
					correct++;
				}
			}
			else if(type == 2){
				int a = tab.get("FRE");
				a++;
				tab.put("FRE",a);
				if(entry.getValue().contains("FRE")){
					correct++;
				}
			}
			else if(type == 3){
				int a = tab.get("GER");
				a++;
				tab.put("GER",a);
				if(entry.getValue().contains("GER")){
					correct++;
				}
			}
			else if(type == 4){
				int a = tab.get("HIN");
				a++;
				tab.put("HIN",a);
				if(entry.getValue().contains("HIN")){
					correct++;
				}
			}
			else if(type == 5){
				int a = tab.get("ITA");
				a++;
				tab.put("ITA",a);
				if(entry.getValue().contains("ITA")){
					correct++;
				}
			}
			else if(type == 6){
				int a = tab.get("JPN");
				a++;
				tab.put("JPN",a);
				if(entry.getValue().contains("JPN")){
					correct++;
				}
			}
			else if(type == 7){
				int a = tab.get("KOR");
				a++;
				tab.put("KOR",a);
				if(entry.getValue().contains("KOR")){
					correct++;
				}
			}
			else if(type == 8){
				int a = tab.get("SPA");
				a++;
				tab.put("SPA",a);
				if(entry.getValue().contains("SPA")){
					correct++;
				}
			}
			else if(type == 9){
				int a = tab.get("TEL");
				a++;
				tab.put("TEL",a);
				if(entry.getValue().contains("TEL")){
					correct++;
				}
			}
			else if(type == 10){
				int a = tab.get("TUR");
				a++;
				tab.put("TUR",a);
				if(entry.getValue().contains("TUR")){
					correct++;
				}
			}
			i++;
		}
		
		bonnesReponses += (double)correct/(double)i;
		
		System.out.println("Total de tweet dans le corpusEval : "+i);
		System.out.println("Bonnes réponses : "+(double)correct/(double)i);			
	}
	
	
	public int traiterTweet(ArrayList<Integer> tweet, String tweetComplet){
		double result = Integer.MIN_VALUE;
		int type = Integer.MIN_VALUE;
		int i = 0;
		
		for (final Entry<double[], Integer> entry : arrayMots.entrySet()) {
			double resultInter = tabAlpha[i];			
			for(int mot : tweet){		
				resultInter = resultInter + Math.log(entry.getKey()[mot]);
				resultInter = resultInter - Math.log(1-entry.getKey()[mot]);
			}
			double salut = entry.getValue()/(double)tweetEntrainement.size();
			resultInter = resultInter+Math.log(salut);
			if(resultInter> result){
				result = resultInter;
				type = i;
			}
			i++;
		}
		
		return type;		
	}
	
	public void afficherConfusion(){
		
		System.out.println("\tARA\tCHI\tFRE\tGER\tHIN\tITA\tJPN\tKOR\tSPA\tTE\tTUR\t");
		System.out.print("ARA\t");
		for (final Entry<String, Integer> entry : tabConfusionArabe.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("CHI\t");
		for (final Entry<String, Integer> entry : tabConfusionChinois.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}		
		System.out.println();
		System.out.print("FRE\t");
		for (final Entry<String, Integer> entry : tabConfusionFrancais.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("GER\t");
		for (final Entry<String, Integer> entry : tabConfusionAllemand.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("HIN\t");
		for (final Entry<String, Integer> entry : tabConfusionHindou.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("ITA\t");
		for (final Entry<String, Integer> entry : tabConfusionItalien.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("JPN\t");
		for (final Entry<String, Integer> entry : tabConfusionJaponais.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("KOR\t");
		for (final Entry<String, Integer> entry : tabConfusionCoreen.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("SP\t");
		for (final Entry<String, Integer> entry : tabConfusionEspagnol.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("TEL\t");
		for (final Entry<String, Integer> entry : tabConfusionTelugu.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		System.out.println();
		System.out.print("TUR\t");
		for (final Entry<String, Integer> entry : tabConfusionTurque.entrySet()) {
			System.out.print(entry.getValue()+"\t");
		}
		
		
		System.out.println();
		System.out.println("Bonnes reponses sur le corpus "+(bonnesReponses/10)*100+"%");
	}
	
	public double bonneReponses(){
		return ((bonnesReponses/10)*100);
	}
	
	public void reset(){
		bonnesReponses = 0;
		tabConfusionArabe = new LinkedHashMap<String, Integer>();	
		tabConfusionChinois = new LinkedHashMap<String, Integer>();
		tabConfusionFrancais = new LinkedHashMap<String, Integer>();
		tabConfusionAllemand = new LinkedHashMap<String, Integer>();
		tabConfusionHindou = new LinkedHashMap<String, Integer>();
		tabConfusionItalien = new LinkedHashMap<String, Integer>();
		tabConfusionJaponais = new LinkedHashMap<String, Integer>();
		tabConfusionCoreen = new LinkedHashMap<String, Integer>();
		tabConfusionEspagnol = new LinkedHashMap<String, Integer>();
		tabConfusionTelugu = new LinkedHashMap<String, Integer>();
		tabConfusionTurque = new LinkedHashMap<String, Integer>();
		
		init(tabConfusionArabe);
		init(tabConfusionChinois);
		init(tabConfusionFrancais);
		init(tabConfusionAllemand);
		init(tabConfusionHindou);
		init(tabConfusionItalien);
		init(tabConfusionJaponais);
		init(tabConfusionCoreen);
		init(tabConfusionEspagnol);
		init(tabConfusionTelugu);
		init(tabConfusionTurque);
		
	}
	
	public void changerK(double newK){
		k = newK;		
	}
	
	






}