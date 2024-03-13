package com.example.processor.keylogger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyloggerProcessor {
    private static final List<String> MALWARE_WORDS = Arrays.asList(
            " netstat ", " ifconfig ", " ipconfig ", " whoami ", " id ", " uname ", " ver ", " ps ", " tasklist ",
            " cd ", " ls ", " dir ", " cp ", " move ", " del ", " rm ", " wget ", " curl ", " sudo ", " su ", " psexec ",
            " ssh ", " telnet ", " nc ", " scp ", " password ", " credentials ", " credit card ", " ssn ", " .pem ",
            " .key ", " .wallet ", " gpg ", " zip ", " rar ", " 7z ", " ftp ", " sftp ", " rsync ", " regedit ",
            " chregistry ", " sed ", " awk ", " echo ", " wireshark ", " tcpdump ");

    public void processLogFile(String inputFilePath, String outputCsvFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputFilePath));

        String content = String.join(" ", lines);

        String[] segments = content.split("Keystroke latency:");

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputCsvFilePath));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Keystroke Latency", "Backspace Count", "Right Click Count", "Left Click Count", "Malware Word Count"));

        Pattern latencyPattern = Pattern.compile("(\\d+\\.\\d+) ms");
        Pattern malwarePattern = Pattern.compile(String.join("|", MALWARE_WORDS), Pattern.CASE_INSENSITIVE);

        for (String segment : segments) {
            if (segment.trim().isEmpty()) continue;

            Matcher latencyMatcher = latencyPattern.matcher(segment);
            double latency = latencyMatcher.find() ? Double.parseDouble(latencyMatcher.group(1)) : 0;

            long backspaceCount = countOccurrences(segment, "backButton");
            long rightClickCount = countOccurrences(segment, "rclick");
            long leftClickCount = countOccurrences(segment, "lclick");
            long malwareWordCount = countMalwareWords(segment, malwarePattern);

            csvPrinter.printRecord(latency, backspaceCount, rightClickCount, leftClickCount, malwareWordCount);
        }

        csvPrinter.flush();
        csvPrinter.close();
        writer.close();
    }

    private long countOccurrences(String segment, String keyword) {
        return Arrays.stream(segment.split(" "))
                .filter(s -> s.equals(keyword))
                .count();
    }

    private long countMalwareWords(String segment, Pattern malwarePattern) {
        Matcher matcher = malwarePattern.matcher(segment);
        long count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        new KeyloggerProcessor().processLogFile("C:\\Users\\liais\\workspace\\licenta\\License-project\\processor\\keylogs.txt", "C:\\Users\\liais\\workspace\\licenta\\License-project\\keylogs.csv");
    }
}
