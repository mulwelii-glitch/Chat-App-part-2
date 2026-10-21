package chatapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testMessageIDCharactersBoundaries() {
        Message msg = new Message(1, "+27718693002", "Sample validation context.");
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testRecipientCellValidationSuccess() {
        Message msg = new Message(1, "+27718693002", "Sample text.");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashAlgorithmicSequenceFormula() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hashResult = msg.getMessageHash();
        
        String firstTwoDigitsToken = msg.getMessageId().substring(0, 2);
        String expectedHashSequenceStructure = firstTwoDigitsToken + ":0:HITONIGHT";
        
        assertEquals(expectedHashSequenceStructure.toUpperCase(), hashResult);
    }

    @Test
    public void testMessageCharacterLimitValidationExceeded() {
        StringBuilder longStringBuilder = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            longStringBuilder.append("x");
        }
        
        Message msg = new Message(1, "08575975889", longStringBuilder.toString());
        String statusOutput = msg.SentMessage(1);
        
        // Verifies the exact required error string from your rubric sheet layout
        assertEquals("Message exceeds 250 characters by 5, please reduce the size.", statusOutput);
    }
}
