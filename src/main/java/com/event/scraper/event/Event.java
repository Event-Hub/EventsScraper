package com.event.scraper.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {

    private Long eventId;
    private String city;
    private String title;
    private String description;
    private String address;
    private LocalDate date;
    private LocalTime time;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Event() {
    }

    public Event(String city, String title, String description, String address, LocalDate date, LocalTime time) {
        this.city = city;
        this.title = title;
        this.description = description;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public Event(Long eventId, String city, String title, String description, String address, LocalDate date, LocalTime time) {
        this.eventId = eventId;
        this.city = city;
        this.title = title;
        this.description = description;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId) && Objects.equals(city, event.city) && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(address, event.address) && Objects.equals(date, event.date) && Objects.equals(time, event.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, city, title, description, address, date, time);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", city='" + city + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
