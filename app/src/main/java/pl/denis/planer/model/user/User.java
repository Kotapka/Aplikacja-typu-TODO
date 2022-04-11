package pl.denis.planer.model.user;

import java.util.ArrayList;
import java.util.List;

import pl.denis.planer.exceptions.EmailAlreadyUsedException;
import pl.denis.planer.exceptions.LoginAlreadyUsedException;
import pl.denis.planer.exceptions.WrongEmailAddressException;
import pl.denis.planer.exceptions.WrongLoginException;
import pl.denis.planer.exceptions.WrongPasswordException;
import pl.denis.planer.model.project.Project;

public class User {
    /**
     * counter of existing user objects
     */
    private static int userCount =0;
    protected List<Project> projects = new ArrayList<>();
    protected int id;
    protected String emailAddress;
    protected String login;
    protected String password;
    protected User(){}

    public static void setUserCount(int userCount) {
        User.userCount = userCount;
    }

    public void addProject(Project project){
        projects.add(project);
    }

    public void removeProject(Project project){
        projects.remove(project);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id){
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String eMailAddress) throws WrongEmailAddressException, EmailAlreadyUsedException {
        if(UserDataValidator.checkIfEmailAddressIsValid(eMailAddress)){
            this.emailAddress = eMailAddress;
            return;
        }
        throw new WrongEmailAddressException("forbidden characters in email");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) throws WrongLoginException, LoginAlreadyUsedException {
        if(UserDataValidator.checkIfLoginIsValid(login)){
            this.login = login;
            return;
        }
        throw new WrongLoginException("You are using forbidden characters");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws WrongPasswordException {
        if(UserDataValidator.checkIfPasswordIsValid(password)){
            this.password = password;
            return;
        }
        throw new WrongPasswordException("You are using forbidden characters");
    }

}
