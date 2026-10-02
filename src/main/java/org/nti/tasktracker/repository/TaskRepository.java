package org.nti.tasktracker.repository;

import jakarta.persistence.EntityManager;
import org.nti.tasktracker.dto.TaskDto;
import org.nti.tasktracker.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final EntityManager em;

    public TaskRepository(EntityManager em) {
        this.em = em;
    }

    public Task save(Task task) {
        em.persist(task);
        return task;
    }

    public Task findById(Long id) {
        return em.find(Task.class, id);
    }

    public List<Task> findAll(int page, int size) {
        return em.createQuery("SELECT t FROM Task t ORDER BY t.id", Task.class)
                .setFirstResult(page * size)   // offset
                .setMaxResults(size)           // limit
                .getResultList();
    }

    public List<Task> findCompletedTask(boolean completed) {
        return em.createQuery(
                "SELECT t FROM Task t WHERE t.completed = :completed", Task.class
        ).setParameter("completed", completed).getResultList();
    }

    public Task updateTask(Long id, TaskDto taskDto) {
        Task task = findById(id);
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setDueDate(taskDto.dueDate());
        return save(task);
    }


    public Task markTaskAsCompleted(Long id) {
        Task task = findById(id);
        task.setCompleted(true);
        return save(task);
    }


    public void deleteTask(Long id) {
        Task task = findById(id);
        em.remove(task);
    }

    public long countTasks() {
        return em.createQuery("SELECT COUNT(t) FROM Task t", Long.class).getSingleResult();
    }

}
