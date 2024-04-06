package com.advancedraidtracker.filters;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class FilterManager
{
    private static final String filterFolder = System.getProperty("user.home").replace("\\", "/") + "/.runelite/advancedraidtracker/misc-dir/filters/";

    public static ArrayList<Filter> getFilters()
    {
        ArrayList<Filter> currentFilters = new ArrayList<>();
        File folder = new File(filterFolder);
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles()))
            {
                ArrayList<String> activeFileFilters = new ArrayList<>();
                if (entry.isFile())
                {
                    if (entry.getAbsolutePath().endsWith(".filter"))
                    {
                        try
                        {
                            Scanner filterReader = new Scanner(Files.newInputStream(entry.toPath()));
                            while (filterReader.hasNextLine())
                            {
                                activeFileFilters.add(filterReader.nextLine());
                            }
                            filterReader.close();
                            if (!activeFileFilters.isEmpty())
                            {
                                currentFilters.add(new Filter(entry.getName(), activeFileFilters.toArray(new String[0])));
                            }
                        } catch (Exception e)
                        {
                            log.info("Failed to read filter filter");
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            log.info("Failed retrieving filters");
        }
        return currentFilters;
    }

    public static boolean doesFilterExist(String name)
    {
        File folder = new File(filterFolder);
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles()))
            {
                if (entry.getName().equals(name + ".filter"))
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            log.info("Failed to find filter");
        }
        return false;
    }

    public static void saveOverwriteFilter(String name, ArrayList<ImplicitFilter> filters)
    {
        try
        {
            File directory = new File(filterFolder);
            if (!directory.exists())
            {
                if (!directory.mkdirs())
                {
                    log.info("Could not make directories to save filter");
                }
            }
            File filterFile = new File(filterFolder + name + ".filter");

            if (filterFile.exists())
            {
                if (!filterFile.delete())
                {
                    log.info("Could not delete old filter");
                }
            }
            if (!filterFile.createNewFile())
            {
                log.info("Could not create new filter filter");
            }
            BufferedWriter filterWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filterFolder + name + ".filter"))));
            for (ImplicitFilter s : filters)
            {
                filterWriter.write(s.getFilterDescription());
                filterWriter.newLine();
            }
            filterWriter.close();
        } catch (Exception ignored)
        {
        }
    }

    public static void saveFilter(String name, ArrayList<ImplicitFilter> filters)
    {
        try
        {
            File directory = new File(filterFolder);
            if (!directory.exists())
            {
                if (!directory.mkdirs())
                {
                    log.info("Could not make directory to save filter");
                }
            }
            File filterFile = new File(filterFolder + name + ".filter");
            if (!filterFile.exists())
            {
                if (!filterFile.createNewFile())
                {
                    log.info("Could not create new filter file");
                }
            }
            BufferedWriter filterWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filterFolder + name + ".filter"))));
            for (ImplicitFilter filter : filters)
            {
                filterWriter.write(filter.getFilterCSV());
                filterWriter.newLine();
            }
            filterWriter.close();
        } catch (Exception ignored)
        {
        }
    }

    public static void saveFilter(String name, ArrayList<ImplicitFilter> filters, ArrayList<String> quickFiltersState)
    {
        try
        {
            File directory = new File(filterFolder);
            if (!directory.exists())
            {
                if (!directory.mkdirs())
                {
                    log.info("Could not make folder to save filters");
                }
            }
            File filterFile = new File(filterFolder + name + ".filter");
            if (!filterFile.exists())
            {
                if (!filterFile.createNewFile())
                {
                    log.info("Could not create new filter file");
                }
            }
            BufferedWriter filterWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filterFolder + name + ".filter"))));
            for (ImplicitFilter filter : filters)
            {
                filterWriter.write(filter.getFilterCSV());
                filterWriter.newLine();
            }
            for (String s : quickFiltersState)
            {
                filterWriter.write(s);
                filterWriter.newLine();
            }
            filterWriter.close();
        } catch (Exception ignored)
        {
        }
    }

}
