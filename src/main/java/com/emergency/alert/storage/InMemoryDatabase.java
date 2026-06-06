package com.emergency.alert.storage;

import com.emergency.alert.entity.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDatabase {

    public static final Map<Long, EmergencyEvent> EVENTS = new ConcurrentHashMap<>();
    public static final Map<Long, GeoZone> ZONES = new ConcurrentHashMap<>();
    public static final Map<Long, User> USERS = new ConcurrentHashMap<>();
    public static final Map<Long, Notification> NOTIFICATIONS = new ConcurrentHashMap<>();
    public static final Map<Long, UserResponse> RESPONSES = new ConcurrentHashMap<>();

    public static final AtomicLong EVENT_SEQ = new AtomicLong(1);
    public static final AtomicLong ZONE_SEQ = new AtomicLong(1);
    public static final AtomicLong USER_SEQ = new AtomicLong(1);
    public static final AtomicLong NOTIF_SEQ = new AtomicLong(1);
    public static final AtomicLong RESP_SEQ = new AtomicLong(1);
}