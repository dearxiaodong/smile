package springboot.service.impl;

/**
 * @author 357069486@qq.com
 * @date 2018-11-8 14:20
 */
public class assassin {
    private static assassin ourInstance = new assassin();

    public static assassin getInstance() {
        return ourInstance;
    }

    private assassin() {
    }
}
