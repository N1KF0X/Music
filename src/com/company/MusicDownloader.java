package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MusicDownloader extends Thread{
    private final static String REGULAR_FOR_MUSIC_DOWNLOAD = "https:\\/\\/ru.hitmotop.com\\/get\\/music(.+).mp3";
    private final static String REGULAR_FOR_PICTURE_DOWNLOAD = "";
    private final static String REGULAR_FOR_NAME = "\\/track\\/dl\\/(.+)\\/";
    private final static String PATH = "music\\";
    private final static String PROTOCOL = "https://musify.club";
    URL pageUrl;
    String page;
    String link;

    public MusicDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
    }

    @Override
    public void run() {
        super.run();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));

            Pattern email_pattern = Pattern.compile(REGULAR_FOR_MUSIC_DOWNLOAD);
            Matcher matcher = email_pattern.matcher(page);
            matcher.find();
            link = matcher.group();

            ReadableByteChannel byteChannel = Channels.newChannel(new URL(PROTOCOL + link).openStream());
            String result = link.replaceAll(REGULAR_FOR_NAME, "");

            String fullPath = PATH + result.replaceAll(".mp3", "") + "\\";
            File file = new File(fullPath);
            if (file.mkdir()){
                System.out.println("hgh");
            }

            FileOutputStream stream = new FileOutputStream(PATH + result);
            stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);

            stream.close();
            byteChannel.close();

            email_pattern = Pattern.compile(REGULAR_FOR_PICTURE_DOWNLOAD);
            matcher = email_pattern.matcher(page);
            matcher.find();
            link = matcher.group();
            URL url = new URL(link);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(fullPath + "name.jpg");
            fos.write(response);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
