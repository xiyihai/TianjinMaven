package services.Interface;

import java.util.Set;

public interface SearchService {

	  //通过cellid查找对应的基站信息，返回一个
    String findBSbyCellid(String cellid);
    
    //通过cellName查找对应基站信息，可能返回一组
    String  findBSbyCellName(String cellName,int offset,int length);
    
    //通过cellName来匹配复合的name返回
    String matchCellName(String cellName,int offset,int length);
    
    //通过select2来name匹配，这里name是字符串数组，不能利用上面的函数,需要重新定义
    String findBSByS2NameArray(String[] cellNames);
    
    //删除搜索到的基站
    Set<String> deleteBS(String[] cellidArray);
    
    //根据删除的cellid数组，修改Voronoi表
    void updateVoronoiForOut(Set<String> enBids);
}
