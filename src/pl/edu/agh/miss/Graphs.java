package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_PARTICLES;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.dao.SimulationResultDAO;
import pl.edu.agh.miss.output.SimulationResult;
import pl.edu.agh.miss.particle.species.SpeciesType;

public class Graphs {
	private static final String fitnessFunction = "Rastrigin";
	private final static int dimensions = 100;
	private final static int iterations = 2000000;
	private final static int totalParticles = 25;
	private final static int NUMBER_OF_SPECIES = SpeciesType.values().length;
	
	private static Map<Integer, List<List<Double>>> filteredResults = new HashMap<Integer, List<List<Double>>>();
	
	
	public static void main(String[] args) throws IOException {
		getPartialsForSpecies(1);
	}
	
	
	private static void getPartialsForSpecies(int speciesId) throws IOException{
		System.out.println("Getting results");
		SimulationResultDAO dao = SimulationResultDAO.getInstance();
		List<SimulationResult> results = dao.getResults(fitnessFunction, dimensions, iterations, totalParticles);
		dao.close();
		System.out.println("Results loaded");
		
		
		System.out.println("Filtering results");
		for(int i = 0; i < 25; i+=5){
			List<List<Double>> partialResults = new ArrayList<List<Double>>();
			filteredResults.put(i, partialResults);
		}
		
		for(SimulationResult result : results){
			for(int arg = 0; arg < 25; arg+=5){
				if(meetsCriteria(result, speciesId, getCount(arg))){
					filteredResults.get(arg).add(result.partial);
					break;
				}
			}
			
		}
		
		
		System.out.println("Preparing chart data");
		
		Chart<List<Point>> chart = new ScatterChart().setTitle("PSO " + fitnessFunction + " optimizing, " + "Influence of species no. " + speciesId).
				setXAxisTitle("Iterations").setYAxisTitle("Fitness").addSubTitle("" + totalParticles + " particles, " + NUMBER_OF_DIMENSIONS + " dimensions, " + NUMBER_OF_ITERATIONS + " iterations");
		
		for(int arg = 0; arg < 25; arg+=5){
			int cnt = getCount(arg);
			String label = "" + cnt + " particles (" + Math.round((float) cnt * 100.0f / (float) totalParticles) + "%)";
			List<Point> points = new ArrayList<Point>();

			List<List<Double>> valuesList = filteredResults.get(arg);
			
			for(int i = 0; i < 100; i++){
				//count average
				double sum = 0.0;
				
				for(List<Double> values : valuesList){
					sum += values.get(i);
				}
				
				double x = NUMBER_OF_ITERATIONS * i / 100;
				double y = sum / valuesList.size();
				points.add(new Point(x, y));				
			}
			
			chart.addSeries(label, points);
		}
		
		chart.saveWithDateStamp("partial/chart");
	}
	
	private static int getCount(int arg){
		int argSum = arg + 5 * (NUMBER_OF_SPECIES - 1);
		float speciesShare = (float) arg / (float) argSum;
		return (int) (speciesShare * NUMBER_OF_PARTICLES);
	}
	
	private static boolean meetsCriteria(SimulationResult result, int speciesId, int speciesCnt){
		try {
			Field speciesField = SimulationResult.class.getDeclaredField("species" + speciesId);
			int speciesFieldValue = (Integer) speciesField.get(result);
			return result.totalParticles == NUMBER_OF_PARTICLES && speciesFieldValue == speciesCnt;
		} catch (Exception e) {
			return false;
		} 
	}

}