package com.studyplanner.models;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import com.studyplanner.models.*;

// ===================== REVISION ALGORITHMS =====================
class LeitnerRevisionAlgorithm implements RevisionAlgorithm {
    @Override
    public LocalDate calculateNextRevisionDate(LocalDate lastRevision, int stage) {
        int daysToAdd = (int) Math.pow(2, Math.min(stage - 1, 5));
        return lastRevision.plusDays(daysToAdd);
    }
}

class SuperMemoRevisionAlgorithm implements RevisionAlgorithm {
    @Override
    public LocalDate calculateNextRevisionDate(LocalDate lastRevision, int stage) {
        int[] intervals = {1, 6, 16, 35, 62, 90, 180, 365};
        int daysToAdd = intervals[Math.min(stage - 1, intervals.length - 1)];
        return lastRevision.plusDays(daysToAdd);
    }
}

// ===================== FACTORY PATTERN =====================
class SessionFactory {
    public static Session createSession(String type, int taskId, LocalDateTime startTime, LocalDateTime endTime, String location) {
        switch (type.toUpperCase()) {
            case "STUDY":
                return new StudySession(taskId, startTime, endTime, location);
            default:
                return new StudySession(taskId, startTime, endTime, location);
        }
    }
    
    public static StudyTask createTask(String type, String title, String description, Difficulty difficulty, double estimatedHours, String... params) {
        switch (type.toUpperCase()) {
            case "TOPIC":
                String subject = params.length > 0 ? params[0] : "General";
                return new Topic(title, description, difficulty, estimatedHours, subject);
            case "ASSIGNMENT":
                LocalDate dueDate = params.length > 0 ? LocalDate.parse(params[0]) : LocalDate.now().plusWeeks(1);
                return new Assignment(title, description, difficulty, estimatedHours, dueDate);
            default:
                return new Topic(title, description, difficulty, estimatedHours, "General");
        }
    }
}

// ===================== DECORATOR PATTERN =====================
class SessionDecorator extends Session {
    protected Session decoratedSession;
    
    public SessionDecorator(Session session) {
        super(session.getTaskId(), session.getStartTime(), session.getEndTime(), session.getLocation());
        this.decoratedSession = session;
    }
    
    @Override
    public String exportToJSON() {
        return decoratedSession.exportToJSON();
    }
}

class UrgentSessionDecorator extends SessionDecorator {
    private boolean isUrgent;
    
    public UrgentSessionDecorator(Session session, boolean isUrgent) {
        super(session);
        this.isUrgent = isUrgent;
    }
    
    @Override
    public String exportToJSON() {
        String baseJson = super.exportToJSON();
        return baseJson.substring(0, baseJson.length() - 1) + ",\"urgent\":" + isUrgent + "}";
    }
    
    public boolean isUrgent() { return isUrgent; }
}

// ===================== CHAIN OF RESPONSIBILITY PATTERN =====================
abstract class ValidationChain {
    protected ValidationChain nextValidator;
    
    public void setNext(ValidationChain next) {
        this.nextValidator = next;
    }
    
    public abstract boolean validate(Object data);
}

class TaskValidationChain extends ValidationChain {
    @Override
    public boolean validate(Object data) {
        if (data instanceof StudyTask) {
            StudyTask task = (StudyTask) data;
            if (!task.validate()) {
                System.out.println("Task validation failed: " + task.getValidationErrors());
                return false;
            }
        }
        if (nextValidator != null) {
            return nextValidator.validate(data);
        }
        return true;
    }
}

class SessionValidationChain extends ValidationChain {
    @Override
    public boolean validate(Object data) {
        if (data instanceof Session) {
            Session session = (Session) data;
            if (!session.validate()) {
                System.out.println("Session validation failed: " + session.getValidationErrors());
                return false;
            }
        }
        if (nextValidator != null) {
            return nextValidator.validate(data);
        }
        return true;
    }
}

// ===================== TEMPLATE METHOD PATTERN =====================
abstract class PlanExporter {
    public final String export(List<? extends Exportable> items, String format) {
        StringBuilder result = new StringBuilder();
        result.append(getHeader(format));
        for (Exportable item : items) {
            result.append(formatItem(item, format));
            result.append(getItemSeparator(format));
        }
        result.append(getFooter(format));
        return result.toString();
    }
    protected abstract String getHeader(String format);
    protected abstract String formatItem(Exportable item, String format);
    protected abstract String getItemSeparator(String format);
    protected abstract String getFooter(String format);
}

class StudyPlanExporter extends PlanExporter {
    @Override
    protected String getHeader(String format) {
        if ("CSV".equalsIgnoreCase(format)) {
            return "ID,Title,Description,Difficulty,EstimatedHours,Status,CreatedDate,Tags\n";
        } else if ("JSON".equalsIgnoreCase(format)) {
            return "{\n  \"tasks\": [\n";
        }
        return "";
    }
    @Override
    protected String formatItem(Exportable item, String format) {
        if ("CSV".equalsIgnoreCase(format)) {
            return item.exportToCSV();
        } else if ("JSON".equalsIgnoreCase(format)) {
            return "    " + item.exportToJSON();
        }
        return item.toString();
    }
    @Override
    protected String getItemSeparator(String format) {
        if ("CSV".equalsIgnoreCase(format)) {
            return "\n";
        } else if ("JSON".equalsIgnoreCase(format)) {
            return ",\n";
        }
        return "\n";
    }
    @Override
    protected String getFooter(String format) {
        if ("JSON".equalsIgnoreCase(format)) {
            return "\n  ]\n}";
        }
        return "";
    }
}

// ===================== ANALYTICS ENGINE =====================
class AnalyticsEngine {
    public Map<String, Object> generateProgressReport(List<StudyTask> tasks, List<Session> sessions) {
        Map<String, Object> report = new HashMap<>();
        long completedTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        long totalTasks = tasks.size();
        double completionRate = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;
        report.put("totalTasks", totalTasks);
        report.put("completedTasks", completedTasks);
        report.put("completionRate", String.format("%.1f%%", completionRate));

        Map<Difficulty, Long> difficultyCount = tasks.stream()
            .collect(Collectors.groupingBy(StudyTask::getDifficulty, Collectors.counting()));
        report.put("difficultyBreakdown", difficultyCount);

        double totalEstimatedHours = tasks.stream().mapToDouble(StudyTask::getEstimatedHours).sum();
        double completedHours = tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.DONE)
            .mapToDouble(StudyTask::getEstimatedHours)
            .sum();
        report.put("totalEstimatedHours", StringUtils.formatHours(totalEstimatedHours));
        report.put("completedHours", StringUtils.formatHours(completedHours));

        long completedSessions = sessions.stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();
        report.put("totalSessions", sessions.size());
        report.put("completedSessions", completedSessions);
        return report;
    }
    
    public String generateProductivityHeatmap(List<Session> sessions) {
        StringBuilder heatmap = new StringBuilder();
        heatmap.append("\nPRODUCTIVITY HEATMAP (Last 7 Days)\n");
        heatmap.append("=".repeat(50)).append("\n");
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            long sessionCount = sessions.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .count();
            String dateStr = date.format(DateTimeFormatter.ofPattern("MMM dd"));
            String bar = "█".repeat((int) Math.min(sessionCount, 10));
            String spaces = " ".repeat(Math.max(0, 10 - (int) sessionCount));
            heatmap.append(String.format("%s |%s%s| %d sessions\n", dateStr, bar, spaces, sessionCount));
        }
        return heatmap.toString();
    }
    
    public List<StudyTask> getRecommendedRevisions(List<StudyTask> tasks, List<RevisionPlan> revisionPlans) {
        return tasks.stream()
            .filter(task -> revisionPlans.stream()
                .anyMatch(plan -> plan.getTaskId() == task.getId() && !plan.getNextRevisionDate().isAfter(LocalDate.now())))
            .sorted((a, b) -> Integer.compare(a.getDifficulty().getLevel(), b.getDifficulty().getLevel()))
            .collect(Collectors.toList());
    }
}

// ===================== CONFLICT DETECTOR =====================
class ConflictDetector implements ConflictResolvable {
    private List<Session> sessions;
    private List<Resource> resources;
    private List<GroupMember> members;
    
    public ConflictDetector(List<Session> sessions, List<Resource> resources, List<GroupMember> members) {
        this.sessions = sessions;
        this.resources = resources;
        this.members = members;
    }
    
    @Override
    public List<String> detectConflicts() {
        List<String> conflicts = new ArrayList<>();
        for (int i = 0; i < sessions.size(); i++) {
            for (int j = i + 1; j < sessions.size(); j++) {
                Session s1 = sessions.get(i);
                Session s2 = sessions.get(j);
                if (sessionsOverlap(s1, s2)) {
                    conflicts.add(String.format("Time conflict between sessions %d and %d", s1.getId(), s2.getId()));
                }
            }
        }
        for (Resource resource : resources) {
            List<Session> resourceSessions = new ArrayList<>(resource.getAssignedSessions());
            for (int i = 0; i < resourceSessions.size(); i++) {
                for (int j = i + 1; j < resourceSessions.size(); j++) {
                    Session s1 = resourceSessions.get(i);
                    Session s2 = resourceSessions.get(j);
                    if (sessionsOverlap(s1, s2)) {
                        conflicts.add(String.format("Resource '%s' double-booked for sessions %d and %d", resource.getName(), s1.getId(), s2.getId()));
                    }
                }
            }
        }
        return conflicts;
    }
    
    private boolean sessionsOverlap(Session s1, Session s2) {
        return s1.getStartTime().isBefore(s2.getEndTime()) && s1.getEndTime().isAfter(s2.getStartTime());
    }
    
    @Override
    public boolean resolveConflict(String conflictId) {
        Logger.getInstance().log("Attempting to resolve conflict: " + conflictId);
        return true;
    }
}

// ===================== UTILITIES =====================
class DateUtils {
    public static boolean isOverdue(LocalDate dueDate) {
        return dueDate.isBefore(LocalDate.now());
    }
    
    public static long daysBetween(LocalDate start, LocalDate end) {
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }
    
    public static List<LocalDate> getDateRange(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }
}

class StringUtils {
    public static boolean matchesPattern(String text, String pattern) {
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text).find();
    }
    
    public static List<String> parseTags(String tagString) {
        if (tagString == null || tagString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(tagString.split("[,;]"))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toList());
    }
    
    public static String formatHours(double hours) {
        int h = (int) hours;
        int m = (int) ((hours - h) * 60);
        return String.format("%dh %dm", h, m);
    }
}

// ===================== SINGLETON CLASSES =====================
class Logger {
    private static Logger instance;
    private List<String> logs;
    
    private Logger() {
        this.logs = new ArrayList<>();
    }
    
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    public void log(String message) {
        String logEntry = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + message;
        logs.add(logEntry);
        System.out.println("LOG: " + logEntry);
    }
    
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
    
    public void clearLogs() {
        logs.clear();
    }
}

class SecurityManager {
    private static SecurityManager instance;
    private String currentUser;
    private String pin;
    private Map<String, Set<String>> permissions;
    
    private SecurityManager() {
        this.permissions = new HashMap<>();
        this.pin = "1234"; // Default PIN
    }
    
    public static SecurityManager getInstance() {
        if (instance == null) {
            instance = new SecurityManager();
        }
        return instance;
    }
    
    public boolean authenticate(String inputPin) {
        return pin.equals(inputPin);
    }
    
    public void setPin(String newPin) {
        this.pin = newPin;
        Logger.getInstance().log("PIN changed successfully");
    }
    
    public boolean hasPermission(String user, String action) {
        return permissions.getOrDefault(user, Collections.emptySet()).contains(action);
    }
    
    public void grantPermission(String user, String action) {
        permissions.computeIfAbsent(user, k -> new HashSet<>()).add(action);
    }
    
    public String getCurrentUser() { return currentUser; }
    public void setCurrentUser(String currentUser) { this.currentUser = currentUser;
    }
    }