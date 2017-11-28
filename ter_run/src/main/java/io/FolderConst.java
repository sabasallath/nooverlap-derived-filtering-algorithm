package io;

public enum FolderConst {

    /**
     * Relaxation and grids filde default root folder.
     */
    RELAXATION("input/input_relaxation/"),

    /**
     * Jobshop default root folder.
     */
    JOBSHOP("input/input_orlib/");

    private final String s;

    FolderConst(String s) {
        this.s = s;
    }

    @Override
    public String toString(){
        return s;
    }
}
