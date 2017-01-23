package pl.edu.agh.mpso.species;

import java.awt.Color;

public enum SpeciesType {

	STANDARD(1.0, 1.0, 1.0, "STANDARD", new int[] {}), // 0

	// C1
	C1_a(1.0, 1.0, 1.0, "C1_a", new int[] { 2, 4 }), // 1
	C1_b(1.0, 0.0, 1.0, "C1_b", new int[] {}), // 2
	C1_c(0.0, 1.0, 1.0, "C1_c", new int[] { 2 }), // 3
	C1_d(1.0, 1.0, 0.0, "C1_d", new int[] { 3 }), // 4

	// C2
	C2_a(1.0, 1.0, 1.0, "C2_a", new int[] { 8 }), // 5
	C2_b(1.0, 0.0, 1.0, "C2_b", new int[] { 8 }), // 6
	C2_c(1.0, 0.0, 0.0, "C2_c", new int[] { 5 }), // 7
	C2_d(1.0, 1.0, 0.0, "C2_d", new int[] { 7 }), // 8

	// C3
	C3_a(1.0, 1.0, 1.0, "C3_a", new int[] {}), // 9
	C3_b(1.0, 0.0, 1.0, "C3_b", new int[] { 9 }), // 10
	C3_c(1.0, 1.0, 1.0, "C3_c", new int[] { 9 }), // 11
	C3_d(1.0, 1.0, 0.0, "C3_d", new int[] { 9 }), // 12

	// C4
	C4_a(1.0, 1.0, 1.0, "C4_a", new int[] {}), // 13
	C4_b(1.0, 1.0, 1.0, "C4_b", new int[] { 15, 16 }), // 14
	C4_c(1.0, 1.0, 0.0, "C4_c", new int[] { 13, 14 }), // 15
	C4_d(1.0, 1.0, 0.0, "C4_d", new int[] {}), // 16

	// STANDARD
	ALL(1.0, 1.0, 1.0, "All", new int[] {}), // 17
	GLOBAL_AND_NEIGHBOUR(1.0, 0.0, 1.0, "Global and neighbour", new int[] {}), // 18
	LOCAL_AND_NEIGHBOUR(0.0, 1.0, 1.0, "Local and neighbour", new int[] {}), // 19
	GLOBAL_ONLY(1.0, 0.0, 0.0, "Global only", new int[] {}), // 20
	LOCAL_ONLY(0.0, 1.0, 0.0, "Local only", new int[] {}), // 21
	NEIGHBOUR_ONLY(0.0, 0.0, 1.0, "Neighbour only", new int[] {}), // 22
	RANDOM(); // 23

	private static final int SPECIES_NUMBER = 24;

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
