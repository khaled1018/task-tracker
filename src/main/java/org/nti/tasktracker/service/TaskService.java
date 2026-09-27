package org.nti.tasktracker;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskTrackerProperties props;



    public TaskService(TaskRepository taskRepository, TaskTrackerProperties props) {
        this.taskRepository = taskRepository;
        this.props = props;
    }

    @Transactional
    public Task createTask(TaskDto taskDto) {
        if(taskRepository.countTasks() >= 3){
            throw new MaxTasksExceededException("Cannot create task: maximum of 3 tasks reached.");
        }
        Task task = new Task();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setDueDate(taskDto.dueDate());
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(int page, Integer size) {

        int pageSize = (size != null) ? size : props.getDefaultPageSize();

        if (pageSize > props.getMaxTasks()) {
            pageSize = props.getMaxTasks();
        }

        if (page < 0) page = 0;
        if (pageSize < 1) pageSize = props.getDefaultPageSize();

        return taskRepository.findAll(page, pageSize);
    }

    public List<Task> getCompletedTasks(boolean completed) {
        return taskRepository.findCompletedTask(completed);
    }

    public Task getTask(Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new TaskNotFoundException(String.format("Task with ID %s doesn't exist", id));
        }
        return taskRepository.findById(id);
    }

    @Transactional
    public Task updateTask(Long id, TaskDto taskDto) {
        isExisted(id);
        return taskRepository.updateTask(id, taskDto);
    }

    public Task markTaskAsCompleted(Long id) {
        isExisted(id);
        return taskRepository.markTaskAsCompleted(id);
    }

    public void deleteTask(Long id) {
        isExisted(id);
        taskRepository.deleteTask(id);
    }

    private void isExisted(Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new TaskNotFoundException(String.format("Task with ID %s doesn't exist", id));
        }
    }
}
