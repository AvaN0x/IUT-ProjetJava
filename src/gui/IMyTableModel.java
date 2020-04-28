package gui;

import java.util.ArrayList;

public interface IMyTableModel<T> {
    public final int[] columnSizeModifier = new int[] { 3, 1, 1, 1, 1 };

    public void add(T item);

    public void remove(int rowIndex);

    public T getItem(int index);

    public ArrayList<T> getList();
    
    public void setList(ArrayList<T> list);
}
