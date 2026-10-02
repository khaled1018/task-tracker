package org.nti.tasktracker.service;

import jakarta.transaction.Transactional;
import org.nti.tasktracker.config.TaskTrackerProperties;
import org.nti.tasktracker.dto.TaskDto;
import org.nti.tasktracker.entity.Task;
import org.nti.tasktracker.exceptions.MaxTasksExceededException;
import org.nti.tasktracker.exceptions.TaskNotFoundException;
import org.nti.tasktracker.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final TaskTrackerProperties props;

    public TaskService(TaskRepository taskRepository, TaskTrackerProperties props) {
        this.taskRepository = taskRepository;
        this.props = props;
    }

    @Transactional
    public Task createTask(TaskDto taskDto) {
        if(taskRepository.countTasks() == props.getMaxTasks()) {
            throw new MaxTasksExceededException("Cannot create task: maximum of "+ props.getMaxTasks() +" tasks reached.");
        }
        Task task = new Task();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setDueDate(taskDto.dueDate());
        log.info("Created task \"{}\"", task.getTitle());
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
        isExisted(id);
        return taskRepository.findById(id);
    }

    @Transactional
    public Task updateTask(Long id, TaskDto taskDto) {
        isExisted(id);
        return taskRepository.updateTask(id, taskDto);
    }

    @Transactional
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
