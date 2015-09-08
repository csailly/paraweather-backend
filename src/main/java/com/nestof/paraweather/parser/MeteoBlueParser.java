package com.nestof.paraweather.parser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nestof.paraweather.model.DayForecast;
import com.nestof.paraweather.model.HourForecast;

public class MeteoBlueParser {

	private static Logger log = LoggerFactory.getLogger(MeteoBlueParser.class);

	private Document doc;

	public void init(String url) {
		try {
			log.debug("Get datas from " + url);
			Connection connection = Jsoup.connect(url);
			connection.cookie("addparam", "true");

			doc = connection.get();
		} catch (IOException e) {
			log.error("Error retrieving html {}", url, e);
			return;
		}
	}

	public DayForecast getForecast() {
		DayForecast dayForecast = new DayForecast();

		Element datas = doc.select("div[data-day].active").first();
		
		dayForecast.setLastUpdateTime(extractLastUpdateTime(datas.text()));

		// **********Daily datas************
		Element date = datas.select("tr.times > th time").first();
		dayForecast.setDate(date.attr("datetime"));

		// Heures d'ensoleillement
		Element sunnyHours = datas.select("div.clearfix div.sun div.sun_icon")
				.first();
		dayForecast.setSunnyHours(Integer.parseInt(extractSunnyHours(sunnyHours.attr("title"))));

		// Lever / Couché soleil
		Element sunHours = datas.select(
				"div.clearfix div.sun div:has(span.winddir)").first();
		dayForecast.setSunrise(extractSunrise(sunHours.text()));
		dayForecast.setSunset(extractSunset(sunHours.text()));

		// ***************3h datas**********************
		Elements times = datas.select("tr.times > td time");
		Elements weather = datas
				.select("tr.icons > td > div > div > div[title]");
		Elements temperatures = datas.select("tr.temperatures > td > div");
		Elements windchills = datas.select("tr.windchills > td > div");
		Elements winddirs = datas.select("tr.winddirs > td > div > div");
		Elements windspeeds = datas.select("tr.windspeeds > td > div");
		Elements precips = datas.select("tr.precips > td > div");

		for (Element element : times) {
			int index = times.indexOf(element);
			HourForecast hourForecast = new HourForecast();

			hourForecast.setHour(Integer.parseInt(StringUtils.split(
					element.attr("datetime"), ":")[0]));
			hourForecast.setWeatherLabel(weather.get(index).attr("title"));
			hourForecast.setTemperature(Float.parseFloat(StringUtils.remove(
					temperatures.get(index).text(), '°')));
			hourForecast.setWindChill(Float.parseFloat(StringUtils.remove(
					windchills.get(index).text(), '°')));
			hourForecast.setWindDirection(StringUtils.split(winddirs.get(index)
					.attr("class"), " ")[2]);
			hourForecast.setWindSpeed(Float.parseFloat(StringUtils.split(
					windspeeds.get(index).text(), "-")[0]));

			if (StringUtils.contains(windspeeds.get(index).text(), "-")) {
				hourForecast.setWindGust(Float.parseFloat(StringUtils.split(
						windspeeds.get(index).text(), "-")[1]));
			}
			hourForecast.setPrecips(precips.get(index).text());
			dayForecast.getThreeHoursForecast().put(hourForecast.getHour(),
					hourForecast);
		}

		// Pression atmosphérique
		Element airpressure = datas.select("div.airpressure").first();
		dayForecast.setPressure(extractPressure(airpressure.text()));

		// ****************Hourly datas***************
		Element hourly = datas.select("table.hourlywind").first();

		Elements hourlyTimes = hourly.select("tr:has(span.clock)").first()
				.select("td");
		Elements hourlyWeather = hourly.select("tr.pictos_1h div.picon");
		Elements hourlyTemperature = hourly.select("tr:has(span.temperature)")
				.first().select("td");
		Elements hourlyWinddir = hourly.select("tr:has(span.winddir)").first()
				.select("td span.winddir");
		Elements hourlyWindspeed = hourly.select("tr:has(span.windspeed)")
				.first().select("td");
		Elements hourlyWindgust = hourly.select("tr:has(span.windgust)")
				.first().select("td");

		for (Element element : hourlyTimes) {
			int index = hourlyTimes.indexOf(element);
			HourForecast hourForecast = new HourForecast();
			hourForecast.setHour(Integer.parseInt(element.text()));
			hourForecast
					.setWeatherLabel(hourlyWeather.get(index).attr("title"));
			hourForecast.setTemperature(Float.parseFloat(hourlyTemperature.get(
					index).text()));
			hourForecast.setWindDirection(StringUtils.split(
					hourlyWinddir.get(index).attr("class"), " ")[2]);
			hourForecast.setWindSpeed(Float.parseFloat(hourlyWindspeed.get(
					index).text()));
			hourForecast.setWindGust(Float.parseFloat(hourlyWindgust.get(index)
					.text()));

			dayForecast.getHourlyForecast().put(hourForecast.getHour(),
					hourForecast);
		}

		return dayForecast;
	}

	
	private  String extractSunnyHours(String stringToSearch) {
		// the pattern we want to search for
		Pattern p = Pattern.compile(" (\\d*)h");
		Matcher m = p.matcher(stringToSearch);
		if (m.find())
	    {
	      // we're only looking for one group, so get it
			return m.group(1);
	    }
		return null;
	}
	
	private String extractPressure(String stringToSearch) {
		// the pattern we want to search for
		Pattern p = Pattern.compile(" (\\d*) ");
		Matcher m = p.matcher(stringToSearch);
		if (m.find())
	    {
	      // we're only looking for one group, so get it
			return m.group(1);
	    }
		return null;
	}
	
	private String extractSunrise(String stringToSearch) {
		// the pattern we want to search for
		Pattern p = Pattern.compile("\\D*(\\d{2}:\\d{2}).*");
		Matcher m = p.matcher(stringToSearch);
		if (m.find())
	    {
	      // we're only looking for one group, so get it
			return m.group(1);
	    }
		return null;
	}
	
	private String extractSunset(String stringToSearch) {
		// the pattern we want to search for
		Pattern p = Pattern.compile(".*(\\d{2}:\\d{2}).*");
		Matcher m = p.matcher(stringToSearch);
		if (m.find())
	    {
	      // we're only looking for one group, so get it
			return m.group(1);
	    }
		return null;
	}
	
	private String extractLastUpdateTime(String stringToSearch) {
		// the pattern we want to search for
		Pattern p = Pattern.compile("Dernière mise à jour: (.*) CEST");
		Matcher m = p.matcher(stringToSearch);
		if (m.find())
	    {
	      // we're only looking for one group, so get it
			return m.group(1);
	    }
		return null;
	}

	public static void main(String[] args) {
		MeteoBlueParser meteoBlueParser = new MeteoBlueParser();
		meteoBlueParser
		.init("https://www.meteoblue.com/fr/meteo/prevision/semaine/la-comt%C3%A9_france_3009940?day=2");
		DayForecast dayForecast = meteoBlueParser.getForecast();
		
		System.out.println(dayForecast);
	}
}
