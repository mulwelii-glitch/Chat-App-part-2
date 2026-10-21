package chatapp;

import java.util.Random;

/**
 * The Message class manages individual chat message properties, 
 * unique tracking IDs, formatting hashes, and character limit checks.
 */
public class Message {
    private String messageId;
    private int messageNumber; 
    private String recipientNumber;
    private String messageText;
    private String messageHash;

    /**
     * Parameterized Constructor initializing core message parameters.
     */
    public Message(int messageNumber, String recipientNumber, String messageText) {
        this.messageNumber = messageNumber;
        this.recipientNumber = recipientNumber;
        this.messageText = messageText;
        
        // Autogenerate tracking data attributes
        this.messageId = generateRandom10DigitID();
        this.messageHash = createMessageHash();
    }

    /**
     * Requirement: Generates a randomly generated ten-digit number string.
     */
    private String generateRandom10DigitID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Requirement: Ensures that the message ID is not more than ten characters.
     */
    public boolean checkMessageID() {
        return this.messageId != null && this.messageId.length() <= 10;
    }

    /**
     * Requirement: Ensures that the recipient cell number starts with a code.
     */
    public String checkRecipientCell() {
        if (this.recipientNumber == null || this.recipientNumber.isEmpty()) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        
        boolean isValidInternational = this.recipientNumber.startsWith("+27") && this.recipientNumber.length() == 12;
        boolean isValidLocal = this.recipientNumber.startsWith("0") && this.recipientNumber.length() == 10;

        if (isValidInternational || isValidLocal) {
            return "Cell phone number successfully captured.";
        }
        
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Requirement: Creates and returns the Message Hash in all caps.
     * Formula: [First two digits of ID]:[0-indexed message number]:[FirstWordLastWord]
     */
    public String createMessageHash() {
        if (this.messageId == null || this.messageId.length() < 2) {
            return "00:0:EMPTY";
        }
        
        String firstTwoNumbers = this.messageId.substring(0, 2);
        int zeroIndexedNumber = this.messageNumber - 1; 
        
        String cleanText = this.messageText.trim();
        String[] words = cleanText.split("\\s+");
        
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        String compiledHash = firstTwoNumbers + ":" + zeroIndexedNumber + ":" + firstWord + lastWord;
        return compiledHash.toUpperCase();
    }

    /**
     * Requirement: Evaluates choices and computes the character overage values exactly.
     */
    public String SentMessage(int choice) {
        if (this.messageText != null && this.messageText.length() > 250) {
            int extraCharactersCount = this.messageText.length() - 250;
            return "Message exceeds 250 characters by " + extraCharactersCount + ", please reduce the size.";
        }

        switch (choice) {
            case 1:
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Unknown selection protocol choice.";
        }
    }

    /**
     * Research Requirement: Converts active elements into a clean structured JSON block.
     */
    public String storeMessage() {
        return "  {\n" +
               "    \"messageId\": \"" + messageId + "\",\n" +
               "    \"messageHash\": \"" + messageHash + "\",\n" +
               "    \"recipient\": \"" + recipientNumber + "\",\n" +
               "    \"message\": \"" + messageText + "\"\n" +
               "  }";
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipientNumber() { return recipientNumber; }
    public String getMessageText() { return messageText; }
}
