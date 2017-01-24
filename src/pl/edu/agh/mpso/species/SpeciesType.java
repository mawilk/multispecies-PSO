package pl.edu.agh.mpso.species;

import java.awt.Color;

public enum SpeciesType {

	STANDARD(1.0, 1.0, 1.0, "STANDARD", new int[] {}), // 0

	// C1
	C1_A(1.0, 1.0, 1.0, "C1_A", new int[] { 2, 4 }), // 1
	C1_B(1.0, 0.0, 1.0, "C1_B", new int[] {}), // 2
	C1_C(0.0, 1.0, 1.0, "C1_C", new int[] { 2 }), // 3
	C1_D(1.0, 1.0, 0.0, "C1_D", new int[] { 3 }), // 4

	// C2
	C2_A(1.0, 1.0, 1.0, "C2_A", new int[] { 8 }), // 5
	C2_B(1.0, 0.0, 1.0, "C2_B", new int[] { 8 }), // 6
	C2_C(1.0, 0.0, 0.0, "C2_C", new int[] { 5 }), // 7
	C2_D(1.0, 1.0, 0.0, "C2_D", new int[] { 7 }), // 8

	// C3
	C3_A(1.0, 1.0, 1.0, "C3_A", new int[] {}), // 9
	C3_B(1.0, 0.0, 1.0, "C3_B", new int[] { 9 }), // 10
	C3_C(1.0, 1.0, 1.0, "C3_C", new int[] { 9 }), // 11
	C3_D(1.0, 1.0, 0.0, "C3_D", new int[] { 9 }), // 12

	// C4
	C4_A(1.0, 1.0, 1.0, "C4_A", new int[] {}), // 13
	C4_B(1.0, 1.0, 1.0, "C4_B", new int[] { 15, 16 }), // 14
	C4_C(1.0, 1.0, 0.0, "C4_C", new int[] { 13, 14 }), // 15
	C4_D(1.0, 1.0, 0.0, "C4_D", new int[] {}), // 16

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
