package gui;

import java.util.List;

public interface IMyTableModel<T> {
    public final int[] columnSizeModifier = new int[] { 3, 1, 1, 1, 1 };

    public void add(T item);

    public void remove(int rowIndex);

    public T getItem(int index);

    public List<T> getList();
    
    public void setList(List<T> list);
}
