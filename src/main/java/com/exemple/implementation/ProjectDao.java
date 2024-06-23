package com.exemple.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.exemple.entity.*;

public class ProjectDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/dbproject";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	private Connection jdbcConnection;

	public ProjectDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	public ProjectDao() {
		super();
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

	public boolean addProject(Project project) throws SQLException {
		String sql = "INSERT INTO project (code, description, startDate) VALUES (?, ?, ?)";
		try {
			connect();

			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, project.getCode());
			statement.setString(2, project.getDescription());
			statement.setString(3, project.getStartDate());

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
			throw e;
		} finally {
			disconnect();
		}
	}

	public Project getProjectByCode(String code) throws SQLException {
		Project project = null;
		String sql = "SELECT * FROM project WHERE code = ?";

		try {
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
		} catch (SQLException e) {
			System.out.println("Error fetching project by code: " + e.getMessage());
			throw e;
		} finally {
			disconnect();
		}

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

	public boolean deleteProject(String code) throws SQLException {
		String sql = "DELETE FROM project WHERE code = ?";

		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, code);

		boolean rowDeleted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowDeleted;
	}

	public boolean updateProject(Project project) throws SQLException {
		String sql = "UPDATE project SET description = ?, startDate = ? WHERE code = ?";
		connect();

		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, project.getDescription());
			statement.setString(2, project.getStartDate());
			statement.setString(3, project.getCode());

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
			String startDate = resultSet.getString("start_date");

			project = new Project(code, description, startDate);
		}

		resultSet.close();
		statement.close();

		disconnect();

		return project;
	}
}
