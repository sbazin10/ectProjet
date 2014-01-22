package projet;

import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		CorpusDeTweet corpus = new CorpusDeTweet();
		corpus.lireCorpusDeTweets("/net/k13/u/etudiant/sbazin10/Desktop/data/train.txt");
		
		BayesienNaif a = new BayesienNaif(corpus.getSizeCorpusDeMot());
		double kQuiBouge = 0.01;
		double meilleurK = 0;
		double meilleurReussite=0;
		for(int j=0;j<30;j++){	
			//On change le k
			kQuiBouge=kQuiBouge+0.01;
			a.changerK(kQuiBouge);
			
			//On analyse tout le corpus par cross validation
			for(int i=0;i<10;i++){
				System.out.println("Traitement du corpus "+ i);
				a.run(corpus.getCorpusTrain(i), corpus.getCorpusEval(i));
			}
			
			//On affiche la confusion
			a.afficherConfusion();
			if(meilleurReussite<a.bonneReponses()){				
				meilleurReussite = a.bonneReponses();
				meilleurK = kQuiBouge;
			}
			//On reset
			a.reset();
		}
		
		System.out.println();
		System.out.println("Meilleur resultat : "+meilleurReussite);
		System.out.println("Meilleur k : "+meilleurK);
		
		
	}

}