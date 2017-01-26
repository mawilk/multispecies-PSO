package pl.edu.agh.mpso;

import static pl.edu.agh.mpso.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_PARTICLES;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.Particle;
import pl.edu.agh.mpso.dao.SimulationResultDAO;
import pl.edu.agh.mpso.fitness.Rastrigin;
import pl.edu.agh.mpso.output.SimulationOutput;
import pl.edu.agh.mpso.output.SimulationOutputError;
import pl.edu.agh.mpso.output.SimulationOutputOk;
import pl.edu.agh.mpso.output.SimulationResult;
import pl.edu.agh.mpso.species.SpeciesType;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.NeighborhoodEuclides;
import pl.edu.agh.mpso.swarm.SwarmInformation;
import pl.edu.agh.mpso.utils.ConfigReader;

import com.google.gson.Gson;

/**
 * 
 * @author iwanb
 * command line args:
 * - function name - must be the same as class from pl.edu.agh.mpso.fitness
 * - number of dimensions
 * - number of iterations
 * - config filename
 */
public class Scalarm {
	private static String className;
	private static final int EXECUTIONS = 5;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		// get optimization problem
		FitnessFunction fitnessFunction = null;
		Class<? extends FitnessFunction> fitnessFunctionClass = Rastrigin.class;
		className = args.length >= 1 ? args[0] : "Rastrigin";
		final String packageName = "pl.edu.agh.mpso.fitness";
		try {
			fitnessFunctionClass = (Class<FitnessFunction>) Class.forName(packageName + "." + className);
		} catch (ClassNotFoundException e) {
			System.out.println(className + " " + e.getMessage() + " using Rastrigin function");
		} finally {
			fitnessFunction = fitnessFunctionClass.newInstance();
		}

		// get number of dimensions
		if (args.length >= 2) {
			NUMBER_OF_DIMENSIONS = Integer.valueOf(args[1]);
			if (NUMBER_OF_DIMENSIONS <= 0)
				NUMBER_OF_DIMENSIONS = 10;
		}
		// get number of iterations
		if (args.length >= 3) {
			NUMBER_OF_ITERATIONS = Integer.valueOf(args[2]);
			if (NUMBER_OF_ITERATIONS <= 0)
				NUMBER_OF_ITERATIONS = 1000;
		}
		
		List<SwarmInformation> configuration;
		String filename;
		// create array of species share
		if (args.length >= 4) {
			filename = args[3];
		} else {
			filename = "swarm_config.txt";
		}
		
		configuration = ConfigReader.getSwarm(filename);
		

		SimulationOutput output = null;
		SimulationResult result = null;
		
		String fileName = "results/" + args[0] + "_" + args[1] + "_" + filename + ".json";
		Writer writer = new FileWriter(fileName);
		
		for(int i = 0; i < EXECUTIONS; i++){
			try {
				result = run(configuration, fitnessFunction);
				output = new SimulationOutputOk();
				((SimulationOutputOk) output).results = result;
				// SimulationResultDAO.getInstance().writeResult(result);
				// SimulationResultDAO.getInstance().close();
			} catch (Throwable e) {
				output = new SimulationOutputError();
				((SimulationOutputError) output).reason = e.toString() + ": " + e.getMessage();
			} finally {
				//String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
				Gson gson = new Gson();
				gson.toJson(result.partial, writer);
				writer.append('\n');
			}
		}
		writer.close();
	}

	private static SimulationResult run(List<SwarmInformation> swarmInformations, FitnessFunction fitnessFunction) {
		System.out.println("NUMBER_OF_ITERATIONS = " + NUMBER_OF_ITERATIONS);
		System.out.println("NUMBER_OF_PARTICLES = " + NUMBER_OF_PARTICLES);

		int cnt = 0;
//		double radius = 20.0;

		SwarmInformation[] swarmInformationsArray = new SwarmInformation[swarmInformations.size()];
		MultiSwarm multiSwarm = new MultiSwarm(swarmInformations.toArray(swarmInformationsArray), fitnessFunction);

		Neighborhood neighbourhood = new Neighborhood1D(cnt / 5, true);
		// Neighborhood neighbourhood = new NeighborhoodEuclides(radius);

		multiSwarm.setNeighborhood(neighbourhood);

		multiSwarm.setInertia(0.95);

		multiSwarm.setMaxPosition(100);
		multiSwarm.setMinPosition(-100);

		List<Double> partial = new ArrayList<Double>(NUMBER_OF_ITERATIONS / 100);

		for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			multiSwarm.evolve();
			
			if(multiSwarm.getBestFitness() == 0.0) {
				System.out.println(i);
				partial.add(multiSwarm.getBestFitness());
				break;
			}

			// display partial results
			if (NUMBER_OF_ITERATIONS > 100 && (i % (NUMBER_OF_ITERATIONS / 100) == 0)) {
				partial.add(multiSwarm.getBestFitness());
			}

		}
		// print final results
		System.out.println(multiSwarm.getBestFitness());
		System.out.println("Partial size: " + partial.size());

		// create output.json
		SimulationResult output = new SimulationResult();
		output.fitnessFunction = className;
		output.iterations = NUMBER_OF_ITERATIONS;
		output.dimensions = NUMBER_OF_DIMENSIONS;
		output.partial = partial;
		output.bestFitness = multiSwarm.getBestFitness();
		output.totalParticles = NUMBER_OF_PARTICLES;

		// output.species1 = particles[0];
		// output.species2 = particles[1];
		// output.species3 = particles[2];
		// output.species4 = particles[3];
		// output.species5 = particles[4];
		// output.species6 = particles[5];
		// output.species7 = particles[6];
		// output.species8 = particles[7];

		return output;
	}
}
