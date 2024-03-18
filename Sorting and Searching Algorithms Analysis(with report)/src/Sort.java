public class Sort {
    protected static void insertionSort(int[] arr){
        for(int j = 1; j < arr.length; j++){
            int key = arr[j];
            int i = j - 1;
            while (i >= 0 && key < arr[i]){
                arr[i + 1] = arr[i];
                i = i - 1;
            }
            arr[i + 1] = key;
        }
    }



    protected static int[] mergeSort(int [] arr){
        int len = arr.length;

        if(len <= 1){
            return arr;
        }
        int [] left = new int[len / 2];
        System.arraycopy(arr, 0, left, 0, len / 2);
        left = mergeSort(left);

        int [] right = new int[len - len/2];
        System.arraycopy(arr, len / 2, right, 0, len - (len / 2));
        right = mergeSort(right);



        return merge(left, right);
    }

    private static int[] merge(int[] arr1, int[] arr2){
        int [] result = new int [arr1.length + arr2.length];
        int i = 0;
        int j = 0;

        int r = 0;
        while (i < arr1.length && j < arr2.length){
            if(arr1[i] < arr2[j]){
                result[r++] = arr1[i++];
            }else{
                result[r++] = arr2[j++];
            }
        }

        while (i < arr1.length){
            result[r++] = arr1[i++];
        }
        while (j < arr2.length){
            result[r++] = arr2[j++];
        }

        return result;
    }



    protected static int[] countingSort(int [] input, int k){
        int size = input.length;
        int [] count = new int [k + 1];
        int [] output = new int [size];

        for (int j : input) {
            count[j]++;
        }

        for(int i = 1; i <= k; i++){
            count[i] = count[i] + count[i - 1];
        }

        for(int i = size - 1; i >= 0; i--){
            int j = input[i];
            count[j]--;
            output[count[j]] = j;
        }

        return output;
    }
}
