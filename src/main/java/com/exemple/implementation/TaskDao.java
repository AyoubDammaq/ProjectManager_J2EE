package com.exemple.implementation;

import com.exemple.entity.*;
import com.exemple.interfaceDAO.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class TaskDao implements ITaskDao {

	private String jdbcURL = "jdbc:mysql://localhost:3306/dbproject";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	private Connection jdbcConnection;

	public TaskDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = "jdbc:mysql://localhost:3306/dbproject";
		this.jdbcUsername = "root";
		this.jdbcPassword = "root";
	}

	public TaskDao() {

	}

	protected void connect() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			System.out.println("Database is connected");
		} catch (ClassNotFoundException e) {
			throw new SQLException("JDBC Driver not found", e);
		}
	}

	protected void disconnect() throws SQLException {
		try {
			if (jdbcConnection != null && !jdbcConnection.isClosed()) {
				jdbcConnection.close();
				System.out.println("Database connection is closed");
			}
		} finally {
		}
	}

	public boolean addTask(Task task) throws SQLException {
		String sql = "INSERT INTO tasks (code, description, startDate, endDate, projectCode) VALUES (?, ?, ?, ?, ?)";

		try {
			connect();

			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, task.getCode());
			statement.setString(2, task.getDescription());
			statement.setString(3, task.getStartDate());
			statement.setString(4, task.getEndDate());

			if (task.getProject() != null) {
				statement.setString(5, task.getProject().getCode());
			} else {
				statement.setNull(5, java.sql.Types.NULL);
			}

			boolean rowInserted = statement.executeUpdate() > 0;

			if (rowInserted) {
				System.out.println("Row successfully inserted!");
			} else {
				System.out.println("No rows were inserted.");
			}

			statement.close();
			return rowInserted;

		} catch (SQLException e) {
			System.out.println("Error during data insertion: " + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			disconnect();
		}
	}

	public Task getTaskByCode(String code) throws SQLException {
		Task task = null;
		String sql = "SELECT * FROM tasks WHERE code = ?";

		try {
			connect();

			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, code);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String description = resultSet.getString("description");
				String startDate = resultSet.getString("startDate");
				String endDate = resultSet.getString("endDate");
				String associedProjectCode = resultSet.getString("projectCode");
				Project associedProject = getProjectById(associedProjectCode);
				task = new Task(code, description, startDate, endDate, associedProject);
			}

			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Error fetching project by code: " + e.getMessage());
			throw e;
		} finally {
			disconnect();
		}

		return task;
	}

	public List<Task> getAllTasks() throws SQLException {
		List<Task> tasks = new ArrayList<>();
		String sql = "SELECT * FROM tasks";
		try {
			connect();
			Statement statement = jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String code = resultSet.getString("code");
				String description = resultSet.getString("description");
				String startDate = resultSet.getString("startDate");
				String endDate = resultSet.getString("endDate");
				String associedProjectCode = resultSet.getString("projectCode");
				Project associedProject = getProjectById(associedProjectCode);
				Task task = new Task(code, description, startDate, endDate, associedProject);
				tasks.add(task);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Error fetching tasks: " + e.getMessage());
			throw e;
		} finally {
			disconnect();
		}
		return tasks;
	}

	public boolean deleteTask(String code) throws SQLException {
		String sql = "DELETE FROM tasks WHERE code = ?";

		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, code);

		boolean rowDeleted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowDeleted;
	}

	public boolean updateTask(Task task) throws SQLException {
		String sql = "UPDATE tasks SET description = ?, startDate = ? , endDate = ?, projectCode = ? WHERE code = ?";
		connect();

		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, task.getDescription());
			statement.setString(2, task.getStartDate());
			statement.setString(3, task.getEndDate());

			if (task.getProject() != null) {
				statement.setString(4, task.getProject().getCode());
			} else {
				statement.setNull(4, java.sql.Types.NULL);
			}

			statement.setString(5, task.getCode());

			boolean rowUpdated = statement.executeUpdate() > 0;

			if (rowUpdated) {
				System.out.println("Row successfully updated!");
			} else {
				System.out.println("No rows were updated.");
			}

			statement.close();
			jdbcConnection.commit();
			return rowUpdated;

		} catch (SQLException e) {
			System.out.println("Error during data update: " + e.getMessage());
			throw e;
		} finally {
			disconnect();
		}
	}

	public Project getProjectById(String code) throws SQLException {
		Project project = null;
		String sql = "SELECT * FROM project WHERE code = ?";

		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, code);

		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			String description = resultSet.getString("description");
			String startDate = resultSet.getString("startDate");

			project = new Project(code, description, startDate);
		}

		resultSet.close();
		statement.close();

		disconnect();

		return project;
	}

	public List<Project> getAllProjects() throws SQLException {
		List<Project> projects = new ArrayList<>();
		String sql = "SELECT * FROM project";
		try {
			connect();
			Statement statement = jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String code = resultSet.getString("code");
				String description = resultSet.getString("description");
				String startDate = resultSet.getString("startDate");

				Project project = new Project(code, description, startDate);
				projects.add(project);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Error fetching projects: " + e.getMessage());
			throw e;
		} finally {
			disconnect();
		}
		return projects;
	}
}