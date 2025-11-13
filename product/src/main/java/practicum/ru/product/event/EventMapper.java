package practicum.ru.product.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practicum.ru.product.category.Category;
import practicum.ru.product.category.CategoryMapper;
import practicum.ru.product.dto.*;
import practicum.ru.product.user.User;
import practicum.ru.product.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public EventMapper(UserMapper userMapper, CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    public Event toEvent(NewEventDto newEventDto, Category category, User user, Status status) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        event.setLocation(toLocation(newEventDto.getLocation()));
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(status);
        return event;
    }

    public EventFullDto toEventFullDto(Event event, UserShortDto userShortDto, LocationDto locationDto, int views) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(userShortDto);
        eventFullDto.setLocation(locationDto);
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(views);
        return eventFullDto;
    }

    public LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }

    public Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setId(location.getId());
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }

    public EventShortDto toEventShortDto(Event event, int views) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(views);
        return eventShortDto;
    }
}
