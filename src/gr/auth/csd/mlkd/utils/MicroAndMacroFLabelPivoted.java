package gr.auth.csd.mlkd.utils;

import gr.auth.csd.mlkd.preprocessing.CorpusJSON;
import gr.auth.csd.mlkd.preprocessing.Document;
import gr.auth.csd.mlkd.preprocessing.Labels;
import gr.auth.csd.mlkd.utils.Timer;
import java.util.TreeSet;

/**
 *
 * @author Grigorios Tsoumakas
 * @author Yannis Papanikolaou
 * @version 2013.07.24
 */
public class MicroAndMacroFLabelPivoted extends Evaluator {

    int numLabels;

    public MicroAndMacroFLabelPivoted(String labelsFile, String folderTest, String filenamePredictions) {
        labels = Labels.readLabels(labelsFile);
        numLabels = labels.getSize();
        corpus = new CorpusJSON(folderTest);
        this.filenamePredictions = filenamePredictions;
    }

    public MicroAndMacroFLabelPivoted(Labels labels, CorpusJSON corpus, String filenamePredictions) {
        this.labels = labels;
        numLabels = labels.getSize();
        this.corpus = corpus;
        this.filenamePredictions = filenamePredictions;
    }

    @Override
    public void evaluate() {
        super.readBipartitions();
        corpus.reset();
        //load predicted labels
        double[] tp, fp, tn, fn;
        tp = new double[numLabels];
        fp = new double[numLabels];
        tn = new double[numLabels];
        fn = new double[numLabels];

        double df = 0;

        Document doc;
        while ((doc = corpus.nextDocument()) != null) {
            TreeSet<String> truth = getTruth(doc);
            TreeSet<String> pred = bipartitions.get(doc.getId());
            //System.out.println(doc.getId()+" "+truth+" "+pred);
            if(pred==null) {
                System.out.println("Null predictions for "+doc.getId());
                continue;
            }
            ConfusionMatrix cm = new ConfusionMatrix(pred, truth, labels, tp, fn, fp, tn);
        }

        double macroF = 0;
        double tpa = 0;
        double fpa = 0;
        double tna = 0;
        double fna = 0;
        for (int i = 0; i < numLabels; i++) {
            //System.out.print("Label " + labels.getLabel(i + 1) + " " + (i + 1) + " ");
            //System.out.printf("tp %.0f ", tp[i]);
            tpa += tp[i];
            //System.out.printf("fp %.0f ", fp[i]);
            fpa += fp[i];
            //System.out.printf("tn %.0f ", tn[i]);
            tna += tn[i];
            //System.out.printf("fn %.0f ", fn[i]);
            fna += fn[i];
            double f = 2.0 * tp[i] / (2.0 * tp[i] + fp[i] + fn[i]);
            if (new Double(f).isNaN()) {
                f = 1;
            }
            macroF += f;
            //System.out.printf("f %.2f", f);
            //System.out.println("");
        }

        /*System.out.println(
         "F: " + df / corpus.getCorpusSize());
         */ System.out.println(
                "MacroF: " + macroF / numLabels);
        double microF = 2.0 * tpa / (2.0 * tpa + fpa + fna);

        System.out.println(tpa + ", " + fpa + ", " + fna);
        System.out.println(
                "MicroF: " + microF);

    }

    public static void main(String[] args) {
        Timer timer = new Timer();

        String labelsFile = args[0];
        String folderTest = args[1];
        String filenamePredictions = args[2];
        MicroAndMacroFLabelPivoted ev = new MicroAndMacroFLabelPivoted(labelsFile, folderTest, filenamePredictions);
        ev.evaluate();
        System.out.println(timer.duration());
    }
}
