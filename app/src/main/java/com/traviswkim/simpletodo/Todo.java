package com.traviswkim.simpletodo;

import com.orm.SugarRecord;

/**
 * Created by traviswkim on 7/3/16.
 */

public class Todo extends SugarRecord{
    String taskName;
    String dueDate;
    String priority;

    public Todo(){
    }
    public Todo(String taskName, String dueDate, String priority) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.priority = priority;
    }
}
