package vadym.spring.console.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import vadym.spring.console.app.dto.LectureDto;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.entity.Lecture;
import vadym.spring.console.app.helpers.LectureHelper;
import ua.com.foxminded.vadym.spring.console.app.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.LectureService;
import vadym.spring.console.app.service.TeacherService;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LectureController.class)
@WithMockUser(roles = "ADMIN")
class LectureControllerIntegrationTest {

    private static final String LECTURES_PAGE = "/lectures";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private LectureHelper lectureHelper;

    @MockBean
    private CourseService courseService;

    @Test
    void givenLectures_whenGetLectures_thenReturnLecturesPage()
            throws Exception {

        Lecture lecture = new Lecture();

        List<Lecture> allLectures = Collections.singletonList(lecture);

        given(lectureService.findAllLectures(null)).willReturn(allLectures);

        mvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures"))
                .andExpect(model().attributeExists("lectures"))
                .andExpect(model().attribute("lectures", hasSize(1)));
    }

    @Test
    void testGetSelectionPage_whenGet_thenReturnTimetablePage() throws Exception {
        mvc.perform(get("/lectures/my-lectures"))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable"));
    }

    @Test
    void testAddLecture_whenAddGet_thenReturnCreatePage() throws Exception {
        given(groupService.findAll()).willReturn(Collections.singletonList(new Group()));
        given(teacherService.findAll()).willReturn(Collections.singletonList(new Teacher()));
        given(courseService.findAll()).willReturn(Collections.singletonList(new Course()));

        mvc.perform(get("/lectures/new-lecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-lecture"))
                .andExpect(model().attributeExists("lecture"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("teachers", hasSize(1)))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("courses", hasSize(1)))
                .andExpect(model().attribute("lecture", notNullValue()));
    }

    @Test
    void testEditLecture_whenEditLectureGet_thenReturnEditPage() throws Exception {
        LectureDto lectureDto = createLectureDto();

        given(lectureService.findById(lectureDto.getId())).willReturn(Optional.of(new Lecture()));
        given(lectureHelper.buildLectureDto(new Lecture())).willReturn(lectureDto);
        given(groupService.findAll()).willReturn(Collections.singletonList(new Group()));
        given(teacherService.findAll()).willReturn(Collections.singletonList(new Teacher()));
        given(courseService.findAll()).willReturn(Collections.singletonList(new Course()));

        mvc.perform(get("/lectures/edit-lecture/{id}", lectureDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-lecture"))
                .andExpect(model().attributeExists("lecture"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("teachers", hasSize(1)))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("courses", hasSize(1)))
                .andExpect(model().attribute("lecture", notNullValue()));
    }

    @Test
    void testGetMyLectures_whenGetMyLectures_thenReturnLecturePage() throws Exception {
        mvc.perform(post("/lectures/my-lectures/retrieve")
                        .with(csrf())
                        .param("startDate", String.valueOf(LocalDate.now()))
                        .param("endDate", String.valueOf(LocalDate.now())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LECTURES_PAGE));;
    }

    @Test
    void testCreateLecture_whenCreateLecturePost_thenReturnLecture() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        LectureDto lectureDto = createLectureDto();

        mvc.perform(post("/lectures/new-lecture/add")
                        .with(csrf())
                        .param("name", lectureDto.getName())
                        .param("date", String.valueOf(lectureDto.getDate()))
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LECTURES_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testAddLecture_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        LectureDto lectureDto = createLectureDto();

        mvc.perform(post("/lectures/new-lecture/add")
                        .with(csrf())
                        .param("name", lectureDto.getName())
                        .param("date", String.valueOf(lectureDto.getDate()))
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testEditLecture_whenEditLecturePost_thenReturnLecturePage() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        LectureDto lectureDto = createLectureDto();

        mvc.perform(post("/lectures/edit-lecture/update/{id}", lectureDto.getId())
                        .with(csrf())
                        .param("name", lectureDto.getName())
                        .param("date", String.valueOf(lectureDto.getDate()))
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LECTURES_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testEditLecture_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        LectureDto lectureDto = createLectureDto();

        mvc.perform(post("/lectures/edit-lecture/update/{id}", lectureDto.getId())
                        .with(csrf())
                        .param("name", lectureDto.getName())
                        .param("date", String.valueOf(lectureDto.getDate()))
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteLecture_whenDeleteLecture_thenReturnLectures() throws Exception {
        Long userId =  1L;

        doNothing().when(lectureService).delete(userId);

        mvc.perform(post("/lectures/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LECTURES_PAGE));

        verify(lectureService, times(1)).delete(userId);
    }

    @Test
    @WithAnonymousUser
    void testDeleteLecture_whenLectureIsAnonymous_thenReturnUnauthorized() throws Exception {
        Long userId =  1L;

        doNothing().when(lectureService).delete(userId);

        mvc.perform(post("/lecture/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    private LectureDto createLectureDto() {
        return LectureDto.builder()
                .id(1L)
                .name("lecture")
                .groupIds(Collections.singleton(1L))
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .room(1)
                .courseId(1L)
                .teacherId(1L)
                .build();
    }
}

