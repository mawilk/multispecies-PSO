package pl.edu.agh.mpso.species;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
import pl.edu.agh.mpso.swarm.MultiSwarm;
import pl.edu.agh.mpso.swarm.SwarmInformation;

public class SpeciesParticleUpdate extends ParticleUpdate {
	private SpeciesType type = SpeciesType.ALL;

	public SpeciesParticleUpdate(Particle particle) {
		super(particle);

		if (particle instanceof SpeciesParticle) {
			type = ((SpeciesParticle) particle).getType();
		}

	}

	@Override
	public void update(Swarm swarm, Particle particle) {

		MultiSwarm multiswarm = (MultiSwarm) swarm;

		SwarmInformation[] infos = multiswarm.getSwarmInfos();

		double[] position = particle.getPosition();
		double[] velocity = particle.getVelocity();

		double[] globalBest = swarm.getBestPosition();
		double[] localBest = particle.getBestPosition();
		double[] neighbourhoodBest = swarm.getNeighborhoodBestPosition(particle);

		double localIncrement = Math.random();
		double globalIncrement = Math.random();
		double neighbourhoodInrement = Math.random();

		double[] weights = type.getWeights();

		for (int i = 0; i < position.length; i++) {
			// Update position
			position[i] = position[i] + velocity[i];

			double inspireDirection = 0;
			int[] speciesToInspire = type.getSpeciesToInspire();

			if (speciesToInspire.length == 0) {
				inspireDirection = globalIncrement * weights[0] * (globalBest[i] - position[i]);
			} else {
				for (int toInspire : speciesToInspire) {
					for (SwarmInformation swarmInformation : infos) {
						if (swarmInformation.getType().ordinal() == toInspire) {
							inspireDirection += globalIncrement * weights[0] * (swarmInformation.getBestPosition()[i] - position[i]);
						}
					}
				}
				
				inspireDirection /= speciesToInspire.length;
			}

			velocity[i] = swarm.getInertia() * velocity[i] + inspireDirection
					+ localIncrement * weights[1] * (localBest[i] - position[i])
					+ neighbourhoodInrement * weights[2] * (neighbourhoodBest[i] - position[i]);
		}
	}

}
