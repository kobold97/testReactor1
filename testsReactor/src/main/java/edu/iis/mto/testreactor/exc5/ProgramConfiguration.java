package edu.iis.mto.testreactor.exc5;

import java.util.Objects;

public class ProgramConfiguration {

    private final Program program;
    private final boolean spin;

    private ProgramConfiguration(Builder builder) {
        this.program = Objects.requireNonNull(builder.program, "program == null");
        this.spin = builder.spin;
    }

    public Program getProgram() {
        return program;
    }

    public boolean isSpin() {
        return spin;
    }

    @Override
    public String toString() {
        return "ProgramConfiguration [program=" + program + ", spin=" + spin + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Program program;
        private boolean spin = true;

        private Builder() {}

        public Builder withProgram(Program program) {
            this.program = program;
            return this;
        }

        public Builder withSpin(boolean spin) {
            this.spin = spin;
            return this;
        }

        public ProgramConfiguration build() {
            return new ProgramConfiguration(this);
        }
    }
}
