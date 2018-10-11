import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.ImmutableList;
import generate.JobShop;
import generate.JobShopWithBound;
import generate.Relaxation;
import generate.SolutionGrids;
import io.DisplayConst;
import io.FolderConst;
import io.LoadFile;
import models.cut.Cut;
import models.cut.Cuts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static JCommander jc;

    @Parameter(names = {"--grids", "-g"}, description="Generate grids solutions", order = 0)
    private boolean grids = false;

    @Parameter(names = {"--relaxation", "-r"}, description="Generate relaxation table", order = 1)
    private boolean relaxation = false;

    @Parameter(names = {"--jobshop", "-j"}, description="Execute jobshop with the custom noOverlap", order = 2)
    private boolean jobshop = false;

    @Parameter(names = {"--cuts", "-c"}, description="Restrict execution to the selected set of cuts",
            order = 3, validateWith = ValidateCut.class)
    private String strCuts = "AB";

    @Parameter(names = {"--Cut", "-C"}, description="Restrict execution to the selected cut", order = 4)
    private boolean cutOnly = false;

    @Parameter(names = {"--files", "-f"}, description = "Restrict execution to the selected file or folder",
            order = 5, variableArity = true)
    private List<String> strFiles = new LinkedList<>();

    @Parameter(names = {"--help", "-h"}, description="Display help", order = 6)
    private boolean help = false;

    @Parameter(names = {"--debug", "-d"}, description="See some debug information", hidden = true, order = 7)
    private boolean debug = false;

    public static class ValidateCut implements IParameterValidator {
        public void validate(String name, String value) throws ParameterException {
            boolean matches = Cut.isMatches(value);
            if (! matches) {
                throw new ParameterException("Parameter " + name +
                        " should be a string compose with {A, B, C, D} element, " +
                        "for instance: ACD (found " + value +")");
            }
        }
    }

    public static void main(String[] argv) {
        Main main = new Main();
        jc = JCommander.newBuilder()
                .addObject(main)
                .verbose(0)
                .build();
        jc.setProgramName("ter-jar-with-dependencies.jar");
        jc.parse(argv);
        main.run();
    }

    private void run() {
        System.setProperty("log.name", logName());
        Logger logger = LoggerFactory.getLogger(Main.class);

        String argv = String.format("Command line argv : " +
                        "grids=%b, relaxation=%b, jobshop=%b, cuts=%s, cutOnly=%b files=%s%n",
                grids, relaxation, jobshop, strCuts, cutOnly, strFiles);
        if (logger.isDebugEnabled()) {
            logger.debug(argv);
        }

        try {

            if (help || !(grids || relaxation || jobshop || debug))  {
                jc.usage();
                return;
            }

            final ImmutableList<Cut> cuts = cutOnly
                    ? ImmutableList.of(Cut.fromString(strCuts))
                    : Cuts.fromString(strCuts);

            if (grids || relaxation) {
                LoadFile loadFile = new LoadFile(FolderConst.RELAXATION.toString());
                List<File> files = strFiles.isEmpty() ? loadFile.all() : loadFile.fromStrings(strFiles);

                files.forEach(e -> logger.info("Adding "  + e.toString()));

                if (grids) {
                    logger.info("Starting grids computation");
                    new SolutionGrids(cuts, files);
                }

                if (relaxation) {
                    logger.info("Starting relaxation computation");
                    new Relaxation(cuts, files);
                }
            }


            if (jobshop) {
                LoadFile loadFile = new LoadFile(FolderConst.JOBSHOP.toString());
                List<File> files = strFiles.isEmpty() ? loadFile.all() : loadFile.fromStrings(strFiles);

                files.forEach(e -> logger.info("Adding "  + e.toString()));
                logger.info("Starting jobshop computation");

                for (File file : files) {
                    new JobShopWithBound(file.getAbsolutePath());
                }
            }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private String logName() {
        StringBuilder sb = new StringBuilder("");
        if (grids) sb.append("g");
        if (relaxation) sb.append("r");
        if (jobshop) sb.append("j");
        sb.append(strCuts);
        if (cutOnly) sb.append("-C");
        return sb.toString();
    }
}
