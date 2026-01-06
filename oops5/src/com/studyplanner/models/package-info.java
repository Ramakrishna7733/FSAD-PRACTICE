package com.studyplanner.models;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

// ===================== ENUMS =====================
enum Difficulty {
    EASY(1), MEDIUM(2), HARD(3), EXPERT(4);
    
    private final int level;
    
    Difficulty(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
}

enum TaskStatus {
    TODO, IN_PROGRESS, DONE, REVISED
}

enum SessionStatus {
    SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
}

enum ResourceType {
    BOOK, SLIDES, LAB, CLASSROOM, COMPUTER
}

// ===================== INTERFACES =====================
interface Exportable {
    String exportToCSV();
    String exportToJSON();
}

interface Validatable {
    boolean validate();
    List<String> getValidationErrors();
}

interface RevisionAlgorithm {
    LocalDate calculateNextRevisionDate(LocalDate lastRevision, int stage);
}

interface ConflictResolvable {
    List<String> detectConflicts();
    boolean resolveConflict(String conflictId);
}

interface Observer {
    void update(String message);
}

interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String message);
}

// ===================== ABSTRACT CLASSES =====================
abstract class StudyTask implements Serializable, Exportable, Validatable {
    protected static int idCounter = 1;
    protected final int id;
    protected String title;
    protected String description;
    protected Difficulty difficulty;
    protected double estimatedHours;
    protected Set<StudyTask> dependencies;
    protected GroupMember assignedTo;
    protected TaskStatus status;
    protected LocalDate createdDate;
    protected Set<String> tags;
    
    public StudyTask(String title, String description, Difficulty difficulty, double estimatedHours) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.estimatedHours = estimatedHours;
        this.dependencies = new HashSet<>();
        this.status = TaskStatus.TODO;
        this.createdDate = LocalDate.now();
        this.tags = new HashSet<>();
    }
    
    public boolean hasCircularDependency(StudyTask target, Set<StudyTask> visited) {
        if (this.equals(target)) return true;
        if (visited.contains(this)) return false;
        
        visited.add(this);
        for (StudyTask dep : dependencies) {
            if (dep.hasCircularDependency(target, visited)) {
                return true;
            }
        }
        return false;
    }
    
    public void addDependency(StudyTask dependency) {
        if (!hasCircularDependency(dependency, new HashSet<>())) {
            dependencies.add(dependency);
        } else {
            throw new IllegalArgumentException("Adding this dependency would create a circular reference!");
        }
    }
    
    @Override
    public boolean validate() {
        return title != null && !title.trim().isEmpty() && estimatedHours > 0;
    }
    
    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        if (title == null || title.trim().isEmpty()) {
            errors.add("Title cannot be empty");
        }
        if (estimatedHours <= 0) {
            errors.add("Estimated hours must be positive");
        }
        return errors;
    }
    
    @Override
    public String exportToCSV() {
        return String.format("%d,\"%s\",\"%s\",%s,%.2f,%s,%s,%s",
            id, title, description, difficulty, estimatedHours, 
            status, createdDate, String.join(";", tags));
    }
    
    @Override
    public String exportToJSON() {
        return String.format(
            "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\",\"difficulty\":\"%s\",\"estimatedHours\":%.2f,\"status\":\"%s\",\"createdDate\":\"%s\",\"tags\":[%s]}",
            id, title, description, difficulty, estimatedHours, status, createdDate,
            tags.stream().map(tag -> "\"" + tag + "\"").collect(Collectors.joining(","))
        );
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public double getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(double estimatedHours) { this.estimatedHours = estimatedHours; }
    public Set<StudyTask> getDependencies() { return dependencies; }
    public GroupMember getAssignedTo() { return assignedTo; }
    public void setAssignedTo(GroupMember assignedTo) { this.assignedTo = assignedTo; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public LocalDate getCreatedDate() { return createdDate; }
    public Set<String> getTags() { return tags; }
    public void addTag(String tag) { this.tags.add(tag); }
}

abstract class Session implements Serializable, Exportable, Validatable {
    protected static int idCounter = 1;
    protected final int id;
    protected int taskId;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected String recurrence;
    protected String location;
    protected Set<GroupMember> assignedGroup;
    protected SessionStatus status;
    protected Set<Resource> resources;
    
    public Session(int taskId, LocalDateTime startTime, LocalDateTime endTime, String location) {
        this.id = idCounter++;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.assignedGroup = new HashSet<>();
        this.status = SessionStatus.SCHEDULED;
        this.resources = new HashSet<>();
    }
    
    @Override
    public boolean validate() {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }
    
    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        if (startTime == null) errors.add("Start time cannot be null");
        if (endTime == null) errors.add("End time cannot be null");
        if (startTime != null && endTime != null && !startTime.isBefore(endTime)) {
            errors.add("Start time must be before end time");
        }
        return errors;
    }
    
    @Override
    public String exportToCSV() {
        return String.format("%d,%d,\"%s\",\"%s\",\"%s\",\"%s\",%s",
            id, taskId, startTime, endTime, location, recurrence, status);
    }
    
    @Override
    public String exportToJSON() {
        return String.format(
            "{\"id\":%d,\"taskId\":%d,\"startTime\":\"%s\",\"endTime\":\"%s\",\"location\":\"%s\",\"status\":\"%s\"}",
            id, taskId, startTime, endTime, location, status);
    }
    
    // Getters and setters
    public int getId() { return id; }
    public int getTaskId() { return taskId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getRecurrence() { return recurrence; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Set<GroupMember> getAssignedGroup() { return assignedGroup; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    public Set<Resource> getResources() { return resources; }
    public void addResource(Resource resource) { this.resources.add(resource); }
}

abstract class Resource implements Serializable {
    protected static int idCounter = 1;
    protected final int id;
    protected String name;
    protected ResourceType type;
    protected boolean available;
    protected Set<Session> assignedSessions;
    
    public Resource(String name, ResourceType type) {
        this.id = idCounter++;
        this.name = name;
        this.type = type;
        this.available = true;
        this.assignedSessions = new HashSet<>();
    }
    
    public boolean isAvailableAt(LocalDateTime startTime, LocalDateTime endTime) {
        for (Session session : assignedSessions) {
            if (session.getStartTime().isBefore(endTime) && session.getEndTime().isAfter(startTime)) {
                return false;
            }
        }
        return available;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ResourceType getType() { return type; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Set<Session> getAssignedSessions() { return assignedSessions; }
    public void addSession(Session session) { this.assignedSessions.add(session); }
}

// ===================== CONCRETE CLASSES =====================
class Topic extends StudyTask {
    private String subject;
    private int priority;
    
    public Topic(String title, String description, Difficulty difficulty, double estimatedHours, String subject) {
        super(title, description, difficulty, estimatedHours);
        this.subject = subject;
        this.priority = 1;
    }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}

class Assignment extends StudyTask {
    private LocalDate dueDate;
    private double maxScore;
    
    public Assignment(String title, String description, Difficulty difficulty, double estimatedHours, LocalDate dueDate) {
        super(title, description, difficulty, estimatedHours);
        this.dueDate = dueDate;
        this.maxScore = 100.0;
    }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }
}

class StudySession extends Session {
    private String sessionType;
    
    public StudySession(int taskId, LocalDateTime startTime, LocalDateTime endTime, String location) {
        super(taskId, startTime, endTime, location);
        this.sessionType = "STUDY";
    }
    
    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
}

class Book extends Resource {
    private String author;
    private String isbn;
    
    public Book(String name, String author, String isbn) {
        super(name, ResourceType.BOOK);
        this.author = author;
        this.isbn = isbn;
    }
    
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
}

class Classroom extends Resource {
    private int capacity;
    private String building;
    
    public Classroom(String name, int capacity, String building) {
        super(name, ResourceType.CLASSROOM);
        this.capacity = capacity;
        this.building = building;
    }
    
    public int getCapacity() { return capacity; }
    public String getBuilding() { return building; }
}

class GroupMember implements Serializable {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private String email;
    private String role;
    private Set<StudyTask> assignments;
    private Map<String, Double> performance;
    
    public GroupMember(String name, String email, String role) {
        this.id = idCounter++;
        this.name = name;
        this.email = email;
        this.role = role;
        this.assignments = new HashSet<>();
        this.performance = new HashMap<>();
    }
    
    public void addAssignment(StudyTask task) {
        assignments.add(task);
        task.setAssignedTo(this);
    }
    
    public double getAveragePerformance() {
        return performance.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Set<StudyTask> getAssignments() { return assignments; }
    public Map<String, Double> getPerformance() { return performance; }
    public void addPerformance(String metric, double value) { this.performance.put(metric, value); }
}

class RevisionPlan implements Serializable {
    private int taskId;
    private LocalDate nextRevisionDate;
    private int stage;
    private LocalDate lastRevisionDate;
    private int completedRevisions;
    
    public RevisionPlan(int taskId) {
        this.taskId = taskId;
        this.stage = 1;
        this.completedRevisions = 0;
        this.nextRevisionDate = LocalDate.now().plusDays(1);
    }
    
    public void markRevised(RevisionAlgorithm algorithm) {
        this.lastRevisionDate = LocalDate.now();
        this.completedRevisions++;
        this.stage++;
        this.nextRevisionDate = algorithm.calculateNextRevisionDate(lastRevisionDate, stage);
    }
    
    // Getters and setters
    public int getTaskId() { return taskId; }
    public LocalDate getNextRevisionDate() { return nextRevisionDate; }
    public void setNextRevisionDate(LocalDate nextRevisionDate) { this.nextRevisionDate = nextRevisionDate; }
    public int getStage() { return stage; }
    public void setStage(int stage) { this.stage = stage; }
    public LocalDate getLastRevisionDate() { return lastRevisionDate; }
    public int getCompletedRevisions() { return completedRevisions; }
}

class CompositeGroup implements Serializable {
    private String name;
    private List<GroupMember> members;
    private List<CompositeGroup> subGroups;
    private GroupMember leader;
    
    public CompositeGroup(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.subGroups = new ArrayList<>();
    }
    
    public void addMember(GroupMember member) {
        members.add(member);
        if (leader == null) {
            leader = member;
        }
    }
    
    public void addSubGroup(CompositeGroup group) {
        subGroups.add(group);
    }
    
    public List<GroupMember> getAllMembers() {
        List<GroupMember> allMembers = new ArrayList<>(members);
        for (CompositeGroup subGroup : subGroups) {
            allMembers.addAll(subGroup.getAllMembers());
        }
        return allMembers;
    }
    
    public int getTotalSize() {
        int size = members.size();
        for (CompositeGroup subGroup : subGroups) {
            size += subGroup.getTotalSize();
        }
        return size;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public List<GroupMember> getMembers() { return members; }
    public List<CompositeGroup> getSubGroups() { return subGroups; }
    public GroupMember getLeader() { return leader; }
    public void setLeader(GroupMember leader) { this.leader = leader; }
}