package pl.edu.agh.mpso;

import static pl.edu.agh.mpso.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_SKIPPED_ITERATIONS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.mpso.chart.Chart;
import pl.edu.agh.mpso.chart.ChartCombiner;
import pl.edu.agh.mpso.chart.Point;
import pl.edu.agh.mpso.chart.ScatterChart;
import pl.edu.agh.mpso.chart.SpeciesPieChart;
import pl.edu.agh.mpso.fitness.Ackley;
import pl.edu.agh.mpso.fitness.Griewank;
import pl.edu.agh.mpso.fitness.Rastrigin;
import pl.edu.agh.mpso.fitness.Rosenbrock;
import pl.edu.agh.mpso.fitness.Schwefel;
import pl.edu.agh.mpso.species.SpeciesType;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.SwarmInformation;

@SuppressWarnings("rawtypes")
public class Comparison {
	private static final int EXECUTIONS = 1;
	private static Map<String, Chart> pieCharts = new TreeMap<String, Chart>();
	private static Map<String, Map<Integer, List<Double>>> results = new TreeMap<String, Map<Integer, List<Double>>>();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		for (int i = 0; i < EXECUTIONS; i++) {
			System.out.println("Execution " + (i + 1) + " of " + EXECUTIONS);
			run("standard", new int[] { 130 });
			// run("c1_1", new int[] { 0, 60, 20, 30, 20 });
			// run("c1_2", new int[] { 0, 30, 60, 20, 20 });
			run("c1_3", new int[] { 0, 20, 30, 60, 20 });
			// run("c1_4", new int[] { 0, 20, 60, 30, 20 });
			// run("c1_5", new int[] { 0, 20, 20, 30, 60 });
			// run("c1_6", new int[] { 0, 30, 20, 20, 60 });
			run("c1_7", new int[] { 0, 60, 20, 20, 30 });
			run("c1_8", new int[] { 0, 20, 20, 60, 30 });
			// run("c1_9", new int[] { 0, 30, 20, 60, 20 });
			// run("c1_10", new int[] { 0, 20, 60, 20, 30 });
			run("c1_11", new int[] { 0, 60, 30, 20, 20 });
			// run("c1_12", new int[] { 0, 20, 30, 20, 60 });
		}

		Chart chart = new ScatterChart()
				.setTitle("PSO Griewank optimizing, " + NUMBER_OF_DIMENSIONS + " dimensions, " + NUMBER_OF_ITERATIONS
						+ " iterations")
				.setXAxisTitle("Iterations").setYAxisTitle("Fitness").addSubTitle("" + EXECUTIONS + " executions");

		for (String swarmName : results.keySet()) {
			List<Point> points = new ArrayList<Point>();
			for (int key : results.get(swarmName).keySet()) {
				List<Double> values = results.get(swarmName).get(key);
				double sum = 0.0;

				for (double value : values) {
					sum += value;
				}

				double average = sum / values.size();
				double standardDeviation = standardDeviation(values, average);

				points.add(new Point(key, average, standardDeviation));
			}
			chart.addSeries(swarmName, points);
		}

		chart.addStandardDeviation().saveWithDateStamp("raw/chart");

		try {
			ChartCombiner.combine(chart, pieCharts);
		} catch (Exception e) {

		}
	}

	private static double standardDeviation(List<Double> values, double average) {
		double sum = 0.0;

		for (double value : values) {
			sum += Math.pow(average - value, 2.0);
		}

		double variance = sum / values.size();

		return Math.sqrt(variance);
	}

	private static void run(String name, int[] particles) {
		// create pie chart
		if (!pieCharts.containsKey(name)) {
			Chart<Integer> pieChart = new SpeciesPieChart().addSpeciesData(name, particles);
			pieCharts.put(name, pieChart);
		}

		// create particles
		int cnt = 0;
		List<SwarmInformation> swarmInformations = new ArrayList<SwarmInformation>();

		for (int i = 0; i < particles.length; i++) {
			if (particles[i] != 0) {
				cnt += particles[i];

				SpeciesType type = SpeciesType.values()[i];
				SwarmInformation swarmInformation = new SwarmInformation(particles[i], type);

				swarmInformations.add(swarmInformation);
			}
		}

		SwarmInformation[] swarmInformationsArray = new SwarmInformation[swarmInformations.size()];
		NUMBER_OF_DIMENSIONS = 500;
		MultiSwarm multiSwarm = new MultiSwarm(swarmInformations.toArray(swarmInformationsArray), new Griewank());

		// NUMBER_OF_DIMENSIONS = 25;
		// MultiSwarm multiSwarm = new
		// MultiSwarm(swarmInformations.toArray(swarmInformationsArray), new
		// Rosenbrock());

		// NUMBER_OF_DIMENSIONS = 50;
		// MultiSwarm multiSwarm = new
		// MultiSwarm(swarmInformations.toArray(swarmInformationsArray), new
		// Ackley());

		Neighborhood neighbourhood = new Neighborhood1D(cnt / 5, true);
		multiSwarm.setNeighborhood(neighbourhood);

		multiSwarm.setInertia(0.95);

		multiSwarm.setMaxPosition(100);
		multiSwarm.setMinPosition(-100);

		for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
			if (i % 10 == 0 && i > 10) {
				if (!results.containsKey(name))
					results.put(name, new HashMap<Integer, List<Double>>());
				if (!results.get(name).containsKey(i))
					results.get(name).put(i, new ArrayList<Double>());
				results.get(name).get(i).add(multiSwarm.getBestFitness());
			}
		}
	}

}
