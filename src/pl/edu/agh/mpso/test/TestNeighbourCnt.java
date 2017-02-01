package pl.edu.agh.mpso.test;

import static pl.edu.agh.mpso.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_PARTICLES;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.mpso.fitness.Griewank;
import pl.edu.agh.mpso.output.SimulationOutput;
import pl.edu.agh.mpso.output.SimulationOutputError;
import pl.edu.agh.mpso.output.SimulationOutputOk;
import pl.edu.agh.mpso.output.SimulationResult;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.SwarmInformation;
import pl.edu.agh.mpso.utils.ConfigReader;

public class TestNeighbourCnt {
	private static String className;
	private static final int EXECUTIONS = 30;
	private static List<Thread> threads;

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, IOException, InterruptedException {
		// get optimization problem
		FitnessFunction fitnessFunction = new Griewank();

		NUMBER_OF_DIMENSIONS = 50;
		NUMBER_OF_ITERATIONS = 10000;

		threads = new ArrayList<Thread>();

		String configurationFileName;

		configurationFileName = "swarm_config";

		for (int i = 2; i <= 10; i+=2) {
			String fileName = "results/Griewank_50_nbh" + String.valueOf(i) + ".json";
			runParallel(i, configurationFileName, fitnessFunction, fileName);
		}

		for (Thread thread : threads) {
			thread.join();
		}
	}

	private static void simulate(int i, String configName, FitnessFunction fitnessFunction, String fileName)
			throws IOException {
		List<SwarmInformation> configuration;
		configuration = ConfigReader.getSwarm(configName + ".txt");

		SimulationOutput output = null;
		SimulationResult result = null;

		for (int j = 0; j < EXECUTIONS; j++) {
			Writer writer = new FileWriter(fileName, true);
			try {
				result = run(configuration, fitnessFunction, i);
				output = new SimulationOutputOk();
				((SimulationOutputOk) output).results = result;
				// SimulationResultDAO.getInstance().writeResult(result);
				// SimulationResultDAO.getInstance().close();
			} catch (Throwable e) {
				output = new SimulationOutputError();
				((SimulationOutputError) output).reason = e.toString() + ": " + e.getMessage();
			} finally {
				// String timeStamp = new
				// SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
				writer.write(String.valueOf(result.bestFitness));
				writer.write(", ");
				writer.write(String.valueOf(result.iterations));
				writer.append('\n');

				writer.close();
			}
		}

	}

	private static void runParallel(final int i, final String configName, final FitnessFunction fitnessFunction,
			final String fileName) {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				try {
					simulate(i, configName, fitnessFunction, fileName);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		threads.add(thread);
		thread.start();
	}

	private static SimulationResult run(List<SwarmInformation> swarmInformations, FitnessFunction fitnessFunction, int nbhCnt) {
		// System.out.println("NUMBER_OF_ITERATIONS = " + NUMBER_OF_ITERATIONS);

		// double radius = 20.0;

		SwarmInformation[] swarmInformationsArray = new SwarmInformation[swarmInformations.size()];
		MultiSwarm multiSwarm = new MultiSwarm(swarmInformations.toArray(swarmInformationsArray), fitnessFunction);

		Neighborhood neighbourhood = new Neighborhood1D(nbhCnt, true);
		// Neighborhood neighbourhood = new NeighborhoodEuclides(radius);

		multiSwarm.setNeighborhood(neighbourhood);

		multiSwarm.setInertia(0.95);

		multiSwarm.setMaxPosition(100);
		multiSwarm.setMinPosition(-100);

		List<Double> partial = new ArrayList<Double>(NUMBER_OF_ITERATIONS / 100);
		int finished = 0;

		for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			multiSwarm.evolve();

			if (multiSwarm.getBestFitness() == 0.0) {
				System.out.println(i);
				finished = i;
				partial.add(multiSwarm.getBestFitness());
				break;
			}

			// display partial results
			if (NUMBER_OF_ITERATIONS > 100 && (i % (NUMBER_OF_ITERATIONS / 100) == 0)) {
				partial.add(multiSwarm.getBestFitness());
			}

		}

		if (finished == 0)
			finished = NUMBER_OF_ITERATIONS;

		// print final results
		System.out.println(multiSwarm.getBestFitness());
		// System.out.println("Partial size: " + partial.size());

		// create output.json
		SimulationResult output = new SimulationResult();
		output.fitnessFunction = className;
		output.iterations = finished;
		output.dimensions = NUMBER_OF_DIMENSIONS;
		output.partial = partial;
		output.bestFitness = multiSwarm.getBestFitness();
		output.totalParticles = NUMBER_OF_PARTICLES;

		return output;
	}
}
