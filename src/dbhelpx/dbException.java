/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbhelpx;

/**
 *
 * @author Administrator
 */
public class dbException extends Exception {

    /**
     * Creates a new instance of <code>dbException</code> without detail
     * message.
     */
    public dbException() {
    }

    /**
     * Constructs an instance of <code>dbException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public dbException(String msg) {
        super(msg);
    }
}
