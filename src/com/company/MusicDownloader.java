package com.company;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MusicDownloader extends Thread{
    private final static String REGULAR = "\\/track\\/dl(.+).mp3";
    private final static String PATH = "D:\\Music\\music.mp3";
    URL pageUrl;
    String page;

    public MusicDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
    }

    @Override
    public void run() {
        super.run();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));

            Pattern email_pattern = Pattern.compile(REGULAR);
            Matcher matcher = email_pattern.matcher(page);
            matcher.find();

            ReadableByteChannel byteChannel = Channels.newChannel(new URL("https://musify.club" + matcher.group()).openStream());
            FileOutputStream stream = new FileOutputStream(PATH);
            stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);

            stream.close();
            byteChannel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
