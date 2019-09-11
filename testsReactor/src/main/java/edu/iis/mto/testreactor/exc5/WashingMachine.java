package edu.iis.mto.testreactor.exc5;

import static java.util.Objects.requireNonNull;

public class WashingMachine {

    public static final Percentage AVERAGE_DEGREE = new Percentage(50.0d);
    public static final double MAX_WEIGTH_KG = 8;
    private static final double HALF_MAX_WEIGTH = MAX_WEIGTH_KG / 2;
    private final DirtDetector dirtDetector;
    private final Engine engine;
    private final WaterPump waterPump;

    public WashingMachine(DirtDetector dirtDetector, Engine engine, WaterPump waterPump) {
        this.dirtDetector = requireNonNull(dirtDetector, "dirtDetector == null");
        this.engine = requireNonNull(engine, "engine == null");
        this.waterPump = requireNonNull(waterPump, "waterPump == null");
    }

    public LaundryStatus start(LaundryBatch laundryBatch, ProgramConfiguration programConfiguration) {
        if (overweight(laundryBatch)) {
            return tooHeavy();
        }
        Program programToRun = specifyProgram(laundryBatch, programConfiguration);
        runProgram(laundryBatch, programToRun);
        spin(programConfiguration);
        return programFinished(programToRun);
    }

    private boolean overweight(LaundryBatch laundryBatch) {
        if (laundryBatch.getMaterialType() == Material.WOOL || laundryBatch.getMaterialType() == Material.JEANS) {
            return laundryBatch.getWeightKg() >= HALF_MAX_WEIGTH;
        }
        return laundryBatch.getWeightKg() > MAX_WEIGTH_KG;
    }

    private LaundryStatus tooHeavy() {
        return LaundryStatus.builder()
                            .withResult(Result.FAILURE)
                            .withErrorCode(ErrorCode.TOO_HEAVY)
                            .build();
    }

    private Program specifyProgram(LaundryBatch laundryBatch, ProgramConfiguration programConfiguration) {
        Program program = programConfiguration.getProgram();
        if (program == Program.AUTODETECT) {
            Percentage dirtDegreePercentage = dirtDetector.detectDirtDegree(laundryBatch);
            return calculateProgramBasedOnDirtDegree(dirtDegreePercentage);
        }
        return program;
    }

    private Program calculateProgramBasedOnDirtDegree(Percentage dirtDegreePercentage) {
        if (dirtDegreePercentage.isGreaterThan(AVERAGE_DEGREE)) {
            return Program.LONG;
        }
        return Program.MEDIUM;
    }

    private void runProgram(LaundryBatch laundryBatch, Program programToRun) {
        waterPump.pour(laundryBatch.getWeightKg());
        engine.runWashing(programToRun.getTimeInMinutes());
        waterPump.release();
    }

    private void spin(ProgramConfiguration programConfiguration) {
        if (programConfiguration.isSpin()) {
            engine.spin();
        }
    }

    private LaundryStatus programFinished(Program programToRun) {
        return LaundryStatus.builder()
                            .withResult(Result.SUCCESS)
                            .withRunnedProgram(programToRun)
                            .build();
    }

}
