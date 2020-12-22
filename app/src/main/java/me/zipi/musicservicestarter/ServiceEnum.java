package me.zipi.musicservicestarter;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum ServiceEnum {
    Melon("com.iloen.melon", "com.iloen.melon.playback.PlaybackService"),
    Vibe("com.naver.vibe", "com.naver.playback.service.MediaPlaybackService");


    @Getter
    private final String pkg;
    @Getter
    private final String service;

    ServiceEnum(String pkg, String service) {
        this.pkg = pkg;
        this.service = service;
    }
}
