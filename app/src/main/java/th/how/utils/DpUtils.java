package th.how.utils;

/**
 * Created by me_touch on 17-9-19.
 * 动态规划篇, 背包九讲示例之Java
 */

public class DpUtils {

    /**
     * 场景一: 01背包
     * 有N 件物品和一个容量为V 的背包。放入第i 件物品耗费的费用是Ci，得到的价值是Wi。求解将哪些物品装入背包可使价值总和最大
     * @param num 物体总数, 为了方便与数组映射
     * @param perSpace 单个物体所占空间
     * @param perValue 单个物体价值
     * @param totalSpace 总空间
     * @return 最大价值
     */
    public static int zeroOnePack(int num, int[] perSpace, int[] perValue, int totalSpace){
        int[][] value = new int[num][totalSpace + 1];
        if(perSpace == null || perValue == null || num != perSpace.length || num != perValue.length)
            throw new IllegalArgumentException("num must be equal with the length of the array");
        for(int i = 0; i <= totalSpace; i ++){
            if(perSpace[0] > i)
                value[0][i] = 0;
            else
                value[0][i] = perValue[0];

        }
        for (int i = 1; i < num; i++) {
            for(int j = 0; j <= totalSpace; j ++){
                if(j >= perSpace[i])
                    value[i][j] = Math.max(value[i - 1][j], value[i - 1][j - perSpace[i]] + perValue[i]);
                else
                    value[i][j] = value[i - 1][j];
            }
        }
        return value[num - 1][totalSpace];
    }

    /**
     * 场景一: 01背包优化版, 降低空间复杂度
     * 有N 件物品和一个容量为V 的背包。放入第i 件物品耗费的费用是Ci，得到的价值是Wi。求解将哪些物品装入背包可使价值总和最大
     * @param num 物体总数, 为了方便与数组映射
     * @param perSpace 单个物体所占空间
     * @param perValue 单个物体价值
     * @param totalSpace 总空间
     * @return 最大价值
     */
    public static int optimizeZeroOnePack(int num, int[] perSpace, int[] perValue, int totalSpace){
        int[] value = new int[totalSpace + 1];
        if(perSpace == null || perValue == null || num != perSpace.length || num != perValue.length)
            throw new IllegalArgumentException("num must be equal with the length of the array");
        for (int i = 0; i < num; i++) {
            for(int j = totalSpace; j >= perSpace[i]; j --){
                value[j] = Math.max(value[j], value[j - perSpace[i]] + perValue[i]);
            }
        }
        return value[totalSpace];
    }

    /**
     * 场景二: 完全背包
     * 有N 种物品和一个容量为V 的背包, 每种物品数量不限。放入第i 件物品耗费的费用是Ci，得到的价值是Wi。求解将哪些物品装入背包可使价值总和最大
     * @param num 物体总数, 为了方便与数组映射
     * @param perSpace 单个物体所占空间
     * @param perValue 单个物体价值
     * @param totalSpace 总空间
     * @return 最大价值
     */
    public static int completePack(int num, int[] perSpace, int[] perValue, int totalSpace){
        int[][] value = new int[num][totalSpace + 1];
        if(perSpace == null || perValue == null || num != perSpace.length || num != perValue.length)
            throw new IllegalArgumentException("num must be equal with the length of the array");
        for(int i = 0; i <= totalSpace; i ++){
            if(perSpace[0] > i)
                value[0][i] = 0;
            else
                value[0][i] = perValue[0];

        }
        for (int i = 1; i < num; i++) {
            for(int j = 0; j <= totalSpace; j ++){
                if(j >= perSpace[i]){
                    int centerValue = 0;
                    for (int k = 0; k <= j / perSpace[i]; k ++){
                        centerValue = Math.max(centerValue, value[i - 1][j - k * perSpace[i]]  + k * perValue[i]);
                    }
                    value[i][j] = centerValue;
                } else
                    value[i][j] = value[i - 1][j];
            }
        }
        return value[num - 1][totalSpace];
    }


    /**
     * 场景二: 完全背包: 转化为01背包
     * 有N 种物品和一个容量为V 的背包, 每种物品数量不限。放入第i 件物品耗费的费用是Ci，得到的价值是Wi。求解将哪些物品装入背包可使价值总和最大
     * @param num 物体总数, 为了方便与数组映射
     * @param perSpace 单个物体所占空间
     * @param perValue 单个物体价值
     * @param totalSpace 总空间
     * @return 最大价值
     */
    public static int complete2ZeroOnePack(int num, int[] perSpace, int[] perValue, int totalSpace){
        int[][] value = new int[num][totalSpace + 1];
        if(perSpace == null || perValue == null || num != perSpace.length || num != perValue.length)
            throw new IllegalArgumentException("num must be equal with the length of the array");
        for(int i = 0; i <= totalSpace; i ++){
            if(perSpace[0] > i)
                value[0][i] = 0;
            else
                value[0][i] = perValue[0];

        }
        for (int i = 1; i < num; i++) {
            for(int j = 0; j <= totalSpace; j ++){
                if(j >= perSpace[i])
                    value[i][j] = Math.max(value[i - 1][j], value[i][j - perSpace[i]] + perValue[i]);
                else
                    value[i][j] = value[i - 1][j];
            }
        }
        return value[num - 1][totalSpace];
    }

    /**
     * 场景二: 完全背包:转化为01背包优化版
     * 有N 种物品和一个容量为V 的背包, 每种物品数量不限。放入第i 件物品耗费的费用是Ci，得到的价值是Wi。求解将哪些物品装入背包可使价值总和最大
     * @param num 物体总数, 为了方便与数组映射
     * @param perSpace 单个物体所占空间
     * @param perValue 单个物体价值
     * @param totalSpace 总空间
     * @return 最大价值
     */
    public static int optimizeCompletePack(int num, int[] perSpace, int[] perValue, int totalSpace){
        int[] value = new int[totalSpace + 1];
        if(perSpace == null || perValue == null || num != perSpace.length || num != perValue.length)
            throw new IllegalArgumentException("num must be equal with the length of the array");
        for (int i = 0; i < num; i++) {
            for (int j = perSpace[i]; j <= totalSpace; j ++){
                if(perSpace[i] <= j)
                    value[j] = Math.max(value[j], value[j - perSpace[i]] + perValue[i]);
            }
        }
        return value[totalSpace];
    }
}
