package gui;

import java.util.List;

public interface IMyTableModel<T> {
    /**
     * The modifier of the columns
     */
    public final int[] columnSizeModifier = new int[] { 3, 1, 1, 1, 1 };

    /**
     * Add an item in the table
     * @param item
     */
    public void add(T item);

    /**
     * Remove the item at rowIndex in the table
     * @param rowIndex
     */
    public void remove(int rowIndex);

    /**
     * Get the item at index in the table
     * @param index
     * @return the item
     */
    public T getItem(int index);

    /**
     * Get the list of the table
     * @return the list
     */
    public List<T> getList();
    
    /**
     * Sets the list of the table
     * @param list
     */
    public void setList(List<T> list);

    /**
     * Clears the table
     */
    public void clear();
}
