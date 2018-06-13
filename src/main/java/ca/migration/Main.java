package ca.migration;

public class Main {

    public static void main(String[] args) {
        int[] array = new int[100];
        System.out.println("Start :");
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 100);
            System.out.print(array[i] + ", ");
        }

        sort(array);
        sortShell(array);
        sortQuick(array, 0, array.length - 1);

        System.out.println("\nEnd :");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");
        }
    }

    private static void sort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j >= 1 && array[j - 1] > array[j]; j--) {
                int tmp = array[j - 1];
                array[j - 1] = array[j];
                array[j] = tmp;
            }
        }
    }

    private static void sortShell(int[] array) {
        int n = array.length;
        for (int h = n / 3; h >= 1; h = h / 3) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && array[j - h] > array[j]; j = j - h) {
                    int tmp = array[j - h];
                    array[j - h] = array[j];
                    array[j] = tmp;
                }
            }
        }
    }

    private static void sortQuick(int[] array, int lo, int hi) {
        if (hi <= lo) return;

        int i = lo;
        int mid = hi + 1;
        int v = array[lo];

        while (true) {
            while (array[++i] < v && i != hi) ;
            while (v < array[--mid] && mid != lo) ;
            if (i >= mid) break;

            int tmp = array[i];
            array[i] = array[mid];
            array[mid] = tmp;
        }

        int tmp = array[lo];
        array[lo] = array[mid];
        array[mid] = tmp;

        sortQuick(array, lo, mid - 1);
        sortQuick(array, mid + 1, hi);
    }
}
