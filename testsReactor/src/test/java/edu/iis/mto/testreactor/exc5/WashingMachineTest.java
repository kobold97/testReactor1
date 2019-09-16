package edu.iis.mto.testreactor.exc5;


import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class WashingMachineTest {

	@Mock
	DirtDetector dirtDetecor;
	
	@Mock
	Engine engine;
	
	@Mock
	WaterPump waterPump;
	
	WashingMachine washingMachine;
	
    public static final double MAX_WEIGTH_KG = 8;
    private static final double HALF_MAX_WEIGTH = MAX_WEIGTH_KG / 2;
		
	@Before
	public void setUp() {
		washingMachine = new WashingMachine(dirtDetecor, engine, waterPump);
	}
	
	@Test 
	public void tooHeavyWoolenLaundryResultsWithFailure() {

		
		LaundryBatch.Builder laundryBuilder = LaundryBatch.builder();
		laundryBuilder.withWeightKg(HALF_MAX_WEIGTH + 1);
		laundryBuilder.withMaterialType(Material.WOOL);
		LaundryBatch laundryBatch = laundryBuilder.build();
		ProgramConfiguration.Builder configurationBuilder = ProgramConfiguration.builder();
		ProgramConfiguration programConfiguration = configurationBuilder.build();
		LaundryStatus status = washingMachine.start(laundryBatch, programConfiguration);
		
		Assert.assertThat(status.getResult(), Matchers.equalTo(Result.FAILURE));
		Assert.assertThat(status.getErrorCode(), Matchers.equalTo(ErrorCode.TOO_HEAVY));

	}

	@Test 
	public void tooHeavyLaundryOtherThanWoolenAndJeansResultsWithFailure() {

		
		LaundryBatch.Builder laundryBuilder = LaundryBatch.builder();
		laundryBuilder.withWeightKg(MAX_WEIGTH_KG + 1);
		laundryBuilder.withMaterialType(Material.COTTON);
		LaundryBatch laundryBatch = laundryBuilder.build();
		ProgramConfiguration.Builder configurationBuilder = ProgramConfiguration.builder();
		ProgramConfiguration programConfiguration = configurationBuilder.build();
		LaundryStatus status = washingMachine.start(laundryBatch, programConfiguration);
		
		Assert.assertThat(status.getResult(), Matchers.equalTo(Result.FAILURE));
		Assert.assertThat(status.getErrorCode(), Matchers.equalTo(ErrorCode.TOO_HEAVY));

	}
	
	@Test 
	public void autoDetectProgramAutoDetectsDirtDegree() {

		
		LaundryBatch.Builder laundryBuilder = LaundryBatch.builder();
		laundryBuilder.withWeightKg(MAX_WEIGTH_KG - 2);
		laundryBuilder.withMaterialType(Material.COTTON);
		LaundryBatch laundryBatch = laundryBuilder.build();
		ProgramConfiguration.Builder configurationBuilder = ProgramConfiguration.builder();
		configurationBuilder.withProgram(Program.AUTODETECT);
		ProgramConfiguration programConfiguration = configurationBuilder.build();
		LaundryStatus status = washingMachine.start(laundryBatch, programConfiguration);
		
		verify(dirtDetecor, times(1)).detectDirtDegree(laundryBatch);

	}
	
	@Test 
	public void checkIfPumpPours() {

		
		LaundryBatch.Builder laundryBuilder = LaundryBatch.builder();
		laundryBuilder.withWeightKg(MAX_WEIGTH_KG - 2);
		laundryBuilder.withMaterialType(Material.COTTON);
		LaundryBatch laundryBatch = laundryBuilder.build();
		ProgramConfiguration.Builder configurationBuilder = ProgramConfiguration.builder();
		configurationBuilder.withProgram(Program.AUTODETECT);
		ProgramConfiguration programConfiguration = configurationBuilder.build();
		LaundryStatus status = washingMachine.start(laundryBatch, programConfiguration);
		
		verify(waterPump, times(1)).pour(laundryBatch.getWeightKg());

	}
	
	@Test 
	public void checkIfEngineRunWashing() {

		
		LaundryBatch.Builder laundryBuilder = LaundryBatch.builder();
		laundryBuilder.withWeightKg(MAX_WEIGTH_KG - 2);
		laundryBuilder.withMaterialType(Material.COTTON);
		LaundryBatch laundryBatch = laundryBuilder.build();
		ProgramConfiguration.Builder configurationBuilder = ProgramConfiguration.builder();
		configurationBuilder.withProgram(Program.LONG);
		ProgramConfiguration programConfiguration = configurationBuilder.build();
		LaundryStatus status = washingMachine.start(laundryBatch, programConfiguration);
		
		verify(engine, times(1)).runWashing(Program.LONG.getTimeInMinutes());

	}
	
	@Test 
	public void checkIfWaterPumpReleases() {

		
		LaundryBatch.Builder laundryBuilder = LaundryBatch.builder();
		laundryBuilder.withWeightKg(MAX_WEIGTH_KG - 2);
		laundryBuilder.withMaterialType(Material.COTTON);
		LaundryBatch laundryBatch = laundryBuilder.build();
		ProgramConfiguration.Builder configurationBuilder = ProgramConfiguration.builder();
		configurationBuilder.withProgram(Program.LONG);
		configurationBuilder.withSpin(true);
		ProgramConfiguration programConfiguration = configurationBuilder.build();
		LaundryStatus status = washingMachine.start(laundryBatch, programConfiguration);
		
		verify(engine, times(1)).spin();

	}
	
}
