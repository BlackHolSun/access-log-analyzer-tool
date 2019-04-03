package com.imladyartist.accessloganalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parsing logs to Content class fields
 * */


public class FileParser {

    public List<Content> fileParsing(File file) {

        List<Content> lines = new ArrayList<Content>();

        BufferedReader fileReader;

        Content content;


        try {
            fileReader = new BufferedReader(new FileReader(file));

            String line;
            String acsLine;

            while ((line = fileReader.readLine()) != null) {

                String testLine = line.substring(line.indexOf("- - \""), line.indexOf("HTTP"));

                //only ACS related lines needed

                if (testLine.contains("/acs")) {

                    acsLine = line.substring(line.indexOf("\"line\" : \"") + 10);

                    String dateTime = acsLine.substring(acsLine.indexOf("\"[") + 2, acsLine.indexOf("]"));


                    String operation = acsLine.substring(acsLine.indexOf("- - \"") + 5, acsLine.indexOf("- - \"") + 8);


                    String url = acsLine.substring(acsLine.indexOf("/acs"), acsLine.indexOf("HTTP/"));

                    String responseCode = acsLine.substring(acsLine.indexOf("HTTP") + 10, acsLine.indexOf("HTTP") + 13);

                    String tempDurationPrep;

                    String bearer = null;
                    String bearerPrep = acsLine.substring(acsLine.indexOf("Bearer") + 7);


                    //if there's no Bearer we put null

                    if (acsLine.contains("Bearer")) {
                        tempDurationPrep = acsLine.substring(acsLine.indexOf("HTTP") + 14, acsLine.indexOf("Bearer") - 1);


                        String[] bearerPrepParts = bearerPrep.split(" ");
                        bearer = bearerPrepParts[0];
                    } else {


                        String intermediate = acsLine.substring(acsLine.indexOf("/acs"));
                        tempDurationPrep = intermediate.substring(intermediate.indexOf("HTTP") + 14);
                    }

                    String[] splitToGetDuration = tempDurationPrep.split(" ");


                    String size = splitToGetDuration[0];

                    String duration = splitToGetDuration[1];

                    String [] userAgentPrep = acsLine.split("\"");
                    int length = userAgentPrep.length;

                    String userAgent = userAgentPrep[length - 3];


                    content = new Content(dateTime, operation, url, responseCode, size, duration, bearer, userAgent);
                    lines.add(content);


                }

            }

            fileReader.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();

        }



        return lines;

    }
}
