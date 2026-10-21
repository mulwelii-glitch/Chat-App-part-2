package chatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class MainApp {
    private static ArrayList<Message> runtimeDatabase = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login chatLoginSystem = new Login(); 
        boolean mainApplicationRunning = true;

        System.out.println("==================================================");
        System.out.println("         WELCOME TO THE CHAT APPLICATION          ");
        System.out.println("==================================================");
        System.out.print("Press ENTER to launch account registration... ");
        scanner.nextLine();

        // ------------------ PART 1: REGISTRATION ------------------
        System.out.println("\n--- ACCOUNT REGISTRATION ---");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Choose Username (Must contain '_' and be <= 5 chars): ");
        String username = scanner.nextLine();
        System.out.print("Set Password (Min 8 chars, 1 Uppercase, 1 Digit, 1 Special): ");
        String password = scanner.nextLine();
        System.out.print("Enter Cell Phone Number: ");
        String cellNo = scanner.nextLine();

        String registerResponse = chatLoginSystem.registerUser(username, password, firstName, lastName, cellNo);
        System.out.println("\n[SYSTEM RESPONSE]:\n" + registerResponse);

        if (registerResponse.contains("incorrectly formatted") || registerResponse.contains("not correctly formatted")) {
            System.out.println("\n[FATAL ERROR]: Registration parameters invalid. Exiting application.");
            scanner.close();
            return;
        }

        // ------------------ PART 1: LOGIN ------------------
        System.out.println("\n--- USER LOGIN AUTHORIZATION ---");
        System.out.print("Username: ");
        String inputtedUser = scanner.nextLine();
        System.out.print("Password: ");
        String inputtedPass = scanner.nextLine();

        boolean isAuthorized = chatLoginSystem.loginUser(inputtedUser, inputtedPass);
        String loginMessage = chatLoginSystem.returnLoginStatus(isAuthorized);
        System.out.println("\n[SYSTEM RESPONSE]: " + loginMessage);

        if (!isAuthorized) {
            System.out.println("\n[ACCESS DENIED]: Access blocked due to invalid login credentials.");
            scanner.close();
            return;
        }

        // ------------------ PART 2: SYSTEM MENUS ------------------
        System.out.println("\nWelcome to QuickChat.");

        while (mainApplicationRunning) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Select menu option (1-3): ");

            String entrySelection = scanner.nextLine();

            switch (entrySelection) {
                case "1":
                    System.out.print("How many messages do you wish to enter? ");
                    int processingBatchSize;
                    try {
                        processingBatchSize = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR]: Invalid format. Defaulting to 1.");
                        processingBatchSize = 1;
                    }

                    for (int i = 0; i < processingBatchSize; i++) {
                        int activeMessageCount = runtimeDatabase.size() + 1;
                        System.out.println("\nEntering details for Message Execution Check #" + activeMessageCount);

                        System.out.print("Enter Recipient Number: ");
                        String recipientCell = scanner.nextLine();

                        System.out.println("Please enter a message of less than 250 characters.");
                        System.out.print("Enter Message Content: ");
                        String contentPayload = scanner.nextLine();

                        Message temporaryMessageObject = new Message(activeMessageCount, recipientCell, contentPayload);

                        System.out.println("Select Delivery Processing Action Option:");
                        System.out.println(" 1 - Send Message\n 2 - Disregard Message\n 3 - Store Message");
                        System.out.print("Choice: ");
                        
                        int operationalActionChoice;
                        try {
                            operationalActionChoice = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            operationalActionChoice = 1;
                        }

                        System.out.println("\n[CELL CHECKS]: " + temporaryMessageObject.checkRecipientCell());
                        
                        String deliveryFeedback = temporaryMessageObject.SentMessage(operationalActionChoice);
                        System.out.println("[STATUS ALERTS]: " + deliveryFeedback);

                        if (!deliveryFeedback.contains("Message exceeds 250 characters")) {
                            System.out.println("\n--- DISPLAYING FULL DISPATCH DATA METADATA ---");
                            System.out.println("Message ID: Message ID generated: " + temporaryMessageObject.getMessageId());
                            System.out.println("Message Hash: " + temporaryMessageObject.getMessageHash());
                            System.out.println("Recipient: " + temporaryMessageObject.getRecipientNumber());
                            System.out.println("Message: " + temporaryMessageObject.getMessageText());
                            System.out.println("------------------------------------------------");
                            runtimeDatabase.add(temporaryMessageObject);
                        } else {
                            System.out.println("[REJECTED]: Message was not saved due to structural length overflow.");
                        }
                    }

                    System.out.println("\n[LOG SUMMARY]: Total number of messages accumulated: " + returnTotalMessagess());
                    executeExportDatabaseFile();
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Exiting application workspace. Goodbye.");
                    mainApplicationRunning = false;
                    break;

                default:
                    System.out.println("[ALERT]: Invalid menu selection. Use keys 1-3.");
                    break;
            }
        }
        scanner.close();
    }

    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : runtimeDatabase) {
            sb.append(m.storeMessage()).append("\n,\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() {
        return runtimeDatabase.size();
    }

    private static void executeExportDatabaseFile() {
        String dbOutputFileNamePath = "messages.json";
        try (FileWriter filePipelineStream = new FileWriter(dbOutputFileNamePath)) {
            filePipelineStream.write("[\n");
            for (int index = 0; index < runtimeDatabase.size(); index++) {
                filePipelineStream.write(runtimeDatabase.get(index).storeMessage());
                if (index < runtimeDatabase.size() - 1) {
                    filePipelineStream.write(",\n");
                }
            }
            filePipelineStream.write("\n]");
            System.out.println("[JSON STORAGE SYNCED]: Records safely stored to local database.");
        } catch (IOException e) {
            System.out.println("[CRITICAL WRITE ERROR]: IO storage failure.");
        }
    }
}
