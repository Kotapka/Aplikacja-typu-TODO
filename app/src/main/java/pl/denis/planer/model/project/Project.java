package pl.denis.planer.model.project;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentSkipListSet;

import pl.denis.planer.model.task.Task;
import pl.denis.planer.model.user.User;

public class Project {
    /**
     * counter of existing Project objects
     */
    private static int projectCount =0;

    /**
     * user to whom this Project belong
     */
    private User user ;

    /**
     * id of this Project
     */
    private int id;

    /**
     * the title of this  Project
     */
    private String title;

    /**
     * description of this Project
     */
    private String description;

    /**
     * information about this Project completion
     */
    private Boolean completed = false;

    /**
     * Sorted collection of uncompleted tasks within this project
     */
    private final Collection<Task> uncompletedTasks = new ConcurrentSkipListSet<Task>();

    /**
     * Collection of completed tasks within this project
     */
    private final Collection<Task> completedTasks = new LinkedHashSet<Task>();

    protected Project(){ }

    /**
     * return new project
     * @param user User who created this project
     * @param title Title of this project
     * @param description description of this project
     * @return new Project object
     */
    public static Project getNewProject(User user , String title ,String description){
        Project project = new Project();
        project.setUser(user);
        project.setTitle(title);
        project.setDescription(description);
        project.id = projectCount;
        projectCount ++;
        return project;
    }

    /**
     * used by the DatabaseHelper to copy project from the  database
     * @param id project id
     * @param user User who created this project
     * @param title Title of this project
     * @param description description of this project
     * @param completed information if project was already completed
     * @return copy of project from the database
     */
    public static Project getExistingProject(int id, User user , String title ,String description,
                                             boolean completed){
        Project project = new Project();
        project.setUser(user);
        project.setTitle(title);
        project.setDescription(description);
        project.setCompleted(completed);
        project.id = id;
        return project;
    }


    public static void setProjectCount(int project_count) {
        Project.projectCount = project_count;
    }

    /**
     * Adds the the task to a project list.
     * This method is called automatically in Task constructor
     * @param task  Newly created task
     */
    public void addTask(Task task){
        uncompletedTasks.add(task);
    }

    /**
     * moves the task to the "completed" list
     * this method does not set the "completed" property in the task
     * @param task  The ask we are moving
     */
    public void completeTask(Task task){
        uncompletedTasks.remove(task);
        completedTasks.add(task);
    }

    /**
     * moves the task to the "uncompleted" list
     * this method does not set the "completed" property in the task
     * @param task  The ask we are moving
     */
    public void retrieveTask(Task task){
        completedTasks.remove(task);
        uncompletedTasks.add(task);
    }

    /**
     * Retrieves the User to whom this project belongs
     * @return User to whom this project belong
     */
    public User getUser() {
        return user;
    }
    private void setUser(User user) {
        this.user = user;
    }

    public int getId(){
        return this.id;
    }

    /**
     * Retrieves title of this project
     * @return title property
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the title of this project
     * @param title a project title
     */
    public void setTitle(String title) {
        this.title=title;
    }

    /**
     * Retrieves the description of this project
     * @return  description property
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description property of this title to the given string
     * @param description description of the project
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves whether escape this project was completed
     * @return true if project was completed; false if it wasn't
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Set te completed flag of project
     * @return true if project was completed; false if it wasn't
     */
    public void setCompleted(Boolean completed) {
        this.completed=completed;
    }

    /**
     * Overridden toString method
     * @return  String representation of project object
     */
    @NonNull
    @Override
    public String toString() {
        String message=getTitle()+"("+getDescription()+")\n";
        message +="UNCOMPLETED TASKS : \n";
        for (Task task:uncompletedTasks) {
            message+= "\t"+String.valueOf(task)+"\n";
        }
        message += "COMPLETED TASKS : \n";
        for (Task task:completedTasks) {
            message+= "\t"+String.valueOf(task)+"\n";
        }
        return message;
    }

    //testowa funkcja do usunięcia
    /*
    public static Project test() {
        Project project = new Project(new User());
        project.setTitle("Projekt testowy");
        project.setDescription("sluzy do sprawdzenia funkcjonalnosci klasy");
        for(int i=0;i<4;i++){
            Task task = new Task(project.getUser(),project);
            task.setTitle("task nr: "+task.getId());
            task.setDescription("task description");
            task.setPriority(4-i);
        }
        for(int i=0;i<4;i++){
            Task task = new Task(project.getUser(),project);
            task.setTitle("task nr: "+task.getId());
            task.setDescription("task description");
            project.completeTask(task);
            task.setPriority(4-i);
        }
        return project;
    }*/

}
