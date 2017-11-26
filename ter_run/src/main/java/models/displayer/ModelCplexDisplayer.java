package models.displayer;

import io.DisplayFormat;
import jobs.Jobs;
import models.ModelCplexItf;

class ModelCplexDisplayer implements ModelDisplayer {

    private final Jobs jobs;
    private final ModelCplexItf m;

    ModelCplexDisplayer(ModelCplexItf m) {
        this.jobs = m.getJobs();
        this.m = m;
    }

    @Override
    public String getInitial() {
        String out = getInput("Execution time grid available slot :", SelectData.XIT);
        out += getInput("Starting time grid available slot:", SelectData.YIT);
        return out;
    }

    @Override
    public String getSolution() {
        String out = getSolution("xit", SelectData.XIT);
        out += getSolution("yit", SelectData.YIT);
        return out;
    }

    private String getInput(String s, SelectData sd) {
        StringBuilder out = new StringBuilder(s + "\n");
        for (int i = -1; i < jobs.size(); i++) {
            for (int t = 0; t < jobs.getH(); t++) {
                if (i == -1) {
                    out.append(DisplayFormat.format(t));
                } else {
                    if (m.notNull(sd, i, t)) {
                        out.append(DisplayFormat.format('x'));
                    } else {
                        out.append(DisplayFormat.format('.'));
                    }
                }
            }
            out.append("\n");
        }
        return out.toString();
    }

    private String getSolution(String s, SelectData sd) {
        StringBuilder out = new StringBuilder("Domain solution grid" + s + "\n");
        for (int i = -1; i < jobs.size(); i++) {
            for (int t = 0; t < jobs.getH(); t++) {
                if (i == -1) {
                    out.append(DisplayFormat.format(t));
                } else {
                    if (! m.notNull(sd, i, t)) {
                        out.append(DisplayFormat.format('.'));
                    } else {
                        double v = m.getValue(sd, i, t);
                        if (v == 0.0) {
                            out.append(DisplayFormat.format(0));
                        } else if (v == 1.0){
                            out.append(DisplayFormat.format(1));
                        } else {
                            out.append(DisplayFormat.format(v));
                        }
                    }
                }
            }
            out.append("\n");
        }
        for (int t = 0; t < jobs.getH(); t++) {
            out.append(DisplayFormat.format('-'));
        }
        out.append("\n");
        for (int t = 0; t < jobs.getH(); t++) {
            double value = m.getValue(t);
            out.append(DisplayFormat.format((int) value));
        }
        out.append("\n");
        return out.toString();
    }
}