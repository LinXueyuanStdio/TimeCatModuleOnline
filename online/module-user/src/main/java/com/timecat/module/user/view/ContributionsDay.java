package com.timecat.module.user.view;

import org.joda.time.DateTime;

/**
 * Copyright 2016 Javier González All right reserved.
 */
public class ContributionsDay {
    public int year;
    public int month;
    public int day;

    // Level is used to record the color of the block
    public int level;
    // Data is used to calculated the height of the pillar
    private int data;

    public ContributionsDay(int year, int month, int day, int level, int data) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.level = level;
        this.data = data;
    }

    public int key() {
        DateTime dateTime = new DateTime(year, month, day, 0,0);
        return dateTime.getDayOfYear();
    }
}
