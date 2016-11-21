package pl.edu.agh.mpso.species;

import java.awt.Color;

public enum SpeciesType {
	ALL(1.0, 1.0, 1.0, "Normal"), 
	GLOBAL_AND_LOCAL(1.0, 1.0, 0.0, "Global and local"), 
	GLOBAL_AND_NEIGHBOUR(1.0, 0.0, 1.0, "Global and neighbour"), 
	LOCAL_AND_NEIGHBOUR(0.0, 1.0, 1.0, "Local and neighbour"), 
	GLOBAL_ONLY(1.0, 0.0, 0.0, "Global only"), 
	LOCAL_ONLY(0.0, 1.0, 0.0, "Local only"), 
	NEIGHBOUR_ONLY(0.0, 0.0, 1.0, "Neighbour only"), 
	RANDOM();

	private static final int SPECIES_NUMBER = 8;
	
	private final double global;
	private final double local;
	private final double neighbour;

	private final Color color;
	private final String label;

	private SpeciesType(double global, double local, double neighbour, String label) {
		int rgb = 255 / (SPECIES_NUMBER - this.ordinal());
		this.color = new Color(rgb, rgb, rgb);
		this.global = global;
		this.local = local;
		this.neighbour = neighbour;
		this.label = label;
	}

	private SpeciesType() {
		int rgb = 255 / (SPECIES_NUMBER - this.ordinal());
		this.label = "Random weights";
		this.color = new Color(rgb, rgb, rgb);
		this.global = Math.random() * 3.0;
		this.local = Math.random() * (3.0 - global);
		this.neighbour = 3.0 - global - local;
	}

	@Override
	public String toString() {
		return label;
	}

	public Color getColor() {
		return color;
	}

	public double[] getWeights() {
		return new double[] { global, local, neighbour };
	}
}
