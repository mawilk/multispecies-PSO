package pl.edu.agh.mpso.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.mpso.species.SpeciesType;
import pl.edu.agh.mpso.swarm.SwarmInformation;

public class ConfigReader {
	public static Map<String, List<SwarmInformation>> getSwarms(String filename) {

		Map<String, List<SwarmInformation>> map = new HashMap<String, List<SwarmInformation>>();

		File file = new File(filename);
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(file));
			String swarmData = null;

			while ((swarmData = br.readLine()) != null) {
				String[] swarmConfig = swarmData.split(" ");

				String swarmName = swarmConfig[0];
				int speciesCount = Integer.parseInt(swarmConfig[1]);
				List<SwarmInformation> swarmInformations = new ArrayList<SwarmInformation>();

				for (int i = 0; i < speciesCount; i++) {
					String speciesData = br.readLine();
					String[] speciesConfig = speciesData.split(" ");

					SpeciesType type = SpeciesType.valueOf(speciesConfig[0]);
					int count = Integer.parseInt(speciesConfig[1]);

					swarmInformations.add(new SwarmInformation(count, type));
				}

				map.put(swarmName, swarmInformations);
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	public static List<SwarmInformation> getSwarm(String filename) {
		List<SwarmInformation> swarmInformations = new ArrayList<SwarmInformation>();
		File file = new File(filename);
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(file));
			String speciesData = null;

			while ((speciesData = br.readLine()) != null) {
				String[] speciesConfig = speciesData.split(" ");

				SpeciesType type = SpeciesType.valueOf(speciesConfig[0]);
				int count = Integer.parseInt(speciesConfig[1]);

				swarmInformations.add(new SwarmInformation(count, type));
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return swarmInformations;
	}
}
