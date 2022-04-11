package pl.denis.planer.model.task;

import androidx.annotation.NonNull;

import pl.denis.planer.model.user.User;
import pl.denis.planer.model.project.Project;

public class Task implements Comparable{
    /**
     * counter of existing task objects
     */
    private static int taskCount =0;

    /**
     * project in which this task is
     */
    private Project project ;

    /**
     * user to whom this task belong
     */
    private User user ;

    /**
     * id of this task
     */
    private int id;

    /**
     * the title of this  task
     */
    private String title;

    /**
     * description of this task
     */
    private String description;

    /**
     * information about this task completion
     */
    private Boolean completed = false;

    /**
     * Priority of this task
     */
    private int priority=0;

    /**
     * Constructor creating Task objects
     */

    private Task(){}
    /**
     * creates new task
     * @param project Project to with this task belongs
     * @param user User who created this task
     * @param title Title of this task
     * @param description description of this task
     * @param priority priority of this task
     * @return new task object
     */
    public static Task getNewTask(Project project, User user , String title ,String description,
    int priority){
        Task task = new Task();
        task.setUser(user);
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setProject(project);
        task.id = taskCount;
        taskCount ++;
        project.addTask(task);
        return task;
    }

    /**
     * used by the DatabaseHelper to copy task from the  database
     * @param id task id
     * @param project Project to with this task belongs
     * @param user User who created this task
     * @param title Title of this task
     * @param description description of this task
     * @param priority priority of this task
     * @param completed information if task was already completed
     * @return copy of task from the database
     */
    public static Task getExistingTask(int id ,Project project, User user , String title ,
                                       String description,int priority , boolean completed){
        Task task = new Task();
        task.setUser(user);
        task.setProject(project);
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setCompleted(completed);
        task.id = id;
        project.addTask(task);
        if(completed){
            project.completeTask(task);
        }
        return task;
    }


    public static void setTaskCount(int task_count) {
        Task.taskCount = task_count;
    }

    /**
     * Retrieves title of this task
     * @return title property
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the title of this task
     * @param title a task title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the description of this task
     * @return  description property
     */
    public String getDescription(){
        return description;
    }

    /**
     * Set the description property of this title to the given string
     * @param description description of the task
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Retrieves whether escape this task was completed
     * @return true if task was completed; false if it wasn't
     */
    public Boolean getCompleted() {
        return this.completed;
    }

    /**
     * Set te completed flag of task
     * @return true if task was completed; false if it wasn't
     */
    public void setCompleted(Boolean completed) {
        if (completed!=this.completed){
            if(completed){
                getProject().completeTask(this);
            }
            else{
                getProject().retrieveTask(this);
            }
            this.completed = completed;
        }
    }

    /**
     * Retrieves the User to whom this task belongs
     * @return user to whom this task belong
     */
    public User getUser() {
        return user;
    }

    private void setUser(User user){this.user = user;}

    /**
     * Retrieves the Project in witch this task is
     * @return  project in with tis task is
     */
    public Project getProject() {
        return project;
    }

    private void setProject(Project project){this.project = project;}

    /**
     * Retrieves the id of this task
     * @return  Unique id number
     */
    public int getId() {
        return id;
    }
    /**
     * Retrieves the priority of this task
     * @return  natural number. The higher the priority, the more important the task
     */
    public int getPriority() {
        return priority;
    }
    /**
     * Sets the priority of this task
     * @return  Natural number
     */
    public void setPriority(int priority) {
        if (priority >= 0) {
            this.priority = priority;
        }
        else{
            throw new IllegalArgumentException("priority must be  the natural number");
        }
    }

    /**
     * Compares this task with the specified task by priority
     * @param task  specified task to compare
     * @return  a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Object task) {
        int compareValue= this.priority - ((Task)task).getPriority();
        if(compareValue!=0){
            return compareValue;
        }
        else{
            return this.id - ((Task)task).getId();
        }
    }

    /**
     * Overridden toString method
     * @return  String representation of task object
     */
    @NonNull
    @Override
    public String toString() {
        return getTitle()+"(" +getDescription() +") priority = "+getPriority();
    }

}
