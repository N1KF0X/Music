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
    URL pageUrl;
    URL musicUrl;
    String page;

    public MusicDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
    }

    @Override
    public void run() {
        super.run();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern email_pattern = Pattern.compile("https:\\/\\/ru.hitmotop.com\\/get\\/music(.+).mp3");
        Matcher matcher = email_pattern.matcher(page);
        matcher.find();
        try {
            musicUrl = new URL(matcher.group());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(matcher.group());
        try {
            downloadUsingNIO(matcher.group(), "D:\\Music\\music.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream()); // Создает байтовый канал для чтения сайта
        FileOutputStream stream = new FileOutputStream(file); // Создает поток для записи
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);//Записывает в поток данные
        stream.close();
        byteChannel.close();
    }
}
