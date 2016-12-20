package pl.edu.agh.mpso.species;

import java.awt.Color;

public enum SpeciesType {
	ALL(1.0, 1.0, 0.0, "Standard", new int[] {}), //0
	GLOBAL_AND_LOCAL(1.0, 1.0, 0.0, "Global and local", new int[] {}), //1
	GLOBAL_AND_NEIGHBOUR(1.0, 0.0, 1.0, "Global and neighbour", new int[] {}), //2 
	LOCAL_AND_NEIGHBOUR(0.0, 1.0, 1.0, "Local and neighbour", new int[] {}), //3
	GLOBAL_ONLY(1.0, 0.0, 0.0, "Global only", new int[] {}), //4
	LOCAL_ONLY(0.0, 1.0, 0.0, "Local only", new int[] {}), //5
	NEIGHBOUR_ONLY(0.0, 0.0, 1.0, "Neighbour only", new int[] {}), //6
	RANDOM(); //7
	
	private static final int SPECIES_NUMBER = 8;
	
	private final double global;
	private final double local;
	private final double neighbour;
	
	private int[] speciesToInspire;

	private final Color color;
	private final String label;

	private SpeciesType(double global, double local, double neighbour, String label, int[] speciesToInspire) {
		int rgb = 255 / (SPECIES_NUMBER - this.ordinal());
		this.color = new Color(rgb, rgb, rgb);
		this.global = global;
		this.local = local;
		this.neighbour = neighbour;
		this.label = label;
		this.speciesToInspire = speciesToInspire;
	}

	private SpeciesType() {
		int rgb = 255 / (SPECIES_NUMBER - this.ordinal());
		this.label = "Random weights";
		this.color = new Color(rgb, rgb, rgb);
		this.global = Math.random() * 3.0;
		this.local = Math.random() * (3.0 - global);
		this.neighbour = 3.0 - global - local;
		this.speciesToInspire = new int[] {};
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
	
	public int[] getSpeciesToInspire() {
		return speciesToInspire;
	}
}
