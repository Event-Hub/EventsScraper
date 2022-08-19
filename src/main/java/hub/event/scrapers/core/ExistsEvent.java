package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;

record ExistsEvent(int eventId, String title, String description,
                   SingleEventDateWithLocation singleEventDateWithLocation,
                   MultipleEventDateWithLocations multipleEventDateWithLocations) {
}
