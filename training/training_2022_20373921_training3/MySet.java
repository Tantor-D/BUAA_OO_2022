import java.util.ArrayList;
import java.util.HashSet;

public class MySet implements IntSet {
    private ArrayList<Integer> arr;
    private int count;

    public MySet() {
        arr = new ArrayList<>();
        count = 0;
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
    public Boolean contains(int x) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == x) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void insert(int x) {
        int left = 0;
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
            if(arr.get(pos) != x) {
                arr.add(pos, x);
                count++;
            }
        }
    }

    @Override
    public void delete(int x) {
        int left = 0;
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
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            temp.add(a.getNum(i));
        }
        for (Integer integer : temp) {
            a.delete(integer);
        }
        for (Integer integer : arr) {
            a.insert(integer);
        }
        ArrayList<Integer> temp2 = new ArrayList<>(arr);
        for (Integer integer : temp2) {
            delete(integer);
        }
        for (Integer integer : temp) {
            insert(integer);
        }
    }

    @Override
    public IntSet symmetricDifference(IntSet a) throws NullPointerException {
        MySet ans = new MySet();
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < a.size(); i++) {
            ans.insert(a.getNum(i));
            set.add(a.getNum(i));
        }
        for (int i = 0; i < size(); i++) {
            ans.insert(getNum(i));
            set.add(getNum(i));
        }
        for (int i : set) {
            if (contains(i) && a.contains(i)) {
                ans.delete(i);
            }
        }
        return ans;
    }

    @Override
    public boolean repOK() {
        int num = arr.size() - 1;
        for (int i = 0; i < num; i++) {
            if (arr.get(i) >= arr.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
