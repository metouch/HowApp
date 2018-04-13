package th.algorithm.practice;

/**
 * Created by me_touch on 17-9-22.
 * 常见笔试算法题的解
 */

public class SolutionUtils {

    /**
     * 场景:输出n行内的杨辉三角
     * @param row 行数
     */

    public static void YHTriAngle(int row){
        int[] arr = new int[row];
        int pre = 1;
        arr[0] = 1;
        for(int i = 0; i < row; i ++){
            for(int j = 0; j <= i; j ++){
                int tenp = arr[j];
                arr[j] = j == 0 ? arr[j] : arr[j] + pre;
                System.out.print(arr[j] + "  ");
                pre = tenp;
            }
            System.out.println();
        }
    }

    /**
     *
     * @param row 行数
     * @return 杨辉三角的值
     */
    public static int[] valueOfYHTriAngle(int row){
        int length = recursiveAdd(row, 0);
        int[] value = new int[length];
        int anchor = 0;
        for (int i = 1; i <= row; i++) {

            for(int j = 0; j < i; j++){
                if(j == 0 || j == i -1){
                    value[anchor] = 1;
                }else {
                    int cur = anchor - i + 1;
                    int pre = cur - 1;
                    value[anchor] = value[cur] + value[pre];
                }
                anchor++;
            }
        }
//      输出结果
//        anchor = 0;
//        for (int i = 1; i <= row; i++) {
//            for (int j = 0; j < i; j++) {
//                System.out.print(value[anchor] + "  ");
//                anchor ++;
//            }
//            System.out.println();
//        }
        return value;
    }

    /**
     *
     * @param arr 由1, 0 构成的数组,两个1之间不相邻,例如1,0,0,0,1
     * @param num 需要插入的1的个数, 插入后满足1的个数不相邻, 1
     * @return 是否能完全插入, 以上述数据为例,则返回true
     */
    public static boolean zeo2one(int[] arr, int num){
        if(arr == null || arr.length < 2 * num - 1) return false;
        int length = arr.length;
        for (int i = 0; i < arr.length; i++) {
            if(num <= 0)
                break;
            if(arr[i] != 1){
                arr[i] = 1;
                boolean flag = i - 1 >= 0 && arr[i - 1] == 1;
                flag |= i + 1 < length && arr[i + 1] == 1;
                if(flag)
                    arr[i] = 0;
                else
                    num --;
            }
        }
        return num <= 0;
    }


    public static int recursiveAdd(int num, int result){
        if(num == 0)
            return result;
        return recursiveAdd(num - 1, result + num);
    }
}
