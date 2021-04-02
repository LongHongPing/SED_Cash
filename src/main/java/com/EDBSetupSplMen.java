package com;

import com.concurrent.Task;
import com.utils.GenUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/** 索引文件生成 */
public class EDBSetupSplMen {
    private final static MasterKey mk = EDBSetupKeyUtil.getKey();
    private final static Pairing pairing = PairingFactory.getPairing("param/curves/a.properties");

    /** 算法实现 */
    public static Task EDBSetup(final String w,final Collection<String> indexes) throws Exception{
        ArrayList<String> XSet = new ArrayList<>();
        HashMap<String,ArrayList<Item>> map = new HashMap<>();
        ArrayList<Item> items = new ArrayList<>();
        byte[] Ke = GenUtil.F(mk.getKs(),w);
        int cnt = 0;
        //处理每个文件
        for(final String index : indexes){
            //计算TSet
            final Element xIndex = GenUtil.Fp(pairing,mk.getKi(),index.getBytes("utf-8"));
            final Element z = GenUtil.Fp(pairing,mk.getKz(),(w + cnt + "").getBytes("utf-8"));
            Element y = xIndex.duplicate().mul(z.duplicate().invert());

            final byte[] e = GenUtil.encAES(Ke,index);
            items.add(new Item(e,new SerializableElement(y)));
            //计算XSet
            Element t1 = GenUtil.Fp(pairing,mk.getKx(),w.getBytes("utf-8"));
            Element t2 = t1.duplicate().mul(xIndex);
            Element result = mk.getG().duplicate().powZn(t2.duplicate());
            XSet.add(new SerializableElement(result).getElement().toString());

            cnt++;
        }
        byte[] stag = TSetSplitMem.TSetGetTag(w);
        return new Task(Arrays.toString(stag),items,XSet);
    }
}
