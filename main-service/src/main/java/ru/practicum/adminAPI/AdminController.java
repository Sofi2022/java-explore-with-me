package ru.practicum.adminAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.ResponseCategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final CategoriesService categoriesService;

    private final EventService eventService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequestDto user) {
        log.info("Вызван метод addUser");
        return userService.addUser(user);
    }


    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam (name = "ids") List<Long> userIds, @RequestParam(name = "from", defaultValue = "0")
    Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вызван метод getUsers {}, size {}, from {}", userIds, from, size);
        return userService.getUsers(userIds, from, size);
    }


    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Вызван метод deleteUser " + userId);
        userService.deleteUser(userId);
    }


    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategoryDto addCategory(@RequestBody @Valid NewCategoryDto category) {
        log.info("Вызван метод addCategory");
        return categoriesService.addCategory(category);
    }


    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Вызван метод deleteCategory " + catId);
        categoriesService.deleteCategory(catId);
    }


    @PatchMapping("/categories{catId}")
    public ResponseCategoryDto updateCategory(@Valid @RequestBody CategoryDto category, @PathVariable Long catId) {
        log.info("Вызван метод updateCategory " + catId);
        return categoriesService.update(category, catId);
    }


    @PatchMapping("events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @Valid @RequestBody UpdateEventAdminRequest event) {
        log.info("Вызван метод updateEventByAdmin " + eventId);
        return eventService.updateEventByAdmin(eventId, event);
    }

    @GetMapping("/events")
    public List<EventFullDto> searchEvents(@RequestParam(name = "users") List<Long> userIds, @RequestParam(name = "states")
    List<String> states, @RequestParam(name = "categories") List<Long> categories, @RequestParam(name = "rangeStart")
                                           String rangeStart, @RequestParam(name = "rangeEnd")
    String rangeEnd, @RequestParam(name = "from",
            defaultValue = "0") Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.searchEvents(userIds, states, categories, rangeStart, rangeEnd, from, size);
    }
}
