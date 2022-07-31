import java.util.ArrayList;

public class MySet implements IntSet {
    private ArrayList<Integer> arr;
    private int count;

    public MySet() {
        arr = new ArrayList<>();
        count = 0;
    }

    @Override
    public Boolean contains(int x) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == x) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNum(int x) throws IndexOutOfBoundsException {
        if (x >= 0 && x < count) {
            return arr.get(x);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void insert(int x) {
        int left = 1;
        int right = count - 1;
        int pos = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr.get(mid) >= x) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (pos == -1) {
            count++;
            arr.add(x);
        } else {
            arr.add(pos, x);
            count++;
        }
    }

    @Override
    public void delete(int x) {
        int left = 0;
        int right = count - 1;
        int pos = -1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (arr.get(mid) >= x) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (pos != -1 && arr.get(pos) == x) {
            arr.remove(pos);
            count--;
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void elementSwap(IntSet a) {
        //TODO
    }

    @Override
    public IntSet symmetricDifference(IntSet a) throws NullPointerException {
        //TODO
        return null;
    }

    @Override
    public boolean repOK() {
        //TODO
    }
}
