package com.exemple.interfaceDAO;

import com.exemple.entity.*;

import java.sql.SQLException;
import java.util.*;

public interface ITaskDao {
	List<Task> getAllTasks() throws SQLException;
	Task getTaskByCode(String code) throws SQLException;
    boolean addTask(Task task) throws SQLException;
    boolean updateTask(Task task) throws SQLException;
    boolean deleteTask(String code) throws SQLException;
}
