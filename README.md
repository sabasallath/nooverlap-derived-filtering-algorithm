# Derived filtering algorithm relative to noOverlap constraint

## Context

One month and a half internship at GSCOP laboratory (Grenoble).

## Goal

### Short version

The purpose of the project is to derive a filtering algorithm from a linear relaxation of a MIP (Mixed integer programming) of the global constraint *noOverlap* in scheduling.

### Long version

The current approach to effectively propagate the constraints of *noOverlap* in a scheduling problem is to restrict itself to the use of polynomial filtering algorithms working mainly to reduce time windows of execution of each task.

The reason for this is the NP-hard character of the problem and the fact that the propagation phase occurs at each node of the search tree in the solution space. We therefore seek to minimize its impact on performance.

However, the simplification of the initial problem and the limitation of the filtering algorithms to a polynomial complexity reduce by the same amount the quantity of possible reasonings during the propagation phase.

The approach proposed in this report is to go beyond this complexity limit to maximize the amount of possible reasoning during the propagation phase. For this we will use a hybrid resolution method mixing CP (constraint programming) and LP (linear programming) for the propagation of the global constraint *noOverlap*.

We propose more specifically to use a linear relaxation of a MIP model of the noOverlap and to derive a filtering algorithm.

The focus is not initially on the pure performance of the implementation that can always be refined later, but on the quality of the reasoning obtained on the domains. That is, the amount of additional filtering obtained during the propagation phase using the relaxed MIP model (the LP) compared to those obtained with the polynomial algorithms currently in use.

## Report

You can find the report at :

https://github.com/sabasallath/nooverlap-derived-filtering-algorithm/ter_run/raw/master/rapport/ter-rapport.pdf

## Implementation

### Requirements

- bash
- maven
- cplex 12.7.1.0 or above

Following instructions will assume that you have install cplex 12.7.1.0 in the default installation folder.

Which is :

```
/opt/ibm/ILOG/CPLEX_Studio1271/cpoptimizer/bin/x86-64_linux/
/opt/ibm/ILOG/CPLEX_Studio1271/cplex/bin/x86-64_linux/
```

For other version of cplex or for windows you will need to modify the pom.xml and some scripts manually.

### Installation

Go to ter_run/ folder.

#### Export the path

```
sh/export_path.sh
```

Or add :

```
export LD_LIBRARY_PATH=/opt/ibm/ILOG/CPLEX_Studio1271/cpoptimizer/bin/x86-64_linux/:/opt/ibm/ILOG/CPLEX_Studio1271/cplex/bin/x86-64_linux/:$LD_LIBRARY_PATH
```

To your .bashrc or .zshrc.

#### Set max and min memory allocation pool

For improved performance it's recommanded to specifies the maximum and the minimum memory allocation pool of the JVM.

```
sh/export_xms_xmx.sh
```

#### Install cplex dependancies

```
sh/install_cplex_dependancies.sh
```

#### Compile the project

```
sh/compile.sh
```

This will package the project with all it's depencies in a single jar file.

### Execute the project

```
java -jar target/im2ag.ter-1.0-SNAPSHOT.jar [options]
```

#### [Options]

By default the program will execute with a default *AB* cuts set 
and all compatible file he can find in the *input/* folder and subfolder.

| long symbol | short symbol | purpose |
|:---|:---|:---|
| --grids | -g | Generate grids solutions |
| --relaxation | -r | Generate relaxation table |
| --jobshop | -j | Execute jobshop with the custom noOverlap |
| --cuts | -c | Restrict execution to the selected set of cuts |
| --Cut | -C | Restrict execution to the selected cut |
| --files | -f |  Restrict execution to the selected file or folder |
| --help | -h | Display help |
| --debug | -d | See some debug information |

#### Command exemples

```
java -jar target/im2ag.ter-1.0-SNAPSHOT.jar -g -c AB -f report
java -jar target/im2ag.ter-1.0-SNAPSHOT.jar -r -c ABCD -C -f report_2.txt
java -jar target/im2ag.ter-1.0-SNAPSHOT.jar -j
java -jar target/im2ag.ter-1.0-SNAPSHOT.jar -j -g -r
```

### Output

#### Program output

Program output depend of the options.

| Option | Output folder |
|:---|:---|
| --g | output/solutionGrids |
| --r | output/relaxation |
| --j | output/jobshop |

#### Cplex and Cp output

Cplex and Cp output and warning are not displayed by default.

To display them you need to set the log level to *trace*.

```
sh/log_level_to_trace.sh
```

And recompile the project. This will redirect Cplex and Cp output to a log file in the log/ folder and will also display them in the console.

To set back Cplex and Cp output and warning to normal :

```
sh/log_level_to_info.sh
```
