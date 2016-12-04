package pl.edu.agh.mpso.swarm;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.Swarm;

import static pl.edu.agh.mpso.Simulation.NUMBER_OF_DIMENSIONS;

public class NeighborhoodEuclides extends Neighborhood{
	
	double radius;
	Swarm swarm;
	
	public NeighborhoodEuclides(double radius) {
		super();
		this.radius = radius;
	}

	@Override
	public Collection<Particle> calcNeighbours(Particle p) {
		
		ArrayList<Particle> neigh = new ArrayList<Particle>();
		for(Particle pp: swarm){
			double distance = 0.0;
			for(int i = 0; i < NUMBER_OF_DIMENSIONS; i++){
				distance += Math.pow(p.getPosition()[i] - pp.getPosition()[i], 2.0);
			}
			distance = Math.sqrt(distance);
					
			if (distance <= radius && pp != p){
				neigh.add(pp);
			}
		}
		return neigh;
	}
	
	@Override
	public void init(Swarm swarm) {
		this.swarm = swarm;
		super.init(swarm); // Call to Neighborhood.init() method
	}
	
	
	//trzeba za każdym razem od nowa liczyć sąsiadów
	@Override
	public Collection<Particle> getNeighbours(Particle p) {
		Collection<Particle> neighs = calcNeighbours(p);
		return neighs;
	}
	
}
