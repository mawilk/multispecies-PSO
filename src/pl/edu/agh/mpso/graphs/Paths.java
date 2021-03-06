package pl.edu.agh.mpso.graphs;

import static pl.edu.agh.mpso.Simulation.NUMBER_OF_DIMENSIONS;
import static pl.edu.agh.mpso.Simulation.NUMBER_OF_ITERATIONS;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import pl.edu.agh.mpso.fitness.Parabola;
import pl.edu.agh.mpso.species.SpeciesParticle;
import pl.edu.agh.mpso.species.SpeciesType;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.SwarmInformation;
import pl.edu.agh.mpso.velocity.ConstantVelocityFunction;

public class Paths {
	private static final ConstantVelocityFunction velocityFunction = new ConstantVelocityFunction(0.001);
	private final static FitnessFunction fitnessFunction = new Parabola();
	private final static int [] particleArray = new int[]{6, 0, 0, 0, 0, 0, 0, 1};
	private final static int drawnParticle = 6;
	
	
	private final static int IMAGE_SIZE = 2000;
	private final static double SEARCH_SPACE_SIZE = 5;
	private static int [] colors = null;
	
	
	public static void main(String[] args) throws IOException {
		NUMBER_OF_DIMENSIONS = 2;
		NUMBER_OF_ITERATIONS = 4000;
		
		BufferedImage pathsImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
		BufferedImage optimasImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
		
		drawFunction(pathsImage, optimasImage);
		drawPaths(pathsImage, optimasImage);
		
		ImageIO.write(pathsImage, "PNG", new File("results/thesis/path/p.png"));
		ImageIO.write(optimasImage, "PNG", new File("results/thesis/path/o.png"));
	}
	
	private static void drawFunction(BufferedImage pathsImage, BufferedImage optimasImage){
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		
		double [] position = new double[2];
		
		//find min and max value
		for(int i = 0; i < IMAGE_SIZE; i++){
			for(int j = 0; j < IMAGE_SIZE; j++){
				position[0] = getCoord(i);
				position[1] = getCoord(j);
				double value = fitnessFunction.evaluate(position);
				
				if(value > max){
					max = value;
				} else if(value < min){
					min = value;
				}
			}
		}
		
		//fill color array
		colors = createColors();

		for(int i = 0; i < IMAGE_SIZE; i++){
			for(int j = 0; j < IMAGE_SIZE; j++){
				position[0] = getCoord(i);
				position[1] = getCoord(j);
				double value = fitnessFunction.evaluate(position);
				int color = getColor(min, max, value);
				
				pathsImage.setRGB(i, j, color);
				optimasImage.setRGB(i, j, color);
			}
		}
		
		System.out.println("Function drawn");
	}
	
	
	private static void drawPaths(BufferedImage pathsImage, BufferedImage optimasImage){
		Graphics2D pathsGraphics = pathsImage.createGraphics();
		Graphics2D optimasGraphics = optimasImage.createGraphics();
		
		optimasGraphics.setPaint(Color.BLACK);
		pathsGraphics.setPaint(Color.BLACK);
		
		final int shapeSize = 20;
		int x, y;
		
		MultiSwarm swarm = createSwarm();
		
		for(int i = 0; i < NUMBER_OF_ITERATIONS; i++){
			swarm.evolve();
			
			double[] bestPosition = swarm.getBestPosition();
			x = getPixel(bestPosition[0]);
			y = getPixel(bestPosition[1]);
			
			optimasGraphics.fillRect(x - shapeSize / 2, y - shapeSize / 2, shapeSize, shapeSize);
			
			for(int j = 0; j < swarm.getParticles().length; j++){
				if(drawnParticle >= 0 && drawnParticle != j){
					continue;
				}
				
				SpeciesParticle particle = (SpeciesParticle) swarm.getParticles()[j];
				double[] position = particle.getPosition();
				x = getPixel(position[0]);
				y = getPixel(position[1]);
				
				pathsGraphics.fillOval(x - shapeSize / 2, y - shapeSize / 2, shapeSize, shapeSize);
			}
		}
		
		System.out.println("Particles drawn");
		System.out.println(swarm.getBestFitness());
	}
	
	private static MultiSwarm createSwarm(){
		List<SwarmInformation> swarmInformations = new ArrayList<SwarmInformation>();
		
		for(int i = 0; i < particleArray.length; i++){
			if(particleArray[i] != 0){
				SpeciesType type = SpeciesType.values()[i];
				SwarmInformation swarmInformation = new SwarmInformation(particleArray[i], type);
				
				swarmInformations.add(swarmInformation);
			}
		}
		
		SwarmInformation [] swarmInformationsArray = new SwarmInformation [swarmInformations.size()]; 
		MultiSwarm multiSwarm = new MultiSwarm(swarmInformations.toArray(swarmInformationsArray), fitnessFunction);
		
		Neighborhood neighbourhood = new Neighborhood1D(1, true);
		multiSwarm.setNeighborhood(neighbourhood);
		
		multiSwarm.setInertia(0.95);
		multiSwarm.setVelocityFunction(velocityFunction);
		
		multiSwarm.setMaxPosition(SEARCH_SPACE_SIZE);
		multiSwarm.setMinPosition(-SEARCH_SPACE_SIZE);
		
		multiSwarm.init();
		
		return multiSwarm;
	}
	
	private static int [] createColors(){
		int r = 0, g = 0, b = 255;
		int index = 0;
		int [] colorArray = new int[4 * 256];
		
		//blue to cyan
		for(;g <= 255; g++){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		g--;
		//cyan to green
		for(;b >= 0; b--){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		b++;
		//green to yellow
		for(;r <= 255; r++){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		r--;
		//yellow to red
		for(;g >= 0; g--){
			colorArray[index] = new Color(r, g, b).getRGB();
			index++;
		}
		
		return colorArray;
	}
	
	private static double getCoord(int c){
		return SEARCH_SPACE_SIZE * (double)(c - IMAGE_SIZE / 2) / (double)(IMAGE_SIZE / 2); 
	}
	
	private static int getPixel(double c){
		return (int) ((c / SEARCH_SPACE_SIZE) * IMAGE_SIZE / 2 + IMAGE_SIZE / 2); 
	}

	private static int getColor(double min, double max, double value){
		int index = (int) (colors.length * (value - min) / (max - min));
		if(index >= colors.length) {
			return colors[colors.length - 1]; 
		}
		return colors[index];
	}
}
