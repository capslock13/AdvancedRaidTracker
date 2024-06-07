package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.utility.JsonTypeAdapters;
import com.google.gson.*;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;

import static com.advancedraidtracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;


@Slf4j
public class ChartIO
{
	public static Gson gson;
    public static ChartIOData loadChartFromFile(String file)
    {
        try(FileReader reader = new FileReader(file))
        {
			gson.newBuilder().registerTypeAdapter(Color.class, new JsonTypeAdapters.ColorDeserializer()).create();
            return gson.fromJson(reader, ChartIOData.class);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static ChartIOData loadChartFromClipboard(String string)
    {
		gson.newBuilder().registerTypeAdapter(Color.class, new JsonTypeAdapters.ColorDeserializer()).create();
        return gson.fromJson(string, ChartIOData.class);
    }
    public static void saveChart(ChartPanel panel, String fileName)
    {
        try (FileWriter writer = new FileWriter(fileName))
        {
			gson.newBuilder().enableComplexMapKeySerialization().registerTypeAdapter(Color.class, new JsonTypeAdapters.ColorSerializer()).create();
            gson.toJson(panel.getForSerialization(), writer);
        }
        catch (Exception e)
        {
            log.info("Couldn't save chart: ");
            e.printStackTrace();
        }
    }
}
