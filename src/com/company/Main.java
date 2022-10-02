package com.company;

import java.net.MalformedURLException;

public class Main {

    public static void main(String[] args) throws MalformedURLException {
	    MusicDownloader musicDownloader = new MusicDownloader("https://ru.hitmotop.com/song/56774126");
        musicDownloader.start();
    }
}
