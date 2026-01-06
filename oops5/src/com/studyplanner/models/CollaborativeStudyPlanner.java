package com.studyplanner.models;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.studyplanner.models.*;

// ===================== MAIN CLASS =====================
public class CollaborativeStudyPlanner {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PlannerManager planner = PlannerManager.getInstance();
    private static final Logger logger = Logger.getInstance();
    
    public static void main(String[] args) {
        System.out.println("Welcome to Collaborative Study Planner!");
        loadSampleData();

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getChoice();
            try {
                switch (choice) {
                    case 1: addTopicTask(); break;
                    case 2: scheduleSession(); break;
                    case 3: groupPlanning(); break;
                    case 4: dailyRevision(); break;
                    case 5: trackProgress(); break;
                    case 6: conflictDetection(); break;
                    case 7: revisionAnalytics(); break;
                    case 8: exportData(); break;
                    case 9: importData(); break;
                    case 10: settings(); break;
                    case 0: 
                        System.out.println("Thank you for using Collaborative Study Planner!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                logger.log("Error in main menu: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void loadSampleData() {
        // Add sample group members
        GroupMember alice = new GroupMember("Alice", "alice@email.com", "Leader");
        GroupMember bob = new GroupMember("Bob", "bob@email.com", "Member");
        planner.addGroupMember(alice);
        planner.addGroupMember(bob);
        
        // Add sample resources
        planner.addResource(new Book("Java Programming", "Oracle", "978-0134685991"));
        planner.addResource(new Classroom("Room A101", 30, "Engineering Building"));
        
        // Add sample tasks
        StudyTask mathTask = SessionFactory.createTask("TOPIC", "Calculus Integration", 
            "Learn integration techniques", Difficulty.MEDIUM, 4.0, "Mathematics");
        StudyTask javaTask = SessionFactory.createTask("ASSIGNMENT", "Java Project", 
            "Complete OOP assignment", Difficulty.HARD, 8.0, "2024-12-31");
        mathTask.setAssignedTo(alice);
        javaTask.setAssignedTo(bob);
        planner.addTask(mathTask);
        planner.addTask(javaTask);
        
        System.out.println("Sample data loaded successfully!");
    }

    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       COLLABORATIVE STUDY PLANNER");
        System.out.println("=".repeat(50));
        System.out.println("1.  Add Study Topic/Subject/Task");
        System.out.println("2.  Schedule Sessions & Deadlines");
        System.out.println("3.  Group Planning & Resource Allocation");
        System.out.println("4.  Daily Revision & Spaced Repetition");
        System.out.println("5.  Track Progress & Productivity");
        System.out.println("6.  Conflict Detection & Resolution");
        System.out.println("7.  Revision Analytics & Recommendations");
        System.out.println("8.  Export/Backup Plans & Logs");
        System.out.println("9.  Import/Restore Plans & Logs");
        System.out.println("10. Settings & Access Control");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice: ");
    }
    
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addTopicTask() {
        System.out.println("\nADD NEW STUDY TASK");
        System.out.println("-".repeat(30));
        System.out.print("Task type (TOPIC/ASSIGNMENT): ");
        String type = scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Difficulty (EASY/MEDIUM/HARD/EXPERT): ");
        Difficulty difficulty = Difficulty.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Estimated hours: ");
        double hours = Double.parseDouble(scanner.nextLine());
        System.out.print("Additional parameter (subject/due date): ");
        String param = scanner.nextLine();

        StudyTask task = SessionFactory.createTask(type, title, description, difficulty, hours, param);

        // Assign to group member
        List<GroupMember> members = planner.getGroupMembers();
        if (!members.isEmpty()) {
            System.out.println("Available members:");
            for (int i = 0; i < members.size(); i++) {
                System.out.println((i + 1) + ". " + members.get(i).getName());
            }
            System.out.print("Assign to member (number): ");
            int memberIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (memberIndex >= 0 && memberIndex < members.size()) {
                task.setAssignedTo(members.get(memberIndex));
            }
        }

        planner.addTask(task);
        System.out.println("Task added successfully!");
    }

    private static void scheduleSession() {
        System.out.println("\nSCHEDULE NEW SESSION");
        System.out.println("-".repeat(30));
        List<StudyTask> tasks = planner.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks available. Please add a task first.");
            return;
        }
        
        System.out.println("Available tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            StudyTask task = tasks.get(i);
            System.out.println((i + 1) + ". " + task.getTitle() + " (" + task.getStatus() + ")");
        }
        System.out.print("Select task (number): ");
        int taskIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            System.out.println("Invalid task selection.");
            return;
        }
        
        StudyTask selectedTask = tasks.get(taskIndex);
        System.out.print("Start date and time (YYYY-MM-DD HH:MM): ");
        String startTimeStr = scanner.nextLine();
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.print("Duration in hours: ");
        double duration = Double.parseDouble(scanner.nextLine());
        LocalDateTime endTime = startTime.plusMinutes((long)(duration * 60));
        System.out.print("Location: ");
        String location = scanner.nextLine();
        
        Session session = SessionFactory.createSession("STUDY", selectedTask.getId(), startTime, endTime, location);
        planner.addSession(session);
        System.out.println("Session scheduled successfully!");
    }

    private static void groupPlanning() {
        System.out.println("\nGROUP PLANNING & RESOURCE ALLOCATION");
        System.out.println("-".repeat(40));
        CompositeGroup mainGroup = planner.getMainGroup();
        System.out.println("Group: " + mainGroup.getName());
        System.out.println("Total members: " + mainGroup.getTotalSize());
        
        System.out.println("\nMembers:");
        for (GroupMember member : mainGroup.getAllMembers()) {
            System.out.println("- " + member.getName() + " (" + member.getRole() + ")");
            System.out.println("  Assignments: " + member.getAssignments().size());
            System.out.println("  Average Performance: " + String.format("%.1f", member.getAveragePerformance()));
        }
        
        System.out.println("\nResources:");
        for (Resource resource : planner.getResources()) {
            System.out.println("- " + resource.getName() + " (" + resource.getType() + ")");
            System.out.println("  Available: " + (resource.isAvailable() ? "Yes" : "No"));
        }
    }

    private static void dailyRevision() {
        System.out.println("\nDAILY REVISION & SPACED REPETITION");
        System.out.println("-".repeat(40));
        List<StudyTask> dueRevisions = planner.getTodaysRevisions();
        if (dueRevisions.isEmpty()) {
            System.out.println("No revisions due today! Great job staying on top of your studies.");
            return;
        }
        
        System.out.println("Tasks due for revision today:");
        for (int i = 0; i < dueRevisions.size(); i++) {
            StudyTask task = dueRevisions.get(i);
            System.out.println((i + 1) + ". " + task.getTitle() + " (" + task.getDifficulty() + ")");
        }
        System.out.print("\nMark revision complete for task (number, 0 to skip): ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice > 0 && choice <= dueRevisions.size()) {
            StudyTask task = dueRevisions.get(choice - 1);
            planner.markRevisionComplete(task.getId());
            System.out.println("Revision marked complete for: " + task.getTitle());
        }
    }

    private static void trackProgress() throws IOException {
        System.out.println("\nPROGRESS & PRODUCTIVITY TRACKING");
        System.out.println("-".repeat(40));
        Map<String, Object> report = planner.getProgressReport();
        
        System.out.println("Progress Summary:");
        System.out.println("Total Tasks: " + report.get("totalTasks"));
        System.out.println("Completed Tasks: " + report.get("completedTasks"));
        System.out.println("Completion Rate: " + report.get("completionRate"));
        System.out.println("Total Estimated Hours: " + report.get("totalEstimatedHours"));
        System.out.println("Completed Hours: " + report.get("completedHours"));
        System.out.println("Total Sessions: " + report.get("totalSessions"));
        System.out.println("Completed Sessions: " + report.get("completedSessions"));
        
        System.out.println("\nDifficulty Breakdown:");
        @SuppressWarnings("unchecked")
        Map<Difficulty, Long> difficultyBreakdown = (Map<Difficulty, Long>) report.get("difficultyBreakdown");
        for (Map.Entry<Difficulty, Long> entry : difficultyBreakdown.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " tasks");
        }
        
        System.out.println(planner.getProductivityHeatmap());
    }

    private static void conflictDetection() {
        System.out.println("\nCONFLICT DETECTION & RESOLUTION");
        System.out.println("-".repeat(40));
        List<String> conflicts = planner.detectConflicts();
        if (conflicts.isEmpty()) {
            System.out.println("No conflicts detected! Your schedule looks good.");
        } else {
            System.out.println("Conflicts detected:");
            for (String conflict : conflicts) {
                System.out.println("- " + conflict);
            }
            System.out.println("\nAuto-resolution recommendations:");
            System.out.println("1. Reschedule overlapping sessions");
            System.out.println("2. Reallocate resources");
            System.out.println("3. Split group assignments");
        }
    }

    private static void revisionAnalytics() {
        System.out.println("\nREVISION ANALYTICS & RECOMMENDATIONS");
        System.out.println("-".repeat(40));
        List<StudyTask> tasks = planner.getTasks();
        Map<TaskStatus, Long> statusCount = tasks.stream()
            .collect(Collectors.groupingBy(StudyTask::getStatus, Collectors.counting()));
        
        System.out.println("Task Status Distribution:");
        for (Map.Entry<TaskStatus, Long> entry : statusCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " tasks");
        }
        
        System.out.println("\nRecommendations:");
        long todoCount = statusCount.getOrDefault(TaskStatus.TODO, 0L);
        long inProgressCount = statusCount.getOrDefault(TaskStatus.IN_PROGRESS, 0L);
        if (todoCount > inProgressCount * 2) {
            System.out.println("- Consider starting more tasks to balance your workload");
        }
        
        List<StudyTask> hardTasks = tasks.stream()
            .filter(t -> t.getDifficulty() == Difficulty.HARD || t.getDifficulty() == Difficulty.EXPERT)
            .filter(t -> t.getStatus() == TaskStatus.TODO)
            .collect(Collectors.toList());
        if (!hardTasks.isEmpty()) {
            System.out.println("- Focus on these challenging tasks:");
            hardTasks.forEach(task -> System.out.println("  • " + task.getTitle()));
        }
    }

    private static void exportData() {
        System.out.println("\nEXPORT/BACKUP PLANS & LOGS");
        System.out.println("-".repeat(30));
        System.out.print("Export format (CSV/JSON): ");
        String format = scanner.nextLine().toUpperCase();
        String exportData = planner.exportData(format);
        System.out.print("Save to file (filename): ");
        String filename = scanner.nextLine();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.print(exportData);
            System.out.println("Data exported to: " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }

    private static void importData() {
        System.out.println("\nIMPORT/RESTORE PLANS & LOGS");
        System.out.println("-".repeat(30));
        System.out.print("Import from file (filename): ");
        String filename = scanner.nextLine();
        
        try {
            planner.loadFromFile(filename);
            System.out.println("Data imported successfully from: " + filename);
        } catch (Exception e) {
            System.out.println("Error importing data: " + e.getMessage());
        }
    }

    private static void settings() {
        System.out.println("\nSETTINGS & ACCESS CONTROL");
        System.out.println("-".repeat(30));
        System.out.println("1. Change PIN");
        System.out.println("2. View Logs");
        System.out.println("3. Clear Logs");
        System.out.println("4. Change Revision Algorithm");
        System.out.print("Choose option: ");
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new PIN: ");
                String newPin = scanner.nextLine();
                System.out.println("PIN change skipped (no login required).");
                break;
            case 2:
                System.out.println("\nRecent Logs:");
                List<String> logs = logger.getLogs();
                logs.stream().limit(10).forEach(System.out::println);
                break;
            case 3:
                logger.clearLogs();
                System.out.println("Logs cleared!");
                break;
            case 4:
                System.out.println("1. Leitner System");
                System.out.println("2. SuperMemo Algorithm");
                System.out.print("Choose algorithm: ");
                int algoChoice = getChoice();
                if (algoChoice == 1) {
                    planner.setRevisionAlgorithm(new LeitnerRevisionAlgorithm());
                } else if (algoChoice == 2) {
                    planner.setRevisionAlgorithm(new SuperMemoRevisionAlgorithm());
                }
                System.out.println("Revision algorithm updated!");
                break;
        }
    }
}