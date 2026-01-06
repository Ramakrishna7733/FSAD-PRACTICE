package com.studyplanner.models;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import com.studyplanner.models.*;

// ===================== MAIN PLANNER MANAGER (SINGLETON) =====================
public class PlannerManager implements Subject {
    private static PlannerManager instance;
    private List<StudyTask> tasks;
    private List<Session> sessions;
    private List<RevisionPlan> revisionPlans;
    private List<GroupMember> groupMembers;
    private List<Resource> resources;
    private CompositeGroup mainGroup;
    private RevisionAlgorithm revisionAlgorithm;
    private List<Observer> observers;
    private ConflictDetector conflictDetector;
    private AnalyticsEngine analyticsEngine;
    
    private PlannerManager() {
        this.tasks = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.revisionPlans = new ArrayList<>();
        this.groupMembers = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.mainGroup = new CompositeGroup("Main Study Group");
        this.revisionAlgorithm = new LeitnerRevisionAlgorithm();
        this.observers = new ArrayList<>();
        this.conflictDetector = new ConflictDetector(sessions, resources, groupMembers);
        this.analyticsEngine = new AnalyticsEngine();
    }
    
    public static PlannerManager getInstance() {
        if (instance == null) {
            instance = new PlannerManager();
        }
        return instance;
    }
    
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
    
    public void addTask(StudyTask task) {
        ValidationChain validator = new TaskValidationChain();
        if (validator.validate(task)) {
            tasks.add(task);
            revisionPlans.add(new RevisionPlan(task.getId()));
            notifyObservers("New task added: " + task.getTitle());
            Logger.getInstance().log("Task added: " + task.getTitle());
        }
    }
    
    public List<StudyTask> searchTasks(String query) {
        return tasks.stream()
            .filter(task -> StringUtils.matchesPattern(task.getTitle(), query) || 
                           StringUtils.matchesPattern(task.getDescription(), query))
            .collect(Collectors.toList());
    }
    
    public void addSession(Session session) {
        ValidationChain validator = new SessionValidationChain();
        if (validator.validate(session)) {
            sessions.add(session);
            notifyObservers("New session scheduled");
            Logger.getInstance().log("Session added for task ID: " + session.getTaskId());
        }
    }
    
    public void addGroupMember(GroupMember member) {
        groupMembers.add(member);
        mainGroup.addMember(member);
        notifyObservers("New member added: " + member.getName());
    }
    
    public void addResource(Resource resource) {
        resources.add(resource);
        Logger.getInstance().log("Resource added: " + resource.getName());
    }
    
    public List<StudyTask> getTodaysRevisions() {
        return analyticsEngine.getRecommendedRevisions(tasks, revisionPlans);
    }
    
    public void markRevisionComplete(int taskId) {
        revisionPlans.stream()
            .filter(plan -> plan.getTaskId() == taskId)
            .findFirst()
            .ifPresent(plan -> {
                plan.markRevised(revisionAlgorithm);
                notifyObservers("Revision completed for task ID: " + taskId);
            });
    }
    
    public Map<String, Object> getProgressReport() {
        return analyticsEngine.generateProgressReport(tasks, sessions);
    }
    
    public String getProductivityHeatmap() {
        return analyticsEngine.generateProductivityHeatmap(sessions);
    }
    
    public List<String> detectConflicts() {
        return conflictDetector.detectConflicts();
    }
    
    public String exportData(String format) {
        StudyPlanExporter exporter = new StudyPlanExporter();
        return exporter.export(new ArrayList<>(tasks), format);
    }
    
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            Logger.getInstance().log("Data saved to: " + filename);
        } catch (IOException e) {
            Logger.getInstance().log("Error saving file: " + e.getMessage());
        }
    }
    
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            PlannerManager loaded = (PlannerManager) ois.readObject();
            this.tasks = loaded.tasks;
            this.sessions = loaded.sessions;
            this.revisionPlans = loaded.revisionPlans;
            this.groupMembers = loaded.groupMembers;
            this.resources = loaded.resources;
            Logger.getInstance().log("Data loaded from: " + filename);
        } catch (IOException | ClassNotFoundException e) {
            Logger.getInstance().log("Error loading file: " + e.getMessage());
        }
    }
    
    // Getters
    public List<StudyTask> getTasks() { return new ArrayList<>(tasks); }
    public List<Session> getSessions() { return new ArrayList<>(sessions); }
    public List<GroupMember> getGroupMembers() { return new ArrayList<>(groupMembers); }
    public List<Resource> getResources() { return new ArrayList<>(resources); }
    public CompositeGroup getMainGroup() { return mainGroup; }
    public void setRevisionAlgorithm(RevisionAlgorithm algorithm) { this.revisionAlgorithm = algorithm; }
}