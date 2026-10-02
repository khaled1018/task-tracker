package org.nti.tasktracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tasktracker")
@Setter
@Getter
public class TaskTrackerProperties {

    private int maxTasks = 6;
    private int defaultPageSize = 2;
}
