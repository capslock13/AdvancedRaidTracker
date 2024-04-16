package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.utility.JsonTypeAdapters;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;

import static com.advancedraidtracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;

@Slf4j
public class ChartIO
{

    public static ChartIOData loadChartFromFile(String file)
    {
        try(FileReader reader = new FileReader(file))
        {
            Gson gson = new GsonBuilder().registerTypeAdapter(Color.class, new JsonTypeAdapters.ColorDeserializer()).create();
            return gson.fromJson(reader, ChartIOData.class);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static ChartIOData loadChartFromClipboard(String string)
    {
        Gson gson = new GsonBuilder().registerTypeAdapter(Color.class, new JsonTypeAdapters.ColorDeserializer()).create();
        return gson.fromJson(string, ChartIOData.class);
    }
    public static void saveChart(ChartPanel panel)
    {
        try (FileWriter writer = new FileWriter(PLUGIN_DIRECTORY+"misc-dir/test.json"))
        {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(Color.class, new JsonTypeAdapters.ColorSerializer()).create();
            gson.toJson(panel.getForSerialization(), writer);
        }
        catch (Exception e)
        {
            log.info("Couldn't save chart: ");
            e.printStackTrace();
        }
        //int start tick
        //int end tick
        //string room name
        //string room specific text
        //List int autos
        //Map<Int, String> npcmap
        //List<DawnSpec> dawnspecs
        //List<ThrallOutlineBox> thrall boxes
        //List<OutlineBox> outlineBoxes
        //Map<Integer, String> room specific text mapping
        //Map<Integer, String> lines
        //Map<Integer, Integer> roomHP
        //
    }
}
