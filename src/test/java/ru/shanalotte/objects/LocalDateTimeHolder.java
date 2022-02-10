package ru.shanalotte.objects;

import java.time.LocalDateTime;

public class LocalDateTimeHolder {

    private LocalDateTime time = LocalDateTime.now();

    @Override
    public String toString() {
        return "LocalDateTimeHolder{" +
                "time=" + time +
                '}';
    }
}
