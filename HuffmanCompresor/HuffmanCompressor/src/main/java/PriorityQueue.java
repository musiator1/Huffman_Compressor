import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T extends Comparable<T>> {
    private final List<T> heap = new ArrayList<>();

    public T getMin() {
        if (heap.size() == 0) {
            return null;
        }

        T retValue = heap.get(0);
        T lastElem = heap.get(heap.size() - 1);
        heap.set(0, lastElem);
        heap.remove(heap.size() - 1);
        heapDown();
        return retValue;
    }

    public void add(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Cannot put null into Priority Queue!");
        }

        heap.add(elem);
        heapUp();
    }

    public int getSize() {
        return heap.size();
    }

    private void heapDown() {
        int parentIndex = 0;
        int leftChildIndex = 1;
        int rightChildIndex = 2;
        int minIndex = parentIndex;

        while (true) {
            if (leftChildIndex < heap.size() && heap.get(leftChildIndex).compareTo(heap.get(minIndex)) < 0) {
                minIndex = leftChildIndex;
            }

            if (rightChildIndex < heap.size() && heap.get(rightChildIndex).compareTo(heap.get(minIndex)) < 0) {
                minIndex = rightChildIndex;
            }

            if (minIndex != parentIndex) {
                swap(minIndex, parentIndex);
                parentIndex = minIndex;
                leftChildIndex = 2 * parentIndex + 1;
                rightChildIndex = 2 * parentIndex + 2;
            } else {
                break;
            }
        }
    }

    private void heapUp() {
        int childIndex = heap.size() - 1;
        while (childIndex > 0) {
            int parentIndex = (childIndex - 1) / 2;
            if (heap.get(childIndex).compareTo(heap.get(parentIndex)) < 0) {
                swap(childIndex, parentIndex);
                childIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    private void swap(int firstId, int secondId) {
        T tmp = heap.get(firstId);
        heap.set(firstId, heap.get(secondId));
        heap.set(secondId, tmp);
    }
}