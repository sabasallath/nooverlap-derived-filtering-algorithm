package generate;

import com.google.common.collect.ImmutableList;
import io.DataReader;
import io.DisplayConst;
import jobs.Jobs;
import models.Model;
import models.ModelFactory;
import models.Solution;
import models.cut.Cut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

public class SolutionGrids {

    private final static Logger logger = LoggerFactory.getLogger(SolutionGrids.class);
    private final ImmutableList<Cut> cuts;
    private final String overview;
    private final String folder;
    private final boolean displaySolution;

    public SolutionGrids(ImmutableList<Cut> cuts, List<File> files) {
        folder = "output/solutionsGrids/sg_" + LocalDateTime.now() + "/";
        this.cuts = cuts;
        this.displaySolution = cuts.size() == 1 && files.size() == 1;
        this.overview = new File(folder).mkdirs()
                ? generate(files)
                : "Error in creating folder";
        display();
    }

    private String generate(List<File> files) {
        StringBuilder overviewBuilder = new StringBuilder();

        for (File file : files) {
            logger.info("Generating solution grids for " + file.getName());
            overviewBuilder
                    .append(DisplayConst.SHORTSEPARATOR).append("\n")
                    .append(file.getName())
                    .append("\n")
                    .append(generate(file));
        }
        return overviewBuilder.toString();
    }

    private String generate(File file) {

        Jobs jobs = new DataReader(file).read();

        final String target = this.folder + file.getName();

        StringJoiner resultBuilder = new StringJoiner("");

        for (Cut cut : cuts) {
            resultBuilder.add("Cut:").add(String.format("%1$" + 4 + "s", cut.toString()));
            Model modelMips = ModelFactory.mipsGrid(jobs, cut);
            Solution sMips = modelMips.getSolution();
            resultBuilder.add(", mip:").add(Double.toString(sMips.getObjValue()));
            sMips.writeSolution(target);
            displaySolution(sMips);

            Solution sLp = ModelFactory.lpGrid(jobs, cut).getSolution();
            resultBuilder.add(", lp:").add(String.format("%.3f", sLp.getObjValue())).add("\n");
            sLp.writeSolution(target);
            displaySolution(sLp);
        }

        Solution sCp = ModelFactory.cpGrid(jobs).getSolution();
        resultBuilder.add("cp:").add(Double.toString(sCp.getObjValue())).add("\n");
        sCp.writeSolution(target);
        displaySolution(sCp);

        return resultBuilder.toString();
    }

    private void displaySolution(Solution s) {
        if (displaySolution) {
            System.out.println(DisplayConst.SEPARATOR);
            System.out.println("Type of the Model : " + s.getTypeModel());
            System.out.print(DisplayConst.SHORTSEPARATOR + "\n");
            System.out.println(s.toString());
        }
    }

    private void display() {
        System.out.println(DisplayConst.SEPARATOR);
        System.out.print("Overview of files generated at "+ folder + "\nCuts="+ cuts.toString() +"\n" + overview);
    }

}
