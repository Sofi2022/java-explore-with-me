package ru.practicum.adminAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.CategoryDto;
import ru.practicum.categories.ResponseCategoryDto;
import ru.practicum.categories.NewCategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.UpdateCommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.compilations.CompilationDto;
import ru.practicum.compilations.NewCompilationDto;
import ru.practicum.compilations.UpdateCompilationRequest;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.events.EventFullDto;
import ru.practicum.events.UpdateEventAdminRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.user.NewUserRequestDto;
import ru.practicum.user.UserDto;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping (path = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final CategoriesService categoriesService;

    private final EventService eventService;

    private final CompilationService compilService;


    private final CommentService commentService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequestDto user) {
        log.info("Admin: Вызван метод addUser");
        return userService.addUser(user);
    }


    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam (name = "ids") List<Long> userIds, @RequestParam(name = "from", defaultValue = "0")
    Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Admin: Вызван метод getUsers {}, size {}, from {}", userIds, from, size);
        return userService.getUsers(userIds, from, size);
    }


    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Admin: Вызван метод deleteUser " + userId);
        userService.deleteUser(userId);
    }


    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategoryDto addCategory(@RequestBody @Valid NewCategoryDto category) {
        log.info("Admin: Вызван метод addCategory");
        return categoriesService.addCategory(category);
    }


    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Admin: Вызван метод deleteCategory " + catId);
        categoriesService.deleteCategory(catId);
    }


    @PatchMapping("/categories/{catId}")
    public ResponseCategoryDto updateCategory(@RequestBody CategoryDto category, @PathVariable Long catId) {
        log.info("Admin: Вызван метод updateCategory " + catId);
        return categoriesService.update(category, catId);
    }


    @PatchMapping("events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @Valid @RequestBody UpdateEventAdminRequest event) {
        log.info("Admin: Вызван метод updateEventByAdmin " + eventId);
        return eventService.updateEventByAdmin(eventId, event);
    }


    @GetMapping("/events")
    public List<EventFullDto> searchEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                           @RequestParam(name = "states", required = false)
    List<String> states, @RequestParam(name = "categories", required = false) List<Long> categories,
                                           @RequestParam(name = "rangeStart", required = false)
                                           String rangeStart, @RequestParam(name = "rangeEnd", required = false)
    String rangeEnd, @RequestParam(name = "from", defaultValue = "0") Integer from, @RequestParam(name = "size",
            defaultValue = "10") Integer size, HttpServletRequest request) {
        log.info("Admin: Вызван метод searchEvents ");
        return eventService.searchEvents(userIds, states, categories, rangeStart, rangeEnd, from, size, request);
    }


    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.info("Admin: Вызван метод addCompilation ");
        return compilService.addCompilation(compilation);
    }


    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Admin: Вызван метод deleteCompilation " + compId);
        compilService.deleteCompilation(compId);
    }


    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody UpdateCompilationRequest compil) {
        log.info("Admin: Вызван метод updateCompilation " + compId);
        return compilService.updateCompilation(compId, compil);
    }


    @PatchMapping("/comments/{comId}")
    public CommentDto updateComment(@PathVariable Long comId, @Valid @RequestBody UpdateCommentDto comment) {
        log.info("Admin: Вызван метод updateComment {}", comId);
        return commentService.updateComment(comId, comment);
    }
}
