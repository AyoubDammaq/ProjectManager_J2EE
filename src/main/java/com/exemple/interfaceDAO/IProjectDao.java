package com.exemple.interfaceDAO;

import java.sql.SQLException;
import java.util.*; 
import com.exemple.entity.*;

public interface IProjectDao {
    List<Project> getAllProjects() throws SQLException;
    Project getProjectByCode(String code) throws SQLException;
    boolean addProject(Project project) throws SQLException;
    boolean updateProject(Project project) throws SQLException;
    boolean deleteProject(String projectId) throws SQLException;
}

