
package edu.fudan.ml.eval;

import java.text.DecimalFormat;

import edu.fudan.ml.classifier.hier.Linear;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.Tree;

public class Evaluation  {
	DecimalFormat df = new DecimalFormat("##.00");
	private int[] golden;
	private int numofclass;
	private Tree tree;
	private int totnum;
	private InstanceSet test;
	
	
	public Evaluation(InstanceSet test) {
		this.test = test;
		totnum=test.size();
		golden = new int[totnum];
		for(int i=0;i<totnum;i++){
			golden[i] = (Integer) test.getInstance(i).getTarget();
		}
		LabelAlphabet labels = test.getAlphabetFactory().buildLabelAlphabet("labels");
		numofclass = labels.size();
	}
	
	public Evaluation(InstanceSet test,Tree tree) {
		this(test);
		if(tree!=null){
			numofclass=tree.size;
			this.tree = tree;
		}
	}


	//Accuracy	Macro F-measure	Macro Precision	Macro Recall	Tree Induced Error
	public void eval(Linear cl) {
		int[] pred = cl.classify(test);
		double Accuracy;
		double MarcoF;
		double MacroPrecision = 0;
		double MacroRecall = 0;
		double Treeloss;

		
		double leafcor=0;
		double loss=0;
		double[] ttcon=new double[10];

		double[] truePositive=new double[numofclass];
		double[] falseNegative=new double[numofclass];
		double[] falsePositive=new double[numofclass];

		for(int i=0;i<totnum;i++){

			if(golden[i]==pred[i]){//正确
				leafcor++;
				truePositive[golden[i]]++;

			}
			else{	
				falsePositive[pred[i]]++;
				falseNegative[golden[i]]++;
				if(tree!=null){
					loss+=tree.dist(golden[i], pred[i]);
				}

			}
		}
		Treeloss=loss/totnum;
		Accuracy=leafcor/totnum;
		double count1=0;
		double count2=0;
		for(int i=0;i<numofclass;i++){
			double base = truePositive[i]+falsePositive[i]; 
			if(base>0)
				MacroPrecision+= truePositive[i]/base;
			else{
				count1++;	
			}
			base = truePositive[i]+falseNegative[i]; 
			if(base>0)
				MacroRecall+=truePositive[i]/base;
			else{
				count2++;
			}
		}
		System.out.println("Accuracy    MarcoF   MacroPrecision   MacroRecall   Treeloss");

		MacroPrecision/=(numofclass-count1);
		MacroRecall/=(numofclass-count2);
		MarcoF=2*MacroPrecision*MacroRecall/(MacroPrecision+MacroRecall+Double.MIN_VALUE);
		int i=0;
		
		Accuracy = Double.parseDouble(df.format(Accuracy*100));
		MarcoF = Double.parseDouble(df.format(MarcoF*100));
		MacroPrecision = Double.parseDouble(df.format(MacroPrecision*100));
		MacroRecall = Double.parseDouble(df.format(MacroRecall*100));
		Treeloss = Double.parseDouble(df.format(Treeloss));
		System.out.println(Accuracy+" "+ MarcoF+" "+ MacroPrecision+" "+ MacroRecall+" "+ Treeloss);

		while(ttcon[i]!=0){
			ttcon[i] = Double.parseDouble(df.format(ttcon[i]*100));
			System.out.println(""+i+"th level accurary: "+(double)ttcon[i]/totnum);
			i++;
		}


	}

}
