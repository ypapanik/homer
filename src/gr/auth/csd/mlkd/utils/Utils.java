package gr.auth.csd.mlkd.utils;

import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Grigorios Tsoumakas
 * @author Yannis Papanikolaou
 * @version 2015.2.9
 */
public class Utils {

    public static TIntObjectHashMap<TreeSet<Integer>> loadLabels(String filenameLabels) {
        try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filenameLabels)))) {
            System.out.println(new Date() + " loading training labels...");
            TIntObjectHashMap<TreeSet<Integer>> labelValuesTemp = (TIntObjectHashMap<TreeSet<Integer>>) input.readObject();
            System.out.println(new Date() + " Finished.");
            return labelValuesTemp;
        } catch (ClassNotFoundException | IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public static int minIndex(double[] doubles) {

        int minIndex = 0;
        double minimum = doubles[0];

        for (int i = 1; i < doubles.length; i++) {
            if (doubles[i] < minimum) {
                minIndex = i;
                minimum = doubles[i];
            }
        }

        return minIndex;
    }

    public static int maxIndex(double[] doubles) {

        int maxIndex = 0;
        double maximum = doubles[0];

        for (int i = 1; i < doubles.length; i++) {
            if (doubles[i] > maximum) {
                maxIndex = i;
                maximum = doubles[i];
            }
        }

        return maxIndex;
    }

    public static String maxIndex(TObjectDoubleHashMap<String> map) {
        //System.out.println(map.toString());
        String maxIndex = "";
        double max = Double.MIN_VALUE;
        TObjectDoubleIterator<String> it = map.iterator();
        while (it.hasNext()) {
            it.advance();
            String label = it.key();
            //System.out.println(label+" "+it.value() +" "+max);
            if (it.value() > max) {
                maxIndex = label;
                max = it.value();
            }
        }
        //System.out.println(maxIndex);
        return maxIndex;
    }

    public static int maxIndex(TIntDoubleHashMap doubles) {

        int maxIndex = 0;
        double maximum = -1;
        TIntDoubleIterator it = doubles.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value() > maximum) {
                maxIndex = it.key();
                maximum = it.value();
            }
        }
        return maxIndex;
    }

    public static boolean isNumber(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        int i = 0;
        if (string.charAt(0) == '-') {
            if (string.length() > 1) {
                i++;
            } else {
                return false;
            }
        }
        for (; i < string.length(); i++) {
            if (!(Character.isDigit(string.charAt(i)) || string.charAt(i) == '.' || string.charAt(i) == ',')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get CPU time in nanoseconds.
     */
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported()
                ? bean.getCurrentThreadCpuTime() : 0L;
    }

    /**
     * Get user time in nanoseconds.
     */
    public static long getUserTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported()
                ? bean.getCurrentThreadUserTime() : 0L;
    }

    /**
     * Get system time in nanoseconds.
     */
    public static long getSystemTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported()
                ? (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime()) : 0L;
    }

    public static String concatenateArrayOfStrings(List<String> ls) {
        String s = "";
        for (int i = 0; i < ls.size() - 1; i++) {
            s += ls.get(i) + " ";
        }
        s += ls.get(ls.size() - 1);
        return s;
    }

    //to account for cases like e.g. 5.49 ->6 instead of 5
    public static long round(double num) {
        double temp = (double) Math.round(num * 10) / 10;
        return Math.round(temp);
    }

    public static double sqrt(double num) {
        double temp = num;
        if (temp < 0 && temp > -1E-16) {
            temp = 0;
        }
        return Math.sqrt(temp);
    }

    public static double factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public static int max(double a, double b, double c) {
        int index = 1;
        if (b > a) {
            if (b > c) {
                index = 2;
            } else {
                index = 3;
            }
        } else if (c > a) {
            index = 3;
        }
        return index;
    }

    public static double max(double a[]) {
        int index = 0;
        double max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
                index = i;
            }
        }
        return max;
    }

    public static int maxIndex(TDoubleArrayList f) {
        int index = 0;
        for (int i = 1; i < f.size(); i++) {
            if (f.get(i) > f.get(index)) {
                index = i;
            }
        }
        return index;
    }

    public static double[] normalize(double arr[], double max) {
        //normalize probabilities
        double m = 0;
        for (int x = 0; x < arr.length; x++) {

            m += arr[x];
        }
        if (m == 0) {
            return arr; //System.out.println(Arrays.toString(arr));
        }
        double r = max / m;
        // normalize the array
        for (int x = 0; x < arr.length; x++) {
            arr[x] = arr[x] * r;
        }
        return arr;
    }

    public static TIntDoubleHashMap normalize(TIntDoubleHashMap arr, double max) {
        //normalize probabilities
        double m = 0;
        TIntDoubleIterator it = arr.iterator();
        while (it.hasNext()) {
            it.advance();
            m += it.value();
        }
        if (m == 0) {
            return arr; //System.out.println(Arrays.toString(arr));
        }
        double r = max / m;
        // normalize the array
        it = arr.iterator();
        while (it.hasNext()) {
            it.advance();
            it.setValue(it.value() * r);
        }
        return arr;
    }

    public static TObjectDoubleHashMap<String> normalize(TObjectDoubleHashMap<String> arr, int max) {
        //normalize probabilities
        double m = 0;
        TObjectDoubleIterator<String> it = arr.iterator();
        while (it.hasNext()) {
            it.advance();
            m += it.value();
        }
        if (m == 0) {
            return arr; //System.out.println(Arrays.toString(arr));
        }
        double r = max / m;
        // normalize the array
        it = arr.iterator();
        while (it.hasNext()) {
            it.advance();
            it.setValue(it.value() * r);
        }
        return arr;
    }

    public static void log(String string) {
        System.out.println(string);
    }

    public static double[] normalizeVector(double[] vector) {
        double length = 0;
        for (double d : vector) {
            length += d * d;
        }
        length = Math.sqrt(length);
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= length;
        }
        return vector;
    }

    public static void printNoZero(double[] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                log(i + ":" + a[i]);
            }
        }
    }

    public static double average(TIntHashSet h) {
        TIntIterator i = h.iterator();
        double avg = 0;
        while (i.hasNext()) {
            avg += i.next();
        }
        return avg / h.size();
    }

    //standard deviation
    public static double std(TIntHashSet h) {
        TIntIterator i = h.iterator();
        double avg = average(h);
        double std = 0;
        while (i.hasNext()) {
            int next = i.next();
            std += (next - avg) * (next - avg);
        }
        return Math.sqrt(std / h.size());
    }

    public static int numberOfLines(File f) {
        LineNumberReader lnr;
        try {
            lnr = new LineNumberReader(new FileReader(f));
            lnr.skip(Long.MAX_VALUE);
            int num = lnr.getLineNumber();
            lnr.close();
            return num;
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public static double summation(TIntDoubleHashMap a) {
        double sum = 0;
        TIntDoubleIterator it = a.iterator();
        while (it.hasNext()) {
            it.advance();
            sum += it.value();
        }
        return sum;
    }

    public static double summation(double a[]) {
        double sum = 0;
        for (double d : a) {
            sum += d;
        }
        return sum;
    }

    public static double innerProduct(double a[], double b[]) {
        if (a.length != b.length) {
            System.out.println("Different length of arrays:" + a.length + " " + b.length);
            return -1;
        }
        double p = 0;
        for (int i = 0; i < a.length; i++) {
            p += a[i] * b[i];
        }
        return p;
    }

    public static double[][] trasposeMatrix(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        double[][] trasposedMatrix = new double[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                trasposedMatrix[x][y] = matrix[y][x];
            }
        }

        return trasposedMatrix;
    }

    public static double min(TIntDoubleHashMap map) {
        double min = Double.MAX_VALUE;
        TIntDoubleIterator it = map.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value() < min) {
                min = it.value();
            }
        }
        return min;
    }

    public static double min(double[] a) {
        double min = Double.MAX_VALUE;
        for (double value : a) {
            if (min > value) {
                min = value;
            }
        }
        return min;
    }

    public static void insertionSort(double[] arr) {
        for (int i = 1; i < arr.length; i++) {
            double valueToSort = arr[i];
            int j = i;
            while (j > 0 && arr[j - 1] < valueToSort) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = valueToSort;
        }

    }

    public static void writeObject(Object o, String file) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(o);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Object readObject(String file) {
        Object o = null;
        if (file == null) {
            return o;
        }
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            o = input.readObject();
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
    }

    public static TreeMap<String, TObjectDoubleHashMap<String>> prune(TreeMap<String, TObjectDoubleHashMap<String>> probMap) {
        Iterator<Map.Entry<String, TObjectDoubleHashMap<String>>> it = probMap.entrySet().iterator();
        TreeMap<String, TObjectDoubleHashMap<String>> probMap2 = new TreeMap<>();
        //System.out.println(probMap);
        while (it.hasNext()) {
            Map.Entry<String, TObjectDoubleHashMap<String>> next = it.next();
            TObjectDoubleHashMap<String> value = next.getValue();
            TObjectDoubleHashMap<String> prunedValue = new TObjectDoubleHashMap<>();
            int threshold = (value.size() > 200) ? 200 : value.size();
            for (int i = 0; i < threshold; i++) {
                String maxIndex = Utils.maxIndex(value);
                prunedValue.put(maxIndex, value.get(maxIndex));
                value.put(maxIndex, Double.MIN_VALUE);
            }
            probMap2.put(next.getKey(), prunedValue);
        }
        //System.out.println(probMap2);
        return probMap2;
    }

    public static double max(TObjectIntHashMap<String> pi) {
        TObjectIntIterator<String> it = pi.iterator();
        double max = Double.MIN_VALUE;
        while (it.hasNext()) {
            it.advance();
            if (it.value() > max) {
                max = it.value();
            }
        }
        return max;
    }

    public static double min(TObjectIntHashMap<String> pi) {
        TObjectIntIterator<String> it = pi.iterator();
        double min = Double.MAX_VALUE;
        while (it.hasNext()) {
            it.advance();
            if (it.value() < min) {
                min = it.value();
            }
        }
        return min;
    }

    public static int countLines(String file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lines = 0;
            while (reader.readLine() != null) {
                lines++;
            }
            return lines;
        } catch (Exception ex) {
        }
        return -1;
    }


    private static double norm(TIntDoubleHashMap a) {
        double norm = 0;
        for (double value : a.values()) {
            norm += value * value;
        }
        return Math.sqrt(norm);
    }

    public static double cosineSimilarity(TIntDoubleHashMap testDocument, TIntDoubleHashMap trainDocument) {

        TIntDoubleIterator it = (trainDocument.size() > testDocument.size())
                ? trainDocument.iterator()
                : testDocument.iterator();
        double cs;
        double sum = 0.0;
        while (it.hasNext()) {
            it.advance();
            double value2 = (trainDocument.size() > testDocument.size())
                    ? testDocument.get(it.key())
                    : trainDocument.get(it.key());
            if (value2 != -1) {
                sum += it.value() * value2;
            }
        }
        double norm1 = norm(testDocument);
        double norm2 = norm(trainDocument);
        if (norm1 * norm2 != 0) {
            cs = sum / (norm1 * norm2);
        } else {
            cs = 0;
        }
        //System.out.println(/*testDocument+" "+trainDocument+" "+*/sum+" "+norm1+" "+norm2+" "+cs);
        return cs; //sum
    }

    public static double hellingerDistance(TIntDoubleHashMap get, TIntDoubleHashMap get0) {
        double hd = 0;
        TIntDoubleIterator it1 = get.iterator();
        //TIntDoubleIterator it2 = get0.iterator();
        while (it1.hasNext() /*&&it2.hasNext()*/) {
            it1.advance();
            //it2.advance();
            double pi = it1.value();
            //double qi = it2.value();
            hd += Math.pow(Math.sqrt(pi) - Math.sqrt(get0.get(it1.key())), 2);
        }
        hd = -(1.0 / Math.sqrt(2)) * Math.sqrt(hd);
        return hd;
    }

    public static double jaccard(TIntDoubleHashMap testDocument, TIntDoubleHashMap trainDocument) {

        return intersection(testDocument.keySet(), trainDocument.keySet()) / union(testDocument.keySet(), trainDocument.keySet());
    }

    private static int intersection(TIntSet k, TIntSet k2) {
        int count = 0;
        //if (k.size() <= k2.size()) {
        TIntIterator it = k.iterator();
        while (it.hasNext()) {
            if (k2.contains(it.next())) {
                count++;
            }
        }
        //}
        return count;
    }

    public static void move(String source, String target) {
        try {
            Files.move(Paths.get(new File(source).getAbsolutePath()),
                    Paths.get(new File(target).getAbsolutePath()), REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void rename(String models, String modelsVanilla) {
        boolean success = new File(models).renameTo(new File(modelsVanilla));
    }

    public static int max(TIntDoubleHashMap[] readPhi) {
        int max = -1;
        for (TIntDoubleHashMap fi : readPhi) {
            TIntIterator it = fi.keySet().iterator();
            while (it.hasNext()) {
                int word = it.next();
                if (word > max) {
                    max = word;
                }
            }
        }
        return max;
    }

    public static void concatFiles(String dir, String file) {
        try {
            // Input files

            File[] files = new File(dir).listFiles((File dir1, String name) -> name.startsWith(file));
            List<String> lines = new ArrayList<>();
            for (File f : files) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(f), Charset.forName("UTF-8")));
                while ((line = br.readLine()) != null) {
                    lines.add(line+"\n");
                }
                //System.out.println(f+" "+lines.size());
            }
            try (FileWriter writer = new FileWriter(file)) {
                for (String str : lines) {
                    writer.write(str);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int union(TIntSet k, TIntSet k2) {
        int count = 0;
        TIntIterator it = k.iterator();
        count = k.size() + k2.size();
        while (it.hasNext()) {
            if (k2.contains(it.next())) {
                count--;
            }
        }
        //}
        return count;
    }

}
