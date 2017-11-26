package models.displayer;

import models.ModelCp;

class ModelCpDisplayer implements ModelDisplayer {
    private ModelCp model;

    ModelCpDisplayer(ModelCp model) {
        this.model = model;
    }

    @Override
    public String getInitial() {
        return model.getSolution().getInitialGrid();
    }

    @Override
    public String getSolution() {
        return model.getSolution().getSolutionGrid();
    }
}
