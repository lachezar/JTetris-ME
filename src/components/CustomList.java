/*
 * CustomList.java
 *
 * Created on Четвъртък, 2007, Декември 27, 2:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

/**
 * Custom version of list which is used for containing the figures used
 * in JTetris project. The CustomList however can work with (contain) elements 
 * of all classes, which are subclasses of Object.
 * @author lucho
 */
public class CustomList {
    
    private class Node {
        private Node prev;
        private Node next;
        private Object data;
        
        private Node(Node prev, Object data) {
            this.prev = prev;
            this.data = data;
        }
        
        private Node() {
        }
        
        private Node(Node node) {
            node.prev = this.prev;
            node.next = this.next;
            node.data = this.data;
        }
    }
    
    private class Iterator {
        private Node current;
        
        private Iterator(Node start) {
            this.current = start;
        }
        
        private Node getNext() {
            current = current.next;
            return current;
        }
        
    }
    
    private Node start;
    private Node current;
    private int lenght;
    private int originalLenght;
    
    /**
     * Creates new empty CustomList
     */
    public CustomList() {
        start = new Node();
        current = start;
    }
    
    /**
     * Creates new CustomList from another CustomList (Copy constructor)
     * @param l the CustomList which is copied
     */
    public CustomList(CustomList l) {
        this();
        originalLenght = l.originalLenght;
        
        for (int i = 0; i < l.lenght; i++) {
            this.add(l.getElement(i));
        }
        
    }
    
    /**
     * Creates new CustomList and fills it with values
     * @param obj array of Object instances which will fill the CustomList
     */
    public CustomList(Object[] obj) {
        this();
        this.add(obj);
    }
    
    /**
     * Obtains element on a specific position
     * @param n position of the element which will be obtained
     * @return the element on the specific position
     */
    public Object getElement(int n) {
        if (n >= lenght) return null;
        Iterator iter = new Iterator(start);
        for (int i = 0; i < n; i++) {
            iter.getNext();                        
        }
        
        return iter.getNext().data;
    }
    
    /**
     * Add new element at the end of the list
     * @param data the data which will be stored
     */
    public void add(Object data) {
        originalLenght++;
        lenght++;
        current.next = new Node(current, data);
        Node temp = current;
        current = current.next;
        current.prev = temp;
    }
    
    /**
     * Add new elements at the end of the list
     * @param data the array of data which will be stored
     */
    public void add(Object[] data) {
        originalLenght += data.length;
        lenght += data.length;
        for (int i = 0; i < data.length; i++) {
            current.next = new Node(current, data[i]);
            Node temp = current;
            current = current.next;
            current.prev = temp;
        }
    }
    
    /**
     * Remove element at specific position in the list
     * @param n position of the element which will be removed
     * @return the element which is removed from the list
     */
    public Object remove(int n) {
        if (n >= lenght) return null;
        lenght--;
        Iterator iter = new Iterator(start);
        for (int i = 0; i < n; i++) {
            iter.getNext();                        
        }
        
        Node node = iter.getNext();
        node.prev.next = node.next;
        if (node.next != null) {
            node.next.prev = node.prev;                
        } else {
            current = node.prev;
        }
        
        return node.data;
    }
    
    /**
     * Inserts new element at specific position
     * @param n position before which will be inserted the element
     * @param data the data which will be inserted in the list
     */
    public void insert(int n, Object data) {
        if (n > lenght) return;
        if (n == lenght) {
            add(data);
            return;
        }
        lenght++;
        Iterator iter = new Iterator(start);
        for (int i = 0; i < n; i++) {
            iter.getNext();                        
        }
        
        Node node = iter.getNext();
        Node temp = new Node(node.prev, data);
        temp.next = node;
        node.prev.next = temp;
        node.prev = temp;
        
    }
    
    /**
     * Obtains the lenght of the list
     * @return length of the list
     */
    public int lenght() {
        return lenght;
    }
    
}
