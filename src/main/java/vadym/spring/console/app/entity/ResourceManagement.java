package vadym.spring.console.app.entity;

public interface ResourceManagement<T> {
    public Timetable getTimetableForDay(T entity);

    public Timetable getTimetableForMonth(T entity);
}
