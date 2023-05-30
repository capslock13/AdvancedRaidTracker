package com.cTimers.utility;

import com.cTimers.filters.cFilter;
import com.cTimers.filters.cImplicitFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class cFilterManager
{
    private static final String filterFolder = System.getProperty("user.home").replace("\\", "/") + "/.runelite/cTimers/filters/";
    public static ArrayList<cFilter> getFilters()
    {
        ArrayList<cFilter> currentFilters = new ArrayList<>();
        File folder = new File(filterFolder);
        try
        {
            for (File entry : folder.listFiles())
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
                            if (activeFileFilters.size() != 0)
                            {
                                currentFilters.add(new cFilter(entry.getName(), activeFileFilters.toArray(new String[activeFileFilters.size()])));
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return currentFilters;
    }

    public static boolean doesFilterExist(String name)
    {
        File folder = new File(filterFolder);
        try
        {
            for (File entry : folder.listFiles())
            {
                if (entry.getName().equals(name + ".filter"))
                {
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveOverwriteFilter(String name, ArrayList<cImplicitFilter> filters)
    {
        try
        {
            File directory = new File(filterFolder);
            if(!directory.exists())
            {
                directory.mkdirs();
            }
            File filterFile = new File(filterFolder + name + ".filter");

            if(filterFile.exists())
            {
                filterFile.delete();
            }
            filterFile.createNewFile();
            BufferedWriter filterWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filterFolder + name + ".filter"))));
            for(cImplicitFilter s : filters)
            {
                filterWriter.write(s.getFilterDescription());
                filterWriter.newLine();
            }
            filterWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveFilter(String name, ArrayList<cImplicitFilter> filters)
    {
        try
        {
            File directory = new File(filterFolder);
            if(!directory.exists())
            {
                directory.mkdirs();
            }
            File filterFile = new File(filterFolder + name + ".filter");
            if(!filterFile.exists())
            {
                filterFile.createNewFile();
            }
            BufferedWriter filterWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filterFolder + name + ".filter"))));
            for(cImplicitFilter filter : filters)
            {
                filterWriter.write(filter.getFilterCSV());
                filterWriter.newLine();
            }
            filterWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
