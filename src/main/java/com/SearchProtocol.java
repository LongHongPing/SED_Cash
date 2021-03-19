package com;

import com.db.MysqlDb;
import com.utils.GenUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.List;

/** 搜索 */
public class SearchProtocol {
    private static MasterKey mk;
    private static Pairing pairing;
    static{
        mk = EDBSetupKeyUtil.getKey();
        pairing = PairingFactory.getPairing("param/curves/a.properties");
    }
    /** 客户端业务1 */
    public static SearchClient searchClient(ArrayList<String> keywords) throws Exception{
        Element g = mk.getG();
        byte[] stag = TSetSplitMem.TSetGetTag(keywords.get(0));
        Element[][] xtoken = new Element[100][keywords.size()];

        for(int i = 0;i < 100;i++){
            for(int j = 1;j < keywords.size();j++){
                Element left = GenUtil.Fp(pairing,mk.getKz(),(keywords.get(0) + i + "").getBytes("utf-8"));
                Element right = GenUtil.Fp(pairing,mk.getKx(),keywords.get(j).getBytes("utf-8"));
                Element result = g.duplicate().powZn(left.duplicate().mul(right));
                xtoken[i][j] = result;
            }
        }
        return new SearchClient(stag,xtoken);
    }
    public static SearchClient searchClient(List<String> keywords,int count) throws Exception{
        Element g = mk.getG();
        byte[] stag = TSetSplitMem.TSetGetTag(keywords.get(0));
        Element[][] xtoken = new Element[count][keywords.size()];

        for(int i = 0;i < count;i++){
            for(int j = 1;j < keywords.size();j++){
                Element left = GenUtil.Fp(pairing,mk.getKz(),(keywords.get(0) + i + "").getBytes("utf-8"));
                Element right = GenUtil.Fp(pairing,mk.getKx(),keywords.get(j).getBytes("utf-8"));
                Element result = g.duplicate().powZn(left.duplicate().mul(right));
                xtoken[i][j] = result;
            }
        }
        return new SearchClient(stag,xtoken);
    }
    /** 客户端业务2 */
    public static ArrayList<String> searchClient(String w,ArrayList<byte[]> es) throws Exception{
        ArrayList<String> indexes = new ArrayList<>();
        byte[] Ke = GenUtil.F(mk.getKs(),w);
        for(byte[] e : es){
            String index = GenUtil.deAES(Ke,e);
            indexes.add(index);
        }
        return indexes;
    }
    /** 服务器端业务 */
    public static ArrayList<byte[]> searchServer(SearchClient searchClient,String tableName) throws Exception{
        ArrayList<byte[]> results = new ArrayList<>();
        ArrayList<Item> items = TSetSplitMem.TSetRetrive(searchClient.getStag(),tableName);
        MysqlDb mysqlDb = new MysqlDb();

        if(items == null){
            return null;
        }

        for(int i = 0;i < items.size();i++){
            boolean flag = true;
            Item item = items.get(i);
            Element y = item.getY().getElement();
            for(int j = 1;j < searchClient.getXtoken()[i].length;j++){
                Element e = searchClient.getXtoken()[i][j].duplicate();
                Element ee = e.duplicate().powZn(y);

                flag = flag && MysqlDb.isExistXSet(ee.toString());
                if(!flag){
                    break;
                }
            }
            if(flag){
                results.add(item.getE());
            }
        }
        return results;
    }
}


