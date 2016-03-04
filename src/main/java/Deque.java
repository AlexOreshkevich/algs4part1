import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double-ended queue or deque (pronounced "deck") is a generalization of a
 * stack and a queue that supports adding and removing items from either the
 * front or the back of the data structure.
 *
 * @author Alex Oreshkevich
 * @see {@linkplain http://coursera.cs.princeton.edu/algs4/assignments/queues.html}
 */
public class Deque<Item> implements Iterable<Item> {

  // wraps items to a node with next item reference
  private class Node {
    Item item;
    Node next;

    Node() {
    }

    Node(Item item) {
      this.item = item;
    }

    @Override
    public String toString() {
      return " " + item + " -> " + next + " ";
    }
  }

  private Node first, last;

  /**
   * Count of the items that are currently in the deque.
   */
  private int count;

  /**
   * Construct an empty deque.
   */
  public Deque() {
  }

  /**
   * Returns true if deque is empty.
   *
   * @return empty
   */
  public boolean isEmpty() {
    return first == null;
  }

  /**
   * Return the number of items on the deque.
   *
   * @return items size
   */
  public int size() {
    return count;
  }

  /**
   * Add the item to the front.
   *
   * @param item item to store
   */
  public void addFirst(Item item) {
    doAdd(item, true);
  }

  /**
   * Add the item to the end.
   *
   * @param item item to store
   */
  public void addLast(Item item) {
    doAdd(item, false);
  }

  private void doAdd(Item item, boolean fromHead) {
    validateNonNull(item);
    Node newNode = new Node(item);

    if (isEmpty()) {
      last = first = newNode;
      first.next = null;
      last.next = null;
    } else {
      updateLinks(fromHead, newNode);
    }

    count++;
    showState();
  }

  private void updateLinks(boolean fromHead, Node newNode) {
    if (fromHead) {
      Node oldFirst = first;
      first = newNode;
      first.next = oldFirst;
    } else {
      Node oldLast = last;
      oldLast.next = newNode;
      last = newNode;
    }

    if (size() == 1) {
      //first.next = newNode;
    }
  }

  /**
   * Remove and return the item from the front.
   *
   * @return first item
   */
  public Item removeFirst() {

    throwIfEmptyDeque();
    count--;

    Item firstItem = first.item;
    first = first.next;

    return firstItem;
  }

  /**
   * Remove and return the item from the end.
   *
   * @return last item
   */
  public Item removeLast() {

    throwIfEmptyDeque();
    count--;

    Item lastItem = last.item;
    last = last.next;

    return lastItem;
  }

  void showState() {
    System.out.println("\n\t[deque state] size: " + size() + "\n\t\tfirst: " + first + "\n\t\tlast: " + last);
  }

  static void validateNonNull(Object o) {
    if (o == null) {
      throw new NullPointerException();
    }
  }

  void throwIfEmptyDeque() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
  }

  /**
   * Return an iterator over items in order from front to end.
   *
   * @return
   */
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item> {

    private Node current;

    DequeIterator() {
      current = first;
    }

    int cntr;

    @Override
    public boolean hasNext() {
      return ++cntr < 20 && current != null;
    }

    @Override
    public Item next() {

      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      Item currentItem = current.item;
      current = current.next;

      return currentItem;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Unit testing.
   *
   * @param args
   */
  public static void main(String[] args) {

    buildDeque();

    // testAddAndRemoveFromHead();
    //testAddAndRemoveFromTail();
  }

  private static void testAddAndRemoveFromHead() {
    Deque<String> deque = buildDeque();

    deque.removeFirst();
    displayQueue(deque); // [2 3]

    deque.removeFirst();
    displayQueue(deque); // [3]
  }

  private static void testAddAndRemoveFromTail() {

    Deque<String> deque = buildDeque();

    deque.removeLast();
    displayQueue(deque); // [2 3]

    deque.removeLast();
    displayQueue(deque); // [3]
  }

  private static Deque<String> buildDeque() {
    Deque<String> deque = new Deque<String>();
    deque.addLast("1");
    deque.addLast("2");
    deque.addFirst("3");
    deque.addFirst("4");
    deque.addLast("5");

    displayQueue(deque);
    return deque;
  }


  private static void displayQueue(Deque<?> deque) {
    System.out.print("Deque with " + deque.size() + " items: [");

    Iterator it = deque.iterator();
    for (int i = 0; i < deque.size(); i++) {
      System.out.print(it.next().toString());
      System.out.print(" ");
    }

    System.out.print("]\n");
  }


}
