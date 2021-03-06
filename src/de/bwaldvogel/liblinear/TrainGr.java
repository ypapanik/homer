/*
 * Copyright (C) 2015 Yannis Papanikolaou <ypapanik@csd.auth.gr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwaldvogel.liblinear;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import static de.bwaldvogel.liblinear.Linear.atof;
import static de.bwaldvogel.liblinear.Linear.atoi;

/**
 *
 * @author Yannis Papanikolaou <ypapanik@csd.auth.gr>
 */
public class TrainGr extends Train {

    private static ProblemGr constructProblem(List<Double> vy, List<Feature[]> vx, int max_index, double bias) {
        ProblemGr prob = new ProblemGr();
        prob.bias = bias;
        prob.l = vy.size();
        prob.n = max_index;
        if (bias >= 0) {
            prob.n++;
        }
        prob.x = new Feature[prob.l][];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = vx.get(i);

            if (bias >= 0) {
                assert prob.x[i][prob.x[i].length - 1] == null;
                prob.x[i][prob.x[i].length - 1] = new FeatureNode(max_index + 1, bias);
            }
        }

        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.y[i] = vy.get(i).doubleValue();
        }

        return prob;
    }

    /**
     * reads a problem from LibSVM format
     *
     * @param file the SVM file
     * @param bias
     * @param max_index
     * @return
     * @throws IOException obviously in case of any I/O exception ;)
     * @throws InvalidInputDataException if the input file is not correctly
     * formatted
     */
    public static ProblemGr readProblem(File file, double bias, int max_index) throws
            IOException, InvalidInputDataException {
        BufferedReader fp = new BufferedReader(new FileReader(file));
        List<Double> vy = new ArrayList<>();
        List<Feature[]> vx = new ArrayList<>();

        int lineNr = 0;

        try {
            while (true) {
                String line = fp.readLine();
                if (line == null) {
                    break;
                }
                lineNr++;

                StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
                String token;
                try {
                    token = st.nextToken();
                } catch (NoSuchElementException e) {
                    throw new InvalidInputDataException("empty line", file, lineNr, e);
                }

                try {
                    vy.add(atof(token));
                } catch (NumberFormatException e) {
                    throw new InvalidInputDataException("invalid label: " + token, file, lineNr, e);
                }

                int m = st.countTokens() / 2;
                Feature[] x;
                if (bias >= 0) {
                    x = new Feature[m + 1];
                } else {
                    x = new Feature[m];
                }
                int indexBefore = 0;
                for (int j = 0; j < m; j++) {

                    token = st.nextToken();
                    int index;
                    try {
                        index = atoi(token);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputDataException("invalid index: " + token, file, lineNr, e);
                    }

                    // assert that indices are valid and sorted
                    if (index < 0) {
                        throw new InvalidInputDataException("invalid index: " + index, file, lineNr);
                    }
                    if (index <= indexBefore) {
                        throw new InvalidInputDataException("indices must be sorted in ascending order", file, lineNr);
                    }
                    indexBefore = index;

                    token = st.nextToken();
                    try {
                        double value = atof(token);
                        x[j] = new FeatureNode(index, value);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputDataException("invalid value: " + token, file, lineNr);
                    }
                }
                vx.add(x);
            }

            return constructProblem(vy, vx, max_index, bias);
        } finally {
            fp.close();
        }
    }

    public static ProblemGr readProblem(double[][] features, double bias, ArrayList<Double> vy) {
        List<Feature[]> vx = new ArrayList<>();

        for (int i = 0; i < features.length; i++) {
            //vy.add(0.0);
            Feature[] x;
            if (bias >= 0) {
                x = new Feature[features[0].length + 1];
            } else {
                x = new Feature[features[0].length];
            }
            for (int j = 0; j < features[0].length; j++) {
                x[j] = new FeatureNode(j+1, features[i][j]);
            }
            vx.add(x);
        }
        return constructProblem(vy, vx, features[0].length, bias);
    }
}
