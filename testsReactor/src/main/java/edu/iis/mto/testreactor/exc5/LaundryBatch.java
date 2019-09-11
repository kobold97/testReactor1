package edu.iis.mto.testreactor.exc5;

import static java.util.Objects.requireNonNull;

public class LaundryBatch {

    private final double weightKg;
    private final Material materialType;

    private LaundryBatch(Builder builder) {
        this.weightKg = requireNonNull(builder.weightKg, "weightKg == null");
        this.materialType = requireNonNull(builder.materialType, "materialType == null");
    }

    public double getWeightKg() {
        return weightKg;
    }

    public Material getMaterialType() {
        return materialType;
    }

    @Override
    public String toString() {
        return "LaundryBatch [weightKg=" + weightKg + ", materialType=" + materialType + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private double weightKg;
        private Material materialType;

        private Builder() {}

        public Builder withWeightKg(double weightKg) {
            this.weightKg = weightKg;
            return this;
        }

        public Builder withMaterialType(Material materialType) {
            this.materialType = materialType;
            return this;
        }

        public LaundryBatch build() {
            return new LaundryBatch(this);
        }
    }

}
