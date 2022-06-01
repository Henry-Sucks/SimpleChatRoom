import java.net.Socket;
import java.util.*;

public class UserMap <k,v>{
    public Map<k, v> map = Collections.synchronizedMap(new HashMap<k, v>());


    // 根据value来删除指定项
    public synchronized void removeByValue(Object value) {
        for (Object key : map.keySet()) {
            if (map.get(key) == value) {
                map.remove(key);
                break;
            }
        }
    }

    //获取所有value组成的Set集合
    public synchronized Set<v> valueSet(){
        Set<v> result=new HashSet<v>();
        //将map中的所有value添加到result集合中
        map.forEach((key,value)->result.add(value));
        return result;
    }

    public synchronized ArrayList<v> toArrayList(){
        ArrayList<v> result = new ArrayList<>();
        map.forEach((key,value)->result.add(value));
        return result;
    }

    //根据value查找key
    public synchronized k getKeyByValue(v value){
        //遍历所有key组成的集合
        for (k key:map.keySet()){
            //如果指定key对应的value与被搜索的value相同，则返回对应的key
            if(map.get(key)==value||map.get(key).equals(value)){
                return key;
            }
        }
        return null;
    }

    public synchronized v getValueByKey(k key){
        return map.get(key);
    }


    //实现put()方法，该方法不允许value重复
    public synchronized v put(k key,v value){
        //遍历所有value组成的集合
        for (v val:valueSet()){
            //如果某个value与试图放入集合的value相同
            //则抛出一个RuntimeException异常
            if (val.equals(value)&&val.hashCode()==value.hashCode()){
                throw new RuntimeException("MyMap实例不允许有重复的value");
            }
        }
        return map.put(key,value);
    }

    public synchronized void remove(k key){
        map.remove(key);
    }
}
