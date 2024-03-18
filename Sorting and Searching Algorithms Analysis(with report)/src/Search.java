public class Search {
    protected static int linearSearch(int [] arr, int x){
        int size = arr.length;
        for (int i = 0; i < size; i++){
            if(arr[i] == x)
                return i;
        }
        return -1;
    }

    protected static int binarySearch(int [] arr, int x){
        int low = 0;
        int high = arr.length - 1;
        while (high - low > 1){
            int mid = (high + low) / 2;
            if(x > arr[mid]){
                low = mid + 1;
            }else{
                high = mid;
            }
        }
        if(arr[low] == x){
            return low;
        }else if(arr[high] == x){
            return high;
        }

        return -1;
    }
}
