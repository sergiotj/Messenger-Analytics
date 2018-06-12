/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengeranalytics;

/**
 *
 * @author Sergio
 */
public class Message {
    
    private String sender;
    private Long timestamp;
    private String content;

    public Message(Long timestamp, String content) {

        this.timestamp = timestamp;
        this.content = content;
    }
    
    public String getSender() {
        
        return this.sender;
    }
    
    public Long getTimestamp() {
        
        return this.timestamp;
    }
    
    public String getContent() {
        
        return this.content;
    }
    
}
