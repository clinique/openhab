/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.toolbox.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for toolbox.
 * 
 * @author clinique
 * @since 1.6.0
 */
public class toolbox {

	private static final Logger logger = LoggerFactory.getLogger(toolbox.class);
	
	/**
	 * Compute the distance between two points",
	 * 
	 * @param latitude of the first point
	 * @param longitude of the first point
	 * @param latitude of the second point
	 * @param longitude of the second point
	 * 
	 * @return distance between the two points in meters 
	 */
	@ActionDoc(text="Compute the distance between two points", 
			returns="distance between the two points in meters")
	public static double getDistance(
			@ParamDoc(name="Point a Latitude") double aLat, 
			@ParamDoc(name="Point a Longitude") double aLng, 
			@ParamDoc(name="Point b Latitude") double bLat,
			@ParamDoc(name="Point b Longitude") double bLng){
		
		double earthRadiusMeter = 3958.75 * 2 * 16090;
		//int meterConversion = 16090;
		
		double dLat = Math.pow(Math.sin(Math.toRadians(bLat - aLat) / 2),2);
		double dLng = Math.pow(Math.sin(Math.toRadians(bLng - aLng) / 2),2);
		
		double a = 	dLat + Math.cos(Math.toRadians(aLat)) * Math.cos(Math.toRadians(bLat)) * dLng;

		double dist = earthRadiusMeter * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		return dist; 				
	}

	/**
	 * Compute the Humidex index"
	 * http://en.wikipedia.org/wiki/Humidex
	 * @param temperature  in (°C)
	 * @param hygro relative level (%) 
	 * @return distance between the two points in meters 
	 */
	@ActionDoc(text="Compute the Humidex index given temperature and hygrometry", 
			returns="Humidex index value")
	public static double getHumidex(
		@ParamDoc(name="Temperature") double temperature, 
		@ParamDoc(name="Relative hygro level") double hygro) { 
			
			double result = 6.112 * Math.pow(10, 7.5 * temperature/(237.7 + temperature)) * hygro/100;
			result = temperature + 0.555555556 * (result - 10);
			return result;
	}
	
	/**
	 * Compute the Beaufort scale for a given wind speed
	 * http://en.wikipedia.org/wiki/Beaufort_scale
	 * @param wind speed in m/s 
	 * @return Beaufort Index between 0 and 12 
	 */
	public static int getBeaufortIndex(
			@ParamDoc(name="WindSpeed") double speed) { 
			int result;
			if 		(speed < 0.3)  result = 0;
			else if (speed < 1.6)  result = 1;
			else if (speed < 3.4)  result = 2;
			else if (speed < 5.5)  result = 3;
			else if (speed < 8)    result = 4;
			else if (speed < 10.8) result = 5;
			else if (speed < 13.9) result = 6;	
			else if (speed < 17.2) result = 7;
			else if (speed < 20.8) result = 8;
			else if (speed < 24.5) result = 9;
			else if (speed < 28.5) result = 10;
			else if (speed < 32.7) result = 11;
			else result = 12;
			
			return result;
		}
	
	/**
	 * Transform an orientation angle to its
	 * cardinal string equivalent
	 * @param bearing in ° 
	 * @return String representing the direction 
	 */
	public static String degToCompass(double bearing) { 
	    if (bearing < 0 && bearing > -180) {
	        // Normalize to [0,360]
	        bearing = 360.0 + bearing;
	      }
	      if (bearing > 360 || bearing < -180) {
	        return "Unknown";
	      }

	      String directions[] = {
	        "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
	        "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW",
	        "N"};
	      String cardinal = directions[(int) Math.floor(((bearing + 11.25) % 360) / 22.5)];
	      return cardinal;
	}

	/**
	 * Compute the Sea Level Pressure
	 * @param absolute pressure hPa 
	 * @param temperature (°C)
	 * @param altitude in meter
	 * @return Equivalent Seal Level pressure 
	 * 
	 * http://keisan.casio.com/exec/system/1224575267
	 * 
	 */
	public static double getSeaLevelPressure( double pressure, double temp, double altitude) {
		double 	x = 0.0065 * altitude;
				x = (1 - x/(temp + x + 273.15));
		double result = pressure * Math.pow(x,-5.257);
		return result;
	}
}

