package ipod.base;

import java.util.HashMap;
import java.util.Map;

import ipod.Constants;

public class IpodInventory {

	public static int getLocalCost(String country) {

		if (country.equalsIgnoreCase(Constants.BRAZIL)) {
			return Constants.IPOD_COST_IN_BRAZIL;
		}
		return Constants.IPOD_COST_IN_ARGENTINA;
	}

	public static int getImportCost(String country) {

		if (country.equalsIgnoreCase(Constants.BRAZIL)) {
			return Constants.IPOD_COST_IN_ARGENTINA;
		}
		return Constants.IPOD_COST_IN_BRAZIL;
	}

	public static String getIpodInventoryDetails(String ipodsWithCountry) {

		String[] ipodsWithCountryArr = ipodsWithCountry.split(":");
		int reqIpods = Integer.parseInt(ipodsWithCountryArr[1]);
		if (reqIpods > Constants.OUT_OF_STOCK_SIZE) {
			return Constants.OUT_OF_STOCK + ":" + Constants.INVENTORY_SIZE + ":" + Constants.INVENTORY_SIZE + ";";
		}
		int remainingInBr = 0;
		int remainingInArg = 0;

		int localCost = 0;
		int importCost = 0;
		int ipodsCost = 0;
		Map<String, int[]> invWithCountryMap = new HashMap<>();
		Map<Integer, int[]> invMap = new HashMap<>();

		if (reqIpods <= Constants.INVENTORY_SIZE) {

			localCost = reqIpods * getLocalCost(ipodsWithCountryArr[0]);
			invMap.put(localCost, new int[] { Constants.INVENTORY_SIZE - reqIpods, Constants.INVENTORY_SIZE });

			importCost = ((reqIpods / Constants.IMPORT_UNITS) * Constants.IMPORT_UNITS)
					* getImportCost(ipodsWithCountryArr[0])
					+ (Constants.IMPORT_COST * (reqIpods / Constants.IMPORT_UNITS))
					+ (reqIpods % Constants.IMPORT_UNITS) * getLocalCost(ipodsWithCountryArr[0]);
			invMap.put(importCost, new int[] { Constants.INVENTORY_SIZE - reqIpods % Constants.IMPORT_UNITS,
					Constants.INVENTORY_SIZE - ((reqIpods / Constants.IMPORT_UNITS) * Constants.IMPORT_UNITS) });

			ipodsCost = Math.min(localCost, importCost);
			invWithCountryMap.put(ipodsWithCountryArr[0], invMap.get(ipodsCost));

		} else {

			localCost = Constants.INVENTORY_SIZE * getLocalCost(ipodsWithCountryArr[0])
					+ ((reqIpods - Constants.INVENTORY_SIZE) / Constants.IMPORT_UNITS) * Constants.IMPORT_UNITS
							* getImportCost(ipodsWithCountryArr[0])
					+ (Constants.IMPORT_COST * (reqIpods - Constants.INVENTORY_SIZE) / Constants.IMPORT_UNITS)
					+ ((reqIpods - Constants.INVENTORY_SIZE) % Constants.IMPORT_UNITS)
							* getLocalCost(ipodsWithCountryArr[0]);
			invMap.put(localCost, new int[] { 0, Constants.INVENTORY_SIZE - (reqIpods - Constants.INVENTORY_SIZE) });

			importCost = Constants.INVENTORY_SIZE * getImportCost(ipodsWithCountryArr[0])
					+ (Constants.INVENTORY_SIZE / Constants.IMPORT_UNITS) * Constants.IMPORT_COST
					+ (reqIpods - Constants.INVENTORY_SIZE) * getLocalCost(ipodsWithCountryArr[0]);
			invMap.put(importCost, new int[] { Constants.INVENTORY_SIZE - (reqIpods - Constants.INVENTORY_SIZE), 0 });

			ipodsCost = Math.min(localCost, importCost);
			invWithCountryMap.put(ipodsWithCountryArr[0], invMap.get(ipodsCost));

		}

		int[] invArr = new int[] {};
		if (ipodsWithCountryArr[0].equalsIgnoreCase(Constants.BRAZIL)) {
			invArr = invWithCountryMap.get(Constants.BRAZIL);
			remainingInBr = invArr[0];
			remainingInArg = invArr[1];
		} else {
			invArr = invWithCountryMap.get(Constants.ARGENTINA);
			remainingInArg = invArr[0];
			remainingInBr = invArr[1];
		}

		return String.valueOf(ipodsCost) + ":" + String.valueOf(remainingInBr) + ":" + String.valueOf(remainingInArg)
				+ ";";
	}

	public static void main(String[] args) {
		System.out.println(getIpodInventoryDetails("BRAZIL:120"));
	}
}
