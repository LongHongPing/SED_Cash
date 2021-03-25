package com;

import org.junit.Test;

public class EDBSetupKeyUtilTest {
    @Test
    /** 密钥生成 */
    public void getKeyTest(){
        MasterKey mk = EDBSetupKeyUtil.getKey();
        System.out.println(mk);
    }
}
